package fr.echoes.lab.foremanclient;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanapi.model.Filter;
import fr.echoes.lab.foremanapi.model.FilterWrapper;
import fr.echoes.lab.foremanapi.model.HostGroup;
import fr.echoes.lab.foremanapi.model.HostGroupWrapper;
import fr.echoes.lab.foremanapi.model.NewFilter;
import fr.echoes.lab.foremanapi.model.NewRole;
import fr.echoes.lab.foremanapi.model.NewUser;
import fr.echoes.lab.foremanapi.model.Permissions;
import fr.echoes.lab.foremanapi.model.Role;
import fr.echoes.lab.foremanapi.model.RoleWrapper;
import fr.echoes.lab.foremanapi.model.Roles;
import fr.echoes.lab.foremanapi.model.User;
import fr.echoes.lab.foremanapi.model.UserWrapper;

/**
* Noninstantiable Utilities class for working with the foreman API.
*
*/

public class ForemanHelper {

	public static final String URL = "https://pc-ksf.metrixware.local";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "foreman234";

	/**
	 * Private constructor to prevent instantiation.
	 */
	private ForemanHelper() {
		// Do nothing
	}

	/**
	 * Find the role with the given name.
	 *
	 * @param api
	 * @param rolename
	 * @return
	 */
	private static Role findRole(IForemanApi api, String rolename) {

		if (StringUtils.isEmpty(rolename)) { // rolename cannot be null or empty
			throw new IllegalArgumentException();
		}

		final Roles roles = api.getRoles(null, null, null, null);
		for (final Role role : roles.results) {
			final String name = role.name;
			if (rolename.equals(name)) {
				return role;
			}
		}

		return null;
	}

	private static List<Filter> findFilters(IForemanApi api, String rolename) {
		Role role = findRole(api, rolename);
		if (role == null) {
			return null;
		}

		role = api.getRole(role.id);

		return role.filters;
	}

	private static Filter copyFilter(IForemanApi api, Filter filter, String roleId, String searchValue) {
		final FilterWrapper filterWrapper = new FilterWrapper();
		final NewFilter newFiler = new NewFilter();
		newFiler.role_id = roleId;


		final String search;
		if (filter.resource_type != null && filter.resource_type.equals("Hostgroup")) {
			newFiler.permission_ids = getPermissionsIds(filter);
			search = "name  = " + searchValue;
		} else if (filter.resource_type != null && filter.resource_type.equals("Host")) {
			search = "hostgroup = " + searchValue;
			newFiler.permission_ids = getAllPermission(api, filter, "Host");
		} else {
			newFiler.permission_ids = getPermissionsIds(filter);
			search = filter.search;
		}

		newFiler.search = search;

		filterWrapper.setFilter(newFiler);

		return api.createFilter(filterWrapper);
	}

	private static String[] getAllPermission(IForemanApi api, Filter filter, String resourceType) {

		final Permissions permissions = api.getPermissions(null, null, resourceType, null);

		final String[] permissionIds = new String[permissions.results.size()];
		for (int i = 0; i < permissions.results.size(); i++) {
			permissionIds[i] = permissions.results.get(i).id;
		}

		return permissionIds;
	}

	private static String[] getPermissionsIds(Filter filter) {
		final String[] permissionIds = new String[filter.permissions.size()];
		for (int i = 0; i < filter.permissions.size(); i++) {
			permissionIds[i] = filter.permissions.get(i).id;
		}
		return permissionIds;
	}

	public static void createProject(String url, String adminUserName, String password, String projectName, String userId) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		final IForemanApi api = ForemanClient.createApi(url, adminUserName, password);

		createHostGroup(api, projectName);

		final Role role = duplicateRole(api, projectName, "Default user");

		addRoleToUser(api, userId, role.id );
	}

	private static void createHostGroup(IForemanApi api, String projectName) {
		final HostGroupWrapper hostGroupWrapper = new HostGroupWrapper();
		final HostGroup hostGroup = new HostGroup();
		hostGroup.name = projectName;
		hostGroupWrapper.setHostGroup(hostGroup);
		api.createHostGroups(hostGroupWrapper);
	}

	private static Role duplicateRole(final IForemanApi api, String projectName, String roleName) {
		final List<Filter> defaultUserFilters = ForemanHelper.findFilters(api, roleName);

		final NewRole newRole = new NewRole();
		newRole.name = projectName;

		final RoleWrapper roleWrapper = new RoleWrapper();
		roleWrapper.setRole(newRole);


		final Role role = api.createRole(roleWrapper);

		final List<String> filterIds = new ArrayList<>();
		for (final Filter filter : defaultUserFilters) {
			final Filter f = api.getFilter(filter.id);

			final Filter clonedFilter = ForemanHelper.copyFilter(api, f, role.id, projectName);
			filterIds.add(clonedFilter.id);
		}
		return role;
	}

	private static void addRoleToUser(final IForemanApi api, String userId, String roleId) {
		final User user = api.getUser(userId);

		final List<String> roleIds = new ArrayList<String>();

		for (final Role role : user.roles) {
			roleIds.add(role.id);
		}

		roleIds.add(roleId);

		final NewUser newUser = new NewUser();

		final UserWrapper userWrapper = new UserWrapper();
		newUser.role_ids = roleIds;
		userWrapper.setUser(newUser);

		api.updateUser(user.id, userWrapper);
	}

}
