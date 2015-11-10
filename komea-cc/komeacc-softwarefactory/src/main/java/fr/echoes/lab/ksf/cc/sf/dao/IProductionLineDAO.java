package fr.echoes.lab.ksf.cc.sf.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tocea.corolla.products.domain.Project;

import fr.echoes.lab.ksf.cc.sf.domain.ProductionLine;

public interface IProductionLineDAO extends MongoRepository<ProductionLine, String> {

	ProductionLine findByProject(Project project);
	
}
