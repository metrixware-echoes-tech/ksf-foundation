package fr.echoes.labs.ksf.cc.sf.commands;

import javax.validation.constraints.NotNull;

import com.tocea.corolla.cqrs.annotations.CommandOptions;

import fr.echoes.lab.ksf.cc.sf.domain.ProductionLine;

@CommandOptions
public class CreateProductionLineCommand {

	@NotNull
	private ProductionLine productionLine;
	
	public CreateProductionLineCommand() {
		super();
	}
	
	public CreateProductionLineCommand(ProductionLine productionLine) {
		super();
		this.productionLine = productionLine;
	}

	public ProductionLine getProductionLine() {
		return productionLine;
	}
	

	public void setProductionLine(ProductionLine productionLine) {
		this.productionLine = productionLine;
	}
	
}
