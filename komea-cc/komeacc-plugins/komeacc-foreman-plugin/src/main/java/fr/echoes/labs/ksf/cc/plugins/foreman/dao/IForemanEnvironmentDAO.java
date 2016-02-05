package fr.echoes.labs.ksf.cc.plugins.foreman.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanEnvironnment;

public interface IForemanEnvironmentDAO extends MongoRepository<ForemanEnvironnment, String> {
	//
}
