package fr.echoes.lab.ksf.database.configuration;

import com.google.common.collect.Lists;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

/**
 * Configuration of the MongoDB database.
 * @author sleroy
 */
@Profile("default")
@Configuration
@ConfigurationProperties(prefix = "mongo")
@EnableMongoRepositories(basePackages = {"fr.echoes.lab", "com.tocea"})
class DatabaseConfiguration extends AbstractMongoConfiguration {

    private String host;
    private String port;
    private String dbname;
    private String login;
    private String password;

    public String getDatabaseName() {
        return dbname;
    }

    @Bean
    public Mongo mongo() throws Exception {

        ServerAddress serverAdress = new ServerAddress(host, Integer.parseInt(port));

        if (login == null || login == "") {

            return new MongoClient(serverAdress);

        } else {

            return new MongoClient(
                    Lists.newArrayList(serverAdress),
                    Lists.newArrayList(MongoCredential.createCredential(login, dbname, password.toCharArray()))
            );

        }

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
