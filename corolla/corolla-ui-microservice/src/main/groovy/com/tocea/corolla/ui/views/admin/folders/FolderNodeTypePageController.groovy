package com.tocea.corolla.ui.views.admin.folders

import groovy.util.logging.Slf4j;

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.trees.commands.CreateFolderNodeTypeCommand
import com.tocea.corolla.trees.commands.EditFolderNodeTypeCommand
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO;
import com.tocea.corolla.trees.domain.FolderNodeType
import com.tocea.corolla.users.domain.Permission;

@Controller
@Slf4j
class FolderNodeTypePageController {

	private static final String INDEX_PAGE = "admin/folders";
	private static final String FORM_PAGE = "admin/folders_edit";
	
	@Autowired
	private IFolderNodeTypeDAO folderNodeTypeDAO;
	
	@Autowired
	private Gate gate
	
	@ModelAttribute("menu")
	public String setMenu() {
		return "admin"
	}
	
	@Secured(Permission.ADMIN)
	@RequestMapping('/ui/admin/folder-node-types') 
	public ModelAndView getListPage() {
		
		def model = new ModelAndView(INDEX_PAGE)	
		model.addObject "types", folderNodeTypeDAO.findAll()
		
		return model	
	}
	
	@Secured(Permission.ADMIN)
	@RequestMapping('/ui/admin/folder-node-types/add') 
	public ModelAndView getAddPage() {
		
		def model = new ModelAndView(FORM_PAGE)	
		model.addObject "type", new FolderNodeType()
		
		return model	
	}
	
	@Secured(Permission.ADMIN)
	@RequestMapping(value = '/ui/admin/folder-node-types/add', method = RequestMethod.POST) 
	public ModelAndView addFolder(@Valid @ModelAttribute("type") FolderNodeType type, BindingResult _result) {
		
		if (_result.hasErrors()) {			
			def model = new ModelAndView(FORM_PAGE)	
			model.addObject "type", type
			return model
		}
		
		type = _result.model.get('type')
				
		gate.dispatch(new CreateFolderNodeTypeCommand(type))
		
		return new ModelAndView("redirect:/ui/admin/folder-node-types")	
	}
	
	@Secured(Permission.ADMIN)
	@RequestMapping('/ui/admin/folder-node-types/edit/{typeID}') 
	public ModelAndView getEditPage(@PathVariable String typeID) {
		
		def type = folderNodeTypeDAO.findOne(typeID)
				
		if (type == null) {
			return new ModelAndView("redirect:/ui/admin/folder-node-types/add")
		}
		
		def model = new ModelAndView(FORM_PAGE)	
		model.addObject "type", type
		
		return model	
	}
	
	@Secured(Permission.ADMIN)
	@RequestMapping(value = '/ui/admin/folder-node-types/edit/{typeID}', method = RequestMethod.POST) 
	public ModelAndView editFolderType(@Valid @ModelAttribute("type") FolderNodeType type, BindingResult _result) {
		
		if (_result.hasErrors()) {			
			def model = new ModelAndView(FORM_PAGE)	
			model.addObject "type", type
			return model
		}
		
		type = _result.model.get('type')

		gate.dispatch(new EditFolderNodeTypeCommand(type))
		
		return new ModelAndView("redirect:/ui/admin/folder-node-types")	
	}
	
}
