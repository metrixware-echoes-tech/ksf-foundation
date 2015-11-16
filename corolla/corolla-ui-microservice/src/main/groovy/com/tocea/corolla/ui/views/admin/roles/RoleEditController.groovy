package com.tocea.corolla.ui.views.admin.roles

import groovy.util.logging.Slf4j

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.users.commands.CreateRoleCommand
import com.tocea.corolla.users.commands.EditRoleCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.domain.Permission
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.dto.RoleDTO

@Secured([
	Permission.ADMIN,
	Permission.ADMIN_ROLES
])
@Slf4j
@Controller
@RequestMapping("/ui/admin/roles")
public class RoleEditController {
	
	static final String ADMIN_ROLES_EDIT = "admin/roles_edit"
	
	@Autowired
	private IRoleDAO roleDAO
	
	@Autowired
	private Gate gate
	
	@ModelAttribute("menu")
	public String setMenu() {
		return "admin"
	}
	
	@ModelAttribute("permissions")
	public List<String> permissions() {
		return Permission.list()
	}
	
	@RequestMapping("/add")
	public ModelAndView getAddPage(@ModelAttribute RoleDTO role) {
		
		def ModelAndView model = new ModelAndView(ADMIN_ROLES_EDIT)
		model.addObject "role", role
		
		return model
		
	}
	
	@RequestMapping(value="/add", method = RequestMethod.POST)
	public ModelAndView saveNewRole(@Valid @ModelAttribute("role") RoleDTO _role, BindingResult _result) {
		
		if (_result.hasErrors()) {
			def ModelAndView model2 = new ModelAndView(ADMIN_ROLES_EDIT)
			model2.addObject "role", _role
			return model2
		}
		log.warn("role found: {}", _role)
		CreateRoleCommand command = new CreateRoleCommand(_role)
		gate.dispatchAsync(command)
		
		return new ModelAndView("redirect:/ui/admin/roles")
		
	}
	
	@RequestMapping("/edit/{role_id}")
	public ModelAndView getEditPage(@PathVariable String role_id) {
		
		Role role = roleDAO.findOne(role_id)
		
		if (role == null) {
			log.error("Role not found {}", role_id)
			return new ModelAndView("redirect:/ui/admin/roles/add")
		}
		
		def model = new ModelAndView(ADMIN_ROLES_EDIT)
		model.addObject "role", new RoleDTO(role)
		
		return model
		
	}
	
	@RequestMapping(value="/edit/{role_id}", method = RequestMethod.POST)
	public ModelAndView modifyRole(@PathVariable String role_id, @Valid @ModelAttribute("role") RoleDTO _role, BindingResult _result) {
		
		if (_result.hasErrors()) {
			def ModelAndView model2 = new ModelAndView(ADMIN_ROLES_EDIT)
			model2.addObject "role", _role
			return model2
		}
		
		Role role = roleDAO.findOne(_role.getId())
		log.warn("role found: {}", _role)
		
		EditRoleCommand command = new EditRoleCommand(role, _role)
		gate.dispatchAsync(command)
		
		return new ModelAndView("redirect:/ui/admin/roles")
		
	}
	
}