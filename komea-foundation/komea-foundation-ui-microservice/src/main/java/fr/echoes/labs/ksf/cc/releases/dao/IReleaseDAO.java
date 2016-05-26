package fr.echoes.labs.ksf.cc.releases.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.echoes.labs.ksf.cc.releases.model.Release;

/**
 * @author dcollard
 *
 */
public interface IReleaseDAO extends MongoRepository<Release, String> {

}
