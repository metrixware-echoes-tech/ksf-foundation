package fr.echoes.labs.ksf.cc.plugins.nexus.extensions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "repository")
@XmlAccessorType(XmlAccessType.FIELD)
public class Repository {

    @XmlElement(name = "data")
    private RepositoryData data;

    /**
     * @return the data
     */
    public RepositoryData getData() {
        return this.data;
    }

    /**
     * @param data the data to set
     */
    public Repository setData(final RepositoryData data) {
        this.data = data;
        return this;
    }
}
