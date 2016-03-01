package fr.echoes.labs.foremanclient
import spock.lang.Specification
import fr.echoes.labs.foremanapi.IForemanApi
import fr.echoes.labs.foremanapi.model.Filter
import fr.echoes.labs.foremanapi.model.Host
import fr.echoes.labs.foremanapi.model.HostGroup
import fr.echoes.labs.foremanapi.model.Hostgroups
import fr.echoes.labs.foremanapi.model.Hosts
import fr.echoes.labs.foremanapi.model.Permission
import fr.echoes.labs.foremanapi.model.Role
import fr.echoes.labs.foremanapi.model.Roles
import fr.echoes.labs.foremanapi.model.User


class ForemanServiceTest extends Specification {

	def IForemanService foremanService
	def IForemanApi foremanApi

	def setup() {
		foremanService = new ForemanService()
		foremanApi = Mock()
	}

	def "it should create a host"() {

		given:
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
			foremanService.createProject(foremanApi, "testProject", "groovy")

		then:
			notThrown Exception

	}

	def "it should return that the host exists "() {
		given:
			Hosts hosts = new Hosts()
			Host host = new Host()
			host.name = "existingHost"
			hosts.results = new ArrayList<Host>()
			hosts.results.add(host)
			foremanApi.getHosts(_, _, _,_) >> hosts
		when:
			def exist = foremanService.hostExists(foremanApi, "existingHost")
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
			def exist = foremanService.hostExists(foremanApi, "notExistingHost")
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
			def exist = foremanService.hostGroupExists(foremanApi, "existingHostGroup")
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
			def exist = foremanService.hostGroupExists(foremanApi, "notExistingHostGroup")
		then:
			exist == false
	}

}