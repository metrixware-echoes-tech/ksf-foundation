package fr.echoes.lab.ksf.cc.sf.events;

import fr.echoes.lab.ksf.cc.sf.domain.ProductionLine;

public class EventProductionLineCreated {

	private ProductionLine productionLine;

	public EventProductionLineCreated() {
		super();
	}
	
	public EventProductionLineCreated(ProductionLine productionLine) {
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
