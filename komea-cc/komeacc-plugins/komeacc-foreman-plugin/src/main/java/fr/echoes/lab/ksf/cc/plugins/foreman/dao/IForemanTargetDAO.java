package fr.echoes.lab.ksf.cc.plugins.foreman.dao;

import org.springframework.data.repository.CrudRepository;

import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanTarget;

public interface IForemanTargetDAO extends CrudRepository<ForemanTarget, String> {
	//
}
