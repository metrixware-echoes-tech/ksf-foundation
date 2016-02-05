package com.tocea.corolla.tests.utils;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.cc.sf.commands.CreateProjectAndProductionLineCommand;
import fr.echoes.labs.ksf.cc.sf.dto.SFProjectDTO;
import fr.echoes.labs.ksf.cc.ui.AbstractSpringTest;

public class CreateProjectTest extends AbstractSpringTest{

	@Autowired
	private Gate gate;
	
	@Autowired
	private IProjectDAO projectDAO;
	
	@Test
	public void createProjectTest() {
		Project project = new Project();
		project.setKey("blbl");
		project.setName("blbl");
		projectDAO.save(project);
		Validate.notEmpty((project.getId()));
	}
	
	@Test
	public void createProjectFromCommandTest() {
		Project project = new Project();
		project.setKey("blbl");
		project.setName("blbl");
		gate.dispatch(new CreateProjectCommand(project));
		Validate.notEmpty(project.getId());
	}
	
	@Test
	public void createProjectAndProductionLineCommandTest() {
		SFProjectDTO projectDTO = new SFProjectDTO();
		projectDTO.setKey("test");
		projectDTO.setName("test");
		projectDTO.setApplications(null);
		Project project = gate.dispatch(new CreateProjectAndProductionLineCommand(projectDTO));
		assertNotNull(project);
		Validate.notEmpty(project.getId());
	}
	
}
