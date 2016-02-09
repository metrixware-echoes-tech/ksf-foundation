package fr.echoes.labs.foremanclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.echoes.labs.foremanapi.IForemanApi;
import fr.echoes.labs.foremanapi.model.Environment;
import fr.echoes.labs.foremanapi.model.Environments;
import fr.echoes.labs.foremanapi.model.Filter;
import fr.echoes.labs.foremanapi.model.FilterWrapper;
import fr.echoes.labs.foremanapi.model.Host;
import fr.echoes.labs.foremanapi.model.HostGroup;
import fr.echoes.labs.foremanapi.model.HostGroupWrapper;
import fr.echoes.labs.foremanapi.model.HostWrapper;
import fr.echoes.labs.foremanapi.model.Hostgroups;
import fr.echoes.labs.foremanapi.model.Hosts;
import fr.echoes.labs.foremanapi.model.NewFilter;
import fr.echoes.labs.foremanapi.model.NewHost;
import fr.echoes.labs.foremanapi.model.NewRole;
import fr.echoes.labs.foremanapi.model.NewUser;
import fr.echoes.labs.foremanapi.model.OverrideValueWrapper;
import fr.echoes.labs.foremanapi.model.Permissions;
import fr.echoes.labs.foremanapi.model.PuppetClassParameter;
import fr.echoes.labs.foremanapi.model.PuppetClassParameters;
import fr.echoes.labs.foremanapi.model.Role;
import fr.echoes.labs.foremanapi.model.RoleWrapper;
import fr.echoes.labs.foremanapi.model.Roles;
import fr.echoes.labs.foremanapi.model.User;
import fr.echoes.labs.foremanapi.model.UserWrapper;
import fr.echoes.labs.foremanapi.model.Users;
import fr.echoes.labs.foremanclient.model.NetworkInterface;
import fr.echoes.labs.foremanclient.model.PowerAction;
import fr.echoes.labs.ksf.cc.plugins.foreman.exceptions.ForemanHostAlreadyExistException;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanHostDescriptor;

/**
* Spring Service for working with the foreman API.
*
*/

@Service
public class ForemanService implements IForemanService {


     private static final String DEFAULT_ENVIRONMENT_ID = "1";
     private static final Logger LOGGER = LoggerFactory.getLogger(ForemanService.class);

     public static final String PER_PAGE_RESULT = String.valueOf(Integer.MAX_VALUE);

     /**
     * Find the role with the given name.
     *
     * @param api
     * @param rolename
     * @return
     */
     private Role findRole(IForemanApi api, String rolename) {

          if (StringUtils.isEmpty(rolename)) { // rolename cannot be null or empty
               throw new IllegalArgumentException();
          }

          final Roles roles = api.getRoles(null, null, null, PER_PAGE_RESULT);
          for (final Role role : roles.results) {
               final String name = role.name;
               if (rolename.equals(name)) {
                    return role;
               }
          }

          return null;
     }

     private List<Filter> findFilters(IForemanApi api, String rolename) {
          Role role = findRole(api, rolename);
          if (role == null) {
               return null;
          }

          role = api.getRole(role.id);

          return role.filters;
     }

     private Filter copyFilter(IForemanApi api, Filter filter, String roleId, String searchValue) {
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

     private String[] getAllPermission(IForemanApi api, Filter filter, String resourceType) {

          final Permissions permissions = api.getPermissions(null, PER_PAGE_RESULT, resourceType, null);

          final String[] permissionIds = new String[permissions.results.size()];
          for (int i = 0; i < permissions.results.size(); i++) {
               permissionIds[i] = permissions.results.get(i).id;
          }

          return permissionIds;
     }

     private String[] getPermissionsIds(Filter filter) {
          final String[] permissionIds = new String[filter.permissions.size()];
          for (int i = 0; i < filter.permissions.size(); i++) {
               permissionIds[i] = filter.permissions.get(i).id;
          }
          return permissionIds;
     }

     /* (non-Javadoc)
	 * @see fr.echoes.labs.foremanclient.IForemanService#createProject(fr.echoes.labs.foremanapi.IForemanApi, java.lang.String, java.lang.String)
	 */
    @Override
	public void createProject(IForemanApi api, String projectName, String userId) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

          createHostGroup(api, projectName);

          final Role role = duplicateRole(api, projectName, "Default user");

          addRoleToUser(api, userId, role.id );
     }

    /* (non-Javadoc)
	 * @see fr.echoes.labs.foremanclient.IForemanService#deleteProject(fr.echoes.labs.foremanapi.IForemanApi, java.lang.String)
	 */
    @Override
	public void deleteProject(IForemanApi api, String projectName) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

    	// Delete the hosts associated to the project
    	List<Host> hosts = findHostsByProject(api, projectName);
    	for (Host host : hosts) {
    		api.deleteHost(host.id);
    	}
    	
    	// Delete the host groups
        final Hostgroups hostGroups = api.getHostGroups(null, null, null, PER_PAGE_RESULT);
        for (final HostGroup hostGroup : hostGroups.results) {
            if (hostGroup.name.equalsIgnoreCase(projectName)) {
                api.deleteHostGroups(hostGroup.id);
                break;
            }
        }
        
        // Delete the role
        final Role findRole = findRole(api, projectName);
        final Users users = api.getUsers(PER_PAGE_RESULT);
        for (final User user : users.results) {
            removeRoleToUser(api, user.id, findRole.id);
        }

        if (findRole != null) {
//            Filters filters = api.getFilters("1000");
//            for (Filter filter : filters.results) {
//                if (filter.role != null && findRole.id.equals(filter.role.id)) {
//                    api.deleteFilter(filter.id);
//                }
//            }

            api.deleteRoles(findRole.id);
        }
        
    }

     private void createHostGroup(IForemanApi api, String projectName) {
          final HostGroupWrapper hostGroupWrapper = new HostGroupWrapper();
          final HostGroup hostGroup = new HostGroup();
          hostGroup.name = projectName;
          hostGroup.subnet_id = DEFAULT_ENVIRONMENT_ID;
          hostGroup.realm_id = "";
          hostGroup.architecture_id = DEFAULT_ENVIRONMENT_ID;
          hostGroup.operatingsystem_id = DEFAULT_ENVIRONMENT_ID;
          hostGroup.ptable_id = "54";
          hostGroup.medium_id = "2";
          hostGroup.domain_id = "2";
          hostGroupWrapper.setHostGroup(hostGroup);
          api.createHostGroups(hostGroupWrapper);
     }

     private Role duplicateRole(final IForemanApi api, String projectName, String roleName) {
          final List<Filter> defaultUserFilters = findFilters(api, roleName);

          final NewRole newRole = new NewRole();
          newRole.name = projectName;

          final RoleWrapper roleWrapper = new RoleWrapper();
          roleWrapper.setRole(newRole);


          final Role role = api.createRole(roleWrapper);

          final List<String> filterIds = new ArrayList<>();
          for (final Filter filter : defaultUserFilters) {
               final Filter f = api.getFilter(filter.id);

               final Filter clonedFilter = copyFilter(api, f, role.id, projectName);
               filterIds.add(clonedFilter.id);
          }
          return role;
     }

     private void removeRoleToUser(final IForemanApi api, String userId, String roleId) {
         updateRoleTOuser(api, userId, roleId, false);
     }

     private void updateRoleTOuser(final IForemanApi api, String userId, String roleId, boolean add) {
         final User user = api.getUser(userId);

         final List<String> roleIds = new ArrayList<String>();

         for (final Role role : user.roles) {
             roleIds.add(role.id);
         }
         if (add) {
             roleIds.add(roleId);
         } else {
             roleIds.remove(roleId);
         }
         final NewUser newUser = new NewUser();

         final UserWrapper userWrapper = new UserWrapper();
         newUser.role_ids = roleIds;
         userWrapper.setUser(newUser);

         api.updateUser(user.id, userWrapper);
     }

     private void addRoleToUser(final IForemanApi api, String userId, String roleId) {
    	 updateRoleTOuser(api, userId, roleId, true);
//          final User user = api.getUser(userId);
//
//          final List<String> roleIds = new ArrayList<String>();
//
//          for (final Role role : user.roles) {
//               roleIds.add(role.id);
//          }
//
//          roleIds.add(roleId);
//
//          final NewUser newUser = new NewUser();
//
//          final UserWrapper userWrapper = new UserWrapper();
//          newUser.role_ids = roleIds;
//          userWrapper.setUser(newUser);
//
//          api.updateUser(user.id, userWrapper);
     }

     /* (non-Javadoc)
	 * @see fr.echoes.labs.foremanclient.IForemanService#createHost(fr.echoes.labs.foremanapi.IForemanApi, fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanHostDescriptor)
	 */
    @Override
	public Host createHost(IForemanApi api, ForemanHostDescriptor parameterObject) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

	   	  if (hostExists(api, parameterObject.getHostName())) {
			  throw new ForemanHostAlreadyExistException(parameterObject.getHostName());
		  }
	
		 final NewHost newHost = new NewHost();
	
		 final HostWrapper hostWrapper = new HostWrapper();
		 hostWrapper.setHost(newHost);
	
		 newHost.name = parameterObject.getHostName();
		 newHost.compute_resource_id = parameterObject.getComputeResourceId();
		 newHost.compute_profile_id = parameterObject.getComputeProfileId();
		 newHost.hostgroup_id = findHostGroupId(api, parameterObject.getHostGroupName());
		 newHost.environment_id = findEnvironmentId(api, parameterObject.getEnvironmentName());
		 newHost.operatingsystem_id = parameterObject.getOperatingSystemId();
		 newHost.architecture_id = parameterObject.getArchitectureId();
		 newHost.domain_id = parameterObject.getDomainId();
		 newHost.root_pass = parameterObject.getRootPassword();
		 newHost.interfaces_attributes.put("0", new NetworkInterface() );
	
		 Modules modules = null;
		 boolean configureModules = true;
		 try {
			 modules = getModules(api, parameterObject.getPuppetConfiguration());
			 newHost.puppetclass_ids = getModuleIds(modules);
		 } catch (final IOException e) {
			 LOGGER.error(e.getMessage(),e);
			 configureModules = false;
			 newHost.puppetclass_ids = findPuppetClassesId(api, parameterObject.getEnvironmentName());
		 }
	
		 final Host host = api.createHost(hostWrapper);
	
		 if (configureModules) {
			 configurePuppet(api, modules,host);
		 }
	
		 final PowerAction powerAction = new PowerAction();
		 powerAction.power_action = "start";
		 api.hostPower(host.id, powerAction);
	
	     return host;
     }

     private List<String> findPuppetClassesId(IForemanApi api, String environmentName) {
    	 final List<String> result = new ArrayList<String>();

    	 final String environmentId = findEnvironmentId(api, environmentName);
    	 if (environmentId == null) {
    		 return result;
    	 }
    	 final Environment environment = api.getEnvironment(environmentId);

    	 final Set<String> moduleId = new HashSet<String>();
         for (final fr.echoes.labs.foremanapi.model.PuppetClass puppetClass : environment.puppetClasses) {
        	 if (puppetClass.module_name.equals(puppetClass.name)) {
        		 moduleId.add(puppetClass.id);
        	 }
         }
         result.addAll(moduleId);
    	 return result;
	}

     private List<String> getModuleIds(Modules modules) {
  	   final Set<Entry<String, Module>> modulesEntry = modules.modules.entrySet();

  	   final List<String> ids = new ArrayList<>(modulesEntry.size());
  	   for (final Entry<String, Module> entry : modulesEntry) {
  		   final Module module = entry .getValue();
  		   if (module.id == null) {
  			   if (module.puppetClasses != null) {
  				   for (final PuppetClass ppc : module.puppetClasses.values()) {
  					 ids.add(ppc.id);
  				   }
  			   }
  		   } else {
  			   ids.add(module.id);
  		   }
  	   }
  	   return ids;
     }

      private Modules getModules(IForemanApi api, String puppetConfiguration) throws IOException {
          final ObjectMapper mapper = new ObjectMapper();
          final Modules modules = mapper .readValue(puppetConfiguration, Modules.class);
          return modules;
      }

  	private void configurePuppet(IForemanApi api, Modules modules, Host host) {

  		LOGGER.info("[foreman] Configure puppet smart clasees parameters");

  		for (final Entry<String, Module> entry : modules.modules.entrySet()) {
  			final Module module = entry .getValue();

  			for (final Entry<String, PuppetClass> puppetClassEntry : module.puppetClasses .entrySet()) {
  				final PuppetClass puppetClass = puppetClassEntry.getValue();
  				for (final Entry<String, Parameter> puppetClassParametersEntry : puppetClass .parameters .entrySet()) {
  					final Parameter parameter = puppetClassParametersEntry .getValue();
  					final Boolean override = parameter.isOverride();
  					if (override) {

  						final String parameterId = parameter.getId();
  						updateValue(api, parameterId, host.name, parameter.getValue(), Boolean.FALSE);
  					}
  				}
  			}
  		}

       }
  	
     private String findEnvironmentId(IForemanApi api, String environmentName) {
          final Environments environments = api.getEnvironments(null, null, null, PER_PAGE_RESULT);
          for (final Environment hostGroup : environments.results) {
               if (hostGroup.name.equals(environmentName)) {
                    return hostGroup.id;
               }
          }
          return DEFAULT_ENVIRONMENT_ID;
     }


     private String findHostGroupId(IForemanApi api, String hostGroupName) {
          final Hostgroups hostGroups = api.getHostGroups(null, null, null, PER_PAGE_RESULT);
          for (final HostGroup hostGroup : hostGroups.results) {
               if (hostGroup.name.equals(hostGroupName)) {
                    return hostGroup.id;
               }
          }
          return null;
     }

     /* (non-Javadoc)
	 * @see fr.echoes.labs.foremanclient.IForemanService#importPuppetClasses(fr.echoes.labs.foremanapi.IForemanApi, java.lang.String)
	 */
    @Override
	public void importPuppetClasses(IForemanApi api, String smartProxyId) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
          api.importPuppetClasses(smartProxyId, "{}"); // Doesn't work without a request body as second parameter ("{}")
     }

     /* (non-Javadoc)
	 * @see fr.echoes.labs.foremanclient.IForemanService#getModulesPuppetClassParameters(fr.echoes.labs.foremanapi.IForemanApi, java.lang.String, boolean)
	 */
    @Override
	public String getModulesPuppetClassParameters(IForemanApi api , String environmentName, boolean allParameters) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, JsonGenerationException, JsonMappingException, IOException {
    	 final String environmentId = findEnvironmentId(api, environmentName);

    	 final Environment environment = api.getEnvironment(environmentId);

    	 final Modules modules = new Modules();

    	 final Set<String> moduleIds = new HashSet<String>();

    	 final Map<String, List<fr.echoes.labs.foremanapi.model.PuppetClass>> specialCase = new HashMap<>();
    	 for (final fr.echoes.labs.foremanapi.model.PuppetClass puppetClass : environment.puppetClasses) {

    		 List<fr.echoes.labs.foremanapi.model.PuppetClass> list = specialCase.get(puppetClass.module_name);
    		 if (list == null) {
    			 list = new ArrayList<>();
    			 specialCase.put(puppetClass.module_name, list);
    		 }
    		 list.add(puppetClass);
    	 }

    	 for (final fr.echoes.labs.foremanapi.model.PuppetClass puppetClass : environment.puppetClasses) {
    		 if (puppetClass.module_name.equals(puppetClass.name)) {
    			 final Module module = modules.getOrCreateModule(puppetClass.module_name);
    			 module.id = puppetClass.id;
    			 final PuppetClass pc = module.getOrCreatePuppetClass(puppetClass.name);
    			 pc.setId(puppetClass.id);
    			 moduleIds.add(puppetClass.id);
    			 if (specialCase.containsKey(puppetClass.module_name)) {
    				 specialCase.remove(puppetClass.module_name);
    			 }
    		 }
    	 }


    	 for (final Map.Entry<String, List<fr.echoes.labs.foremanapi.model.PuppetClass>> entry : specialCase.entrySet()) {
    		 final String moduleName = entry.getKey();
    		 final List<fr.echoes.labs.foremanapi.model.PuppetClass> values = entry.getValue();
    		 final Module module = modules.getOrCreateModule(moduleName);
    		 for (final fr.echoes.labs.foremanapi.model.PuppetClass value : values) {
    			 final PuppetClass ppclass = module.getOrCreatePuppetClass(value.name);
    			 ppclass.setId(value.id);
    		 }

    	 }


    	 for (final Module module : modules.modules.values()) {
    		 for (final PuppetClass puppetClass : module.puppetClasses.values()) {
    			 final PuppetClassParameters puppetClassParameters = api.getPuppetClassParameters(puppetClass.id, null, null, null, PER_PAGE_RESULT);
    			 for (final PuppetClassParameter puppetClassParameter : puppetClassParameters.results) {
    				 if (allParameters || moduleIds.contains(puppetClassParameter.id)) {
    					 final Parameter parameter = puppetClass.getOrCreateParameter(puppetClassParameter.parameter);
    					 parameter.setId(puppetClassParameter.id);
    					 parameter.setValue(puppetClassParameter.default_value);
    					// parameter.setOverride(puppetClassParameter.override);
    					 parameter.setOverride(false);
    				 }
    			 }
    		 }
    	 }



          final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
          return ow.writeValueAsString(modules);
     }

     /* (non-Javadoc)
	 * @see fr.echoes.labs.foremanclient.IForemanService#hostGroupExists(fr.echoes.labs.foremanapi.IForemanApi, java.lang.String)
	 */
    @Override
	public boolean hostGroupExists(IForemanApi api , String hostGroupName) {
    	 if (StringUtils.isEmpty(hostGroupName)) {
    		 throw new IllegalArgumentException("hostName cannot be null or empty");
    	 }

    	 try {
    		 final Hostgroups hostGroups = api.getHostGroups(null, null, null, PER_PAGE_RESULT);
    		 for (final HostGroup hostGroup : hostGroups.results) {
    			 if (hostGroup.name.equalsIgnoreCase(hostGroupName)) {
    				 return true;
    			 }
    		 }
    	 } catch (final Exception e) {
    		 LOGGER.error("Failed to check if the hostGroup exists", e);
    	 }
    	 return false;
     }


     /* (non-Javadoc)
	 * @see fr.echoes.labs.foremanclient.IForemanService#hostExists(fr.echoes.labs.foremanapi.IForemanApi, java.lang.String)
	 */
    @Override
	public boolean hostExists(IForemanApi api, String hostName) {
    	 if (StringUtils.isEmpty(hostName)) {
    		 throw new IllegalArgumentException("hostName cannot be null or empty");
    	 }
    	 try {
    		 final Hosts hosts = api.getHosts(null, null, null, PER_PAGE_RESULT);
    		 for (final Host host : hosts.results) {
    			 if (host.name.equalsIgnoreCase(hostName)) {
    				 return true;
    			 }
    		 }
    	 } catch (final Exception e) {
    		 LOGGER.error("Failed to check if the host exists", e);
    	 }
    	 return false;
     }

     /* (non-Javadoc)
	 * @see fr.echoes.labs.foremanclient.IForemanService#updateValue(fr.echoes.labs.foremanapi.IForemanApi, java.lang.String, java.lang.String, java.lang.Object, java.lang.Boolean)
	 */
    @Override
	public void updateValue(IForemanApi api, String smartClassParamId, String hostName, Object value, Boolean usePuppetDefault) {

    	 try {
			final OverrideValueWrapper overrideValueWrapper = new OverrideValueWrapper();
			overrideValueWrapper.override_value = new HashMap<String, Object>();
			overrideValueWrapper.override_value.put("match", "fqdn=" + hostName);
			overrideValueWrapper.override_value.put("value", value);
			overrideValueWrapper.override_value.put("use_puppet_default", usePuppetDefault.toString());
			api.createOverrideValues(smartClassParamId, overrideValueWrapper);
    	 } catch (final Exception e) {
    		 LOGGER.error("Failed to configure smart class parameter " + smartClassParamId, e);
    	 }
     }

	@Override
	public List<Host> findHostsByProject(IForemanApi api, String projectName) {
		
		Hosts hosts = api.getHostsByHostGroup(projectName, null, null, null, PER_PAGE_RESULT);
		
		return hosts != null ? hosts.results : null;
		
	}


}
