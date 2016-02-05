package fr.echoes.labs.ksf.cc.sf.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.echoes.lab.ksf.cc.sf.domain.SoftwareFactory;

public interface ISoftwareFactoryDAO extends MongoRepository<SoftwareFactory, String> {

}
