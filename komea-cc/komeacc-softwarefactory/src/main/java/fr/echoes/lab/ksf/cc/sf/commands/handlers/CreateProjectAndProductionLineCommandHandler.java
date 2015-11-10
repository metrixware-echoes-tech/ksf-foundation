package fr.echoes.lab.ksf.cc.sf.commands.handlers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.utils.domain.CorollaDomainException;

import fr.echoes.lab.ksf.cc.sf.commands.CreateProductionLineCommand;
import fr.echoes.lab.ksf.cc.sf.commands.CreateProjectAndProductionLineCommand;
import fr.echoes.lab.ksf.cc.sf.dao.ISFApplicationDAO;
import fr.echoes.lab.ksf.cc.sf.dao.ISFApplicationTypeDAO;
import fr.echoes.lab.ksf.cc.sf.domain.ProductionLine;
import fr.echoes.lab.ksf.cc.sf.domain.SFApplication;
import fr.echoes.lab.ksf.cc.sf.domain.SFApplicationType;
import fr.echoes.lab.ksf.cc.sf.dto.SFProjectDTO;

@CommandHandler
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
		
		try {
			gate.dispatch(new CreateProjectCommand(project));
		}catch(CommandExecutionException ex){
			LOGGER.error("[CreateProjectAndProductionLineCommand] Cannot create project. Aborting production line creation.");
			throw (CorollaDomainException) ex.getCause();
		}
				
		ProductionLine productionLine = new ProductionLine();
		productionLine.setProject(project);
		productionLine.setApplications(retrieveApplications(projectDTO.getApplications()));		
		
		try {
			gate.dispatch(new CreateProductionLineCommand(productionLine));
		}catch(CommandExecutionException ex){
			LOGGER.error("[CreateProjectAndProductionLineCommand] Cannot create production line.");
			throw (CorollaDomainException) ex.getCause();
		}
		
		return project;
	}
	
	private List<SFApplication> retrieveApplications(List<String> appNames) {
		
		List<SFApplication> applications = Lists.newArrayList();
		
		if (appNames != null) {
		
			for(String app : appNames) {
				SFApplicationType type = applicationTypeDAO.findByName(app);
				if (type != null) {
					List<SFApplication> applicationsForType = applicationDAO.findByType(type);
					if (!applicationsForType.isEmpty()) {
						applications.add(applicationsForType.get(0));
					}else{
						LOGGER.error("No application found for type {}", type.getName());
					}
				}else{
					LOGGER.error("Type {} does not exist", app);
				}
			}		
		
		}
		
		return applications;
	}

}
