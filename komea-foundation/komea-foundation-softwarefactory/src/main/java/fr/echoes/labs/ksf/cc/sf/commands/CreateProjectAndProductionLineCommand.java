package fr.echoes.labs.ksf.cc.sf.commands;

import javax.validation.constraints.NotNull;

import com.tocea.corolla.cqrs.annotations.CommandOptions;

import fr.echoes.labs.ksf.cc.sf.dto.SFProjectDTO;

@CommandOptions
public class CreateProjectAndProductionLineCommand {

	@NotNull
	private SFProjectDTO projectDTO;
	
	public CreateProjectAndProductionLineCommand() {
		super();
	}
	
	public CreateProjectAndProductionLineCommand(SFProjectDTO projectDTO) {
		super();
		this.projectDTO = projectDTO;
	}

	public SFProjectDTO getProjectDTO() {
		return projectDTO;
	}
	
	public void setProjectDTO(SFProjectDTO projectDTO) {
		this.projectDTO = projectDTO;
	}
	
}
