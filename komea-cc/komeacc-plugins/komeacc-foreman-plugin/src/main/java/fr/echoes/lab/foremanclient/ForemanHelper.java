package fr.echoes.lab.foremanclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanapi.model.Environment;
import fr.echoes.lab.foremanapi.model.Environments;
import fr.echoes.lab.foremanapi.model.Filter;
import fr.echoes.lab.foremanapi.model.FilterWrapper;
import fr.echoes.lab.foremanapi.model.Host;
import fr.echoes.lab.foremanapi.model.HostGroup;
import fr.echoes.lab.foremanapi.model.HostGroupWrapper;
import fr.echoes.lab.foremanapi.model.HostWrapper;
import fr.echoes.lab.foremanapi.model.Hostgroups;
import fr.echoes.lab.foremanapi.model.NewFilter;
import fr.echoes.lab.foremanapi.model.NewHost;
import fr.echoes.lab.foremanapi.model.NewRole;
import fr.echoes.lab.foremanapi.model.NewUser;
import fr.echoes.lab.foremanapi.model.Permissions;
import fr.echoes.lab.foremanapi.model.PuppetClassParameter;
import fr.echoes.lab.foremanapi.model.PuppetClassParameters;
import fr.echoes.lab.foremanapi.model.Role;
import fr.echoes.lab.foremanapi.model.RoleWrapper;
import fr.echoes.lab.foremanapi.model.Roles;
import fr.echoes.lab.foremanapi.model.User;
import fr.echoes.lab.foremanapi.model.UserWrapper;
import fr.echoes.lab.foremanclient.model.NetworkInterface;

/**
* Noninstantiable Utilities class for working with the foreman API.
*
*/

public class ForemanHelper {


     private static final Logger LOGGER = LoggerFactory.getLogger(ForemanHelper.class);

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

     public static Host createHost(String url, String adminUserName, String password, String hostName, String computeResourceId, String computeProfileId, String hostGroupName, String environmentName, String operatingSystemId, String architectureId, String puppetConfiguration, String domainId, String rootPassword) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

         //  final Host host = null;
    	 final IForemanApi api = ForemanClient.createApi(url, adminUserName, password);

    	 final NewHost newHost = new NewHost();

    	 newHost.name = hostName;
    	 newHost.compute_resource_id = computeResourceId;
    	 newHost.compute_profile_id = computeProfileId;
    	 newHost.hostgroup_id = "1";
    	 newHost.environment_id = findEnvironmentId(api, environmentName);
    	 newHost.operatingsystem_id = operatingSystemId;
    	 newHost.architecture_id = architectureId;
    	 newHost.domain_id = domainId;
    	 newHost.root_pass = rootPassword;

    	 newHost.puppetclass_ids = getPuppetClassesId(api, environmentName);

    	 newHost.interfaces_attributes.put("0", new NetworkInterface() );

    	 final HostWrapper hostWrapper = new HostWrapper();
    	 hostWrapper.setHost(newHost);

    	 configurePuppet(api, puppetConfiguration);

    	 return api.createHost(hostWrapper);
     }

     private static List<String> getPuppetClassesId(IForemanApi api, String environmentName) {
    	 final String environmentId = findEnvironmentId(api, environmentName);

    	 final List<String> result = new ArrayList<String>();

    	 if (environmentId == null) {
    		 return result;
    	 }

    	 final Environment environment = api.getEnvironment(environmentId);

         for (final fr.echoes.lab.foremanapi.model.PuppetClass puppetClass : environment.puppetClasses) {
        	 result.add(puppetClass.id);
        }

    	 return result;
	}

	private static void configurePuppet(IForemanApi api, String puppetConfiguration) {

          if (StringUtils.isEmpty(puppetConfiguration)) {
               return;
          }

          final ObjectMapper mapper = new ObjectMapper();
          try {
               final JsonNode rootNode = mapper.readTree(puppetConfiguration);
               final JsonNode modulesNode = rootNode.get("variables");
               if (modulesNode.isArray()) {

                    for (final JsonNode moduleNode : modulesNode) {
                         final Map<String, String> overrideValuesMap = new HashMap<String, String>();
                       final String variableId = moduleNode.path("variable_id").asText();
                       final JsonNode overrideValues = moduleNode.path("override_value");
                       if (overrideValues.isArray()) {
                            final Iterator<String> fieldNames = overrideValues.getFieldNames();
                            while (fieldNames.hasNext()) {
                                 final String key = fieldNames.next();
                                 overrideValuesMap.put(key, overrideValues.get(key).asText());
                            }
                       }
                       if (overrideValuesMap.size() > 0) {
                            api.overrideSmartVariable(variableId, overrideValuesMap);
                       }
                   }


               }
          } catch (final IOException e) {
               LOGGER.error("Failed to override puppet module.", e);
          }
     }

     private static String findEnvironmentId(IForemanApi api, String environmentName) {
          final Environments environments = api.getEnvironments();
          for (final Environment hostGroup : environments.results) {
               if (hostGroup.name.equals(environmentName)) {
                    return hostGroup.id;
               }
          }
          return null;
     }


     private static String findHostGroupId(IForemanApi api, String hostGroupName) {
          final Hostgroups hostGroups = api.getHostGroups(null, null, null, null);
          for (final HostGroup hostGroup : hostGroups.results) {
               if (hostGroup.name.equals(hostGroupName)) {
                    return hostGroup.id;
               }
          }
          return null;
     }

     public static void importPuppetClasses(String url, String adminUserName,
               String password, String smartProxyId) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
          final IForemanApi api = ForemanClient.createApi(url, adminUserName, password);
          api.importPuppetClasses(smartProxyId, "{}"); // Doesn't work without a request body as second parameter ("{}")
     }

     public static String getModulesPuppetClassParameters(String url, String adminUserName, String password, String environmentName) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, JsonGenerationException, JsonMappingException, IOException {
          final IForemanApi api = ForemanClient.createApi(url, adminUserName, password);
          final String environmentId = findEnvironmentId(api, environmentName);


          final Environment environment = api.getEnvironment(environmentId);

          final Modules modules = new Modules();

          for (final fr.echoes.lab.foremanapi.model.PuppetClass puppetClass : environment.puppetClasses) {
               final Module module = modules.getOrCreateModule(puppetClass.module_name);
               final PuppetClass pc = module.getOrCreatePuppetClass(puppetClass.name);
               pc.setId(puppetClass.id);
          }


          for (final Module module : modules.modules.values()) {
               for (final PuppetClass puppetClass : module.puppetClasses.values()) {
                    final PuppetClassParameters puppetClassParameters = api.getPuppetClassParameters(puppetClass.id);
                    for (final PuppetClassParameter puppetClassParameter : puppetClassParameters.results) {
                         final Parameter parameter = puppetClass.getOrCreateParameter(puppetClassParameter.parameter);
                         parameter.setId(puppetClassParameter.id);
                         parameter.setValue(puppetClassParameter.default_value);
                         parameter.setOverride(puppetClassParameter.override);
                    }
               }
          }

          final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
          return ow.writeValueAsString(modules);
     }

}