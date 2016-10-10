package fr.echoes.labs.ksf.cc.plugins.foreman.dao;

import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanEnvironnment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IForemanEnvironmentDAO extends MongoRepository<ForemanEnvironnment, String> {
    //

    ForemanEnvironnment findByName(String name);
}
