package fr.echoes.labs.ksf.cc.plugins.nexus.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositoryData {

    public static final String PROVIDER_MAVEN2 = "maven2";
    public static final String FORMAT_MAVEN2 = "maven2";
    public static final String WRITE_POLICY_ALLOW_WRITE_ONCE = "ALLOW_WRITE_ONCE";
    public static final String WRITE_POLICY_ALLOW_WRITE = "ALLOW_WRITE";
    public static final String REPO_TYPE_HOSTED = "hosted";
    public static final String REPO_POLICY_RELEASE = "RELEASE";
    public static final String PROVIDER_ROLE_REPOSITORY = "org.sonatype.nexus.proxy.repository.Repository";

    private String id;
    private String name;
    private String provider;
    private String providerRole;
    private String format;
    private String repoType;
    private boolean exposed;
    private String writePolicy = WRITE_POLICY_ALLOW_WRITE_ONCE;
    private boolean browseable = true;
    private boolean indexable = true;
    private int notFoundCacheTTL = 1440;
    private String repoPolicy;
    private boolean downloadRemoteIndexes = false;

    public String getId() {
        return this.id;
    }

    public RepositoryData setId(final String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public RepositoryData setName(final String name) {
        this.name = name;
        return this;
    }

    public boolean isExposed() {
        return this.exposed;
    }

    public RepositoryData setExposed(final boolean exposed) {
        this.exposed = exposed;
        return this;
    }

    public String getRepoType() {
        return this.repoType;
    }

    public RepositoryData setRepoType(final String repoType) {
        this.repoType = repoType;
        return this;
    }

    public String getRepoPolicy() {
        return this.repoPolicy;
    }

    public RepositoryData setRepoPolicy(final String repoPolicy) {
        this.repoPolicy = repoPolicy;
        return this;
    }

    public String getProviderRole() {
        return this.providerRole;
    }

    public RepositoryData setProviderRole(final String providerRole) {
        this.providerRole = providerRole;
        return this;
    }

    public String getProvider() {
        return this.provider;
    }

    public RepositoryData setProvider(final String provider) {
        this.provider = provider;
        return this;
    }

    public String getFormat() {
        return this.format;
    }

    public RepositoryData setFormat(final String format) {
        this.format = format;
        return this;
    }

    public boolean isBrowseable() {
        return this.browseable;
    }

    public RepositoryData setBrowseable(final boolean browseable) {
        this.browseable = browseable;
        return this;
    }

    public boolean isDownloadRemoteIndexes() {
        return this.downloadRemoteIndexes;
    }

    public RepositoryData setDownloadRemoteIndexes(final boolean downloadRemoteIndexes) {
        this.downloadRemoteIndexes = downloadRemoteIndexes;
        return this;
    }

    public boolean isIndexable() {
        return this.indexable;
    }

    public RepositoryData setIndexable(final boolean indexable) {
        this.indexable = indexable;
        return this;
    }

    public String getWritePolicy() {
        return this.writePolicy;
    }

    public RepositoryData setWritePolicy(final String writePolicy) {
        this.writePolicy = writePolicy;
        return this;
    }

    public int getNotFoundCacheTTL() {
        return this.notFoundCacheTTL;
    }

    public RepositoryData setNotFoundCacheTTL(final int notFoundCacheTTL) {
        this.notFoundCacheTTL = notFoundCacheTTL;
        return this;
    }

}
