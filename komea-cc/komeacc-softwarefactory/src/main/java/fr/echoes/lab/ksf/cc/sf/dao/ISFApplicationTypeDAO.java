package fr.echoes.lab.ksf.cc.sf.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.echoes.lab.ksf.cc.sf.model.SFApplicationType;

public interface ISFApplicationTypeDAO extends MongoRepository<SFApplicationType, String> {

}
