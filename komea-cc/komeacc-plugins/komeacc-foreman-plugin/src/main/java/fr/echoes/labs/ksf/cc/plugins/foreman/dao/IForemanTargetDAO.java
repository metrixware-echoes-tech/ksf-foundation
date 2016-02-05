package fr.echoes.labs.ksf.cc.plugins.foreman.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanTarget;

public interface IForemanTargetDAO extends CrudRepository<ForemanTarget, String> {
	
	List<ForemanTarget> findByProject(Project project);
	
}
