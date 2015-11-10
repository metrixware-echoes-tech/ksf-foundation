package fr.echoes.lab.ksf.cc.sf.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.echoes.lab.ksf.cc.sf.domain.SFApplication;
import fr.echoes.lab.ksf.cc.sf.domain.SFApplicationType;

public interface ISFApplicationDAO extends MongoRepository<SFApplication, String> {

	List<SFApplication> findByType(SFApplicationType type);
	
}
