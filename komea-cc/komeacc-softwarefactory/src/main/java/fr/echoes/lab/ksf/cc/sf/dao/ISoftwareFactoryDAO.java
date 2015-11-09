package fr.echoes.lab.ksf.cc.sf.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.echoes.lab.ksf.cc.sf.model.SoftwareFactory;

public interface ISoftwareFactoryDAO extends MongoRepository<SoftwareFactory, String> {

}
