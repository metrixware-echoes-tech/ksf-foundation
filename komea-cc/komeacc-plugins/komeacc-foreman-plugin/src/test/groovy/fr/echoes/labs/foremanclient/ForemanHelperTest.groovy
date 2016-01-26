package fr.echoes.labs.foremanclient
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanapi.model.Filter
import fr.echoes.lab.foremanapi.model.Host
import fr.echoes.lab.foremanapi.model.HostGroup;
import fr.echoes.lab.foremanapi.model.Hostgroups;
import fr.echoes.lab.foremanapi.model.Hosts;
import fr.echoes.lab.foremanapi.model.Permission;
import fr.echoes.lab.foremanapi.model.Role
import fr.echoes.lab.foremanapi.model.Roles
import fr.echoes.lab.foremanapi.model.User;
import fr.echoes.lab.foremanclient.ForemanClient;
import fr.echoes.lab.foremanclient.ForemanHelper;
import spock.lang.Specification;


class ForemanHelperTest extends Specification {
	
	def "it should create a host"() {

		given:
			IForemanApi foremanApi = Mock()
		and:
			Role role = new Role()
			role.builtin = "1"
			role.name = "Default user"
			role.id = "1"
	
			Filter filter = new Filter()
			filter.search = null
			filter.resource_type = "Architecture";
			filter.unlimited = true
			filter.id = "1"
			
			Permission permission = new Permission()
			permission.name = "view_architectures"
			permission.id = "1"
			
			filter.permissions = new ArrayList<Permission>()
			filter.permissions.add(permission)
	
			role.filters = new ArrayList<Filter>()
			role.filters.add(filter)
			
			User user = new User()
			user.id = "1"
			user.roles = new ArrayList<Role>()
	
			def roles = new Roles();
			roles.results = new ArrayList<Role>();
			roles.results.add(role)
			
		and:
			foremanApi.getRoles(_, _, _, _) >> roles
			foremanApi.getRole(_) >> role
			foremanApi.getFilter(_) >> filter
			foremanApi.createRole(_) >> role
			foremanApi.createFilter(_) >> filter
			foremanApi.getUser(_) >> user
		when:
			ForemanHelper.createProject(foremanApi, "testProject", "groovy")
			
		then:
			notThrown Exception

	}
	
	def "it should return that the host exists "() {
		given:
			IForemanApi foremanApi = Mock()
		and:
			Hosts hosts = new Hosts()
			Host host = new Host()
			host.name = "existingHost"
			hosts.results = new ArrayList<Host>()
			hosts.results.add(host)
			foremanApi.getHosts(_, _, _,_) >> hosts
		when:
			def exist = ForemanHelper.hostExists(foremanApi, "existingHost")
		then:
			exist
	}
	
	def "it should return that the host doesn't exist"() {
		given:
			IForemanApi foremanApi = Mock()
		and:
			Hosts hosts = new Hosts()
			Host host = new Host()
			host.name = "existingHost"
			hosts.results = new ArrayList<Host>()
			hosts.results.add(host)
			foremanApi.getHosts(_, _, _,_) >> hosts
		when:
			def exist = ForemanHelper.hostExists(foremanApi, "notExistingHost")
		then:
			!exist
	}		

	def "it should return that the host group exists "() {
		given:
			IForemanApi foremanApi = Mock()
		and:
			Hostgroups hostgroups = new Hostgroups()
			HostGroup hostGroup = new HostGroup()
			hostGroup.name = "existingHostGroup"
			hostgroups.results = new ArrayList<HostGroup>()
			hostgroups.results.add(hostGroup)
			foremanApi.getHostGroups(_, _, _,_) >> hostgroups
		when:
			def exist = ForemanHelper.hostGroupExists(foremanApi, "existingHostGroup")
		then:
			exist == true
	}

	def "it should return that the host group doesn't exist"() {
		given:
			IForemanApi foremanApi = Mock()
		and:
			Hostgroups hostgroups = new Hostgroups()
			HostGroup hostGroup = new HostGroup()
			hostGroup.name = "existingHostGroup"
			hostgroups.results = new ArrayList<HostGroup>()
			hostgroups.results.add(hostGroup)
			foremanApi.getHostGroups(_, _, _,_) >> hostgroups
		when:
			def exist = ForemanHelper.hostGroupExists(foremanApi, "notExistingHostGroup")
		then:
			exist == false
	}
			
}
