package fr.echoes.lab.ksf.cc.plugins.foreman.dao;

import org.springframework.data.repository.CrudRepository;

import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanEnvironnment;

public interface IForemanEnvironmentDAO extends CrudRepository<ForemanEnvironnment, String> {
	//
}
