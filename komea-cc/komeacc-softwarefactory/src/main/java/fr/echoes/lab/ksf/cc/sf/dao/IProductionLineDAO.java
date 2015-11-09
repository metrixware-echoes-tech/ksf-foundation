package fr.echoes.lab.ksf.cc.sf.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.github.fakemongo.impl.aggregation.Project;

import fr.echoes.lab.ksf.cc.sf.model.ProductionLine;

public interface IProductionLineDAO extends MongoRepository<ProductionLine, String> {

	ProductionLine findByProject(Project project);
	
}
