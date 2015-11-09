package fr.echoes.lab.ksf.cc.sf.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.echoes.lab.ksf.cc.sf.model.SFApplication;

public interface ISFApplicationDAO extends MongoRepository<SFApplication, String> {

}
