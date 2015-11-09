/**
 *
 */
package fr.echoes.lab.ksf.cc

import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.commands.CreateProjectCommand
import com.tocea.corolla.products.commands.EditProjectCommand
import com.tocea.corolla.products.dao.IProjectDAO
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.users.commands.CreateRoleCommand
import com.tocea.corolla.users.commands.CreateUserCommand
import com.tocea.corolla.users.commands.CreateUserGroupCommand
import com.tocea.corolla.users.commands.EditRoleCommand
import com.tocea.corolla.users.commands.EditUserCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.dao.IUserDAO
import com.tocea.corolla.users.dao.IUserGroupDAO
import com.tocea.corolla.users.domain.Permission
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.domain.User
import com.tocea.corolla.users.domain.UserGroup

/**
 * @author sleroy
 *
 */
@Profile("demo")
@Component
@Slf4j
public class DemoDataBean {
	
	static final String ADMIN_USERS = ADMIN_USERS
	
	@Autowired
	def IRoleDAO					roleDAO
	
	@Autowired
	def IUserDAO					userDAO
	
	@Autowired
	def IUserGroupDAO				groupDAO
	
	@Autowired
	def IProjectDAO					projectDAO
	
	@Autowired
	def PasswordEncoder				passwordEncoder
	
	@Autowired
	def Gate						gate
	
	@SuppressWarnings("nls")
	@PostConstruct
	public void init() throws MalformedURLException {
		
		/*
		 * Admin role
		 */
		final Role roleAdmin = this.newRole("Administrator", "Administrator role", Permission.list())
		roleAdmin.roleProtected = true
		this.gate.dispatch new EditRoleCommand(roleAdmin)
		
		/*
		 * Roles
		 */
		final Role roleGuest = this.newRole("Guest", "Guest", [], true)
		final Role roleTester = this.newRole("Tester", "Tester", [Permission.REST])
		final Role roleTestManager = this.newRole("Test manager", "Test Manager", [Permission.REST])
		final Role roleApplicationManager = this.newRole(	"Application manager",
				"Application manager", [Permission.REST])
		
		/*
		 * Users
		 */
		def jsnow = this.newUser(	"John", "Snow", "john.snow@email.com", "jsnow",
				"password", roleAdmin)
		def scarreau = this.newUser(	"Sébastien", "Carreau", "sebastien.carreau@tocea.com", "scarreau",
				"scarreau", roleAdmin)
		this.newUser(	"Jack", "Pirate", "jack.pirate@email.com", "jpirate",
				"password", roleGuest)
		this.newUser(	"Ichigo", "Kurosaki", "ichigo.kurosaki@email.com",
				"ikurosaki", "password", roleTester)
		this.newUser(	"James", "Bond", "james.bond@mi6.com", "jbond", "007",
				roleTestManager)
		this.newUser(	"Gandalf", "LeGris", "gandalf.legris@lotr.com",
				"gandalf",
				"saroumaneisg..", roleApplicationManager)
		this.newUser(	"Saroumane", "LeBlanch", "saroumane.leblanc@lotr.com",
				"saroumane",
				"fuckSauron..", roleAdmin)
		
		/*
		 * User Groups
		 */
		def developers = this.newGroup("developers", [jsnow, scarreau])
		
	}
	
	/**
	 * Creates a new role.
	 *
	 * @param _roleName
	 *            the role name
	 * @param _roleNote
	 *            the role note
	 * @return the new role
	 */
	Role newRole(final String _roleName, final String _roleNote) {
		final Role role = new Role()
		role.with {
			name = _roleName
			note = _roleNote
			permissions = ""
			roleProtected = false
		}
		this.gate.dispatch new CreateRoleCommand(role)
		log.info("new role created [_id:"+role.getId()+"]")
		return role
	}
	
	/**
	 * Creates a new role.
	 *
	 * @param _roleName
	 *            the role name
	 * @param _roleNote
	 *            the role note
	 * @return the new role
	 */
	public Role newRole(final String _roleName, final String _roleNote, List<String> _roles, boolean _defaultRole=false) {
		final Role role = new Role()
		role.with {
			name = _roleName
			note = _roleNote
			permissions = _roles.join(", ")
			defaultRole = _defaultRole
			roleProtected = false
		}
		this.gate.dispatch new CreateRoleCommand(role)
		log.info("new role created [_id:"+role.getId()+"]")
		return role
	}
	
	public User newUser(final String _firstName, final String _lastName,
			final String _email, final String _login,
			final String _password, final Role _rolePO) {
		final User user = new User()
		user.with {
			firstName = _firstName
			lastName = _lastName
			email =_email
			login =_login
			password = _password
			roleId = _rolePO.id
		}
		this.gate.dispatch new CreateUserCommand(user)
		user.active = true
		this.gate.dispatch new EditUserCommand(user)
		log.info("new user created [_id:"+user.getId()+"]")
		return user
	}
	
	public UserGroup newGroup(final String name, List<User> users) {
		
		def group = new UserGroup()
		group.setName(name)
		group.setUserIds(users.collect { it.login })
		
		this.gate.dispatch new CreateUserGroupCommand(group)
		log.info("new user group created [_id: {}", group.getId())
		
		return group
		
	}
	
	public Project saveProject(project) {
		
		this.gate.dispatch new CreateProjectCommand(project)
		
		return project
		
	}
	
	public Project editProject(project) {
		
		this.gate.dispatch new EditProjectCommand(project)
		
		return project
	}
	
	
	
	@PreDestroy
	public void destroy() {
		
		projectDAO.deleteAll()
		userDAO.deleteAll()
		roleDAO.deleteAll()
		groupDAO.deleteAll()
		
	}
	
}