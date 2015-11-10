package fr.echoes.lab.ksf.cc.sf.commands.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.lab.ksf.cc.sf.commands.CreateProductionLineCommand;
import fr.echoes.lab.ksf.cc.sf.commands.CreateProjectAndProductionLineCommand;
import fr.echoes.lab.ksf.cc.sf.dao.ISFApplicationDAO;
import fr.echoes.lab.ksf.cc.sf.dao.ISFApplicationTypeDAO;
import fr.echoes.lab.ksf.cc.sf.domain.ProductionLine;
import fr.echoes.lab.ksf.cc.sf.domain.SFApplication;
import fr.echoes.lab.ksf.cc.sf.domain.SFApplicationType;
import fr.echoes.lab.ksf.cc.sf.dto.SFProjectDTO;

public class CreateProjectAndProductionLineCommandHandler implements ICommandHandler<CreateProjectAndProductionLineCommand, Project> {

	@Autowired
	private Gate gate;
	
	@Autowired
	private ISFApplicationDAO applicationDAO;
	
	@Autowired
	private ISFApplicationTypeDAO applicationTypeDAO;
		
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateProjectAndProductionLineCommandHandler.class);
	
	@Override
	public Project handle(@Valid CreateProjectAndProductionLineCommand command) {
		
		SFProjectDTO projectDTO = command.getProjectDTO();
		
		Project project = new Project();
		project.setKey(projectDTO.getKey());
		project.setName(projectDTO.getName());
		
		gate.dispatch(new CreateProjectCommand(project));
		
		ProductionLine productionLine = new ProductionLine();
		productionLine.setProject(project);
		productionLine.setApplications(new ArrayList<SFApplication>());
		
		for(String app : projectDTO.getApplications()) {
			SFApplicationType type = applicationTypeDAO.findByName(app);
			if (type != null) {
				List<SFApplication> applications = applicationDAO.findByType(type);
				if (!applications.isEmpty()) {
					productionLine.getApplications().add(applications.get(0));
				}else{
					LOGGER.error("No application found for type {}", type.getName());
				}
			}else{
				LOGGER.error("Type {} does not exist", app);
			}
		}		
		
		gate.dispatch(new CreateProductionLineCommand(productionLine));
		
		return project;
	}

}
