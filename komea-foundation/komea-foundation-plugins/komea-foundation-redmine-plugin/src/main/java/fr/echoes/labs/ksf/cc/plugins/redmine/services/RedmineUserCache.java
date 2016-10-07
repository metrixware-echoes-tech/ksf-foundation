package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.taskadapter.redmineapi.bean.User;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public enum RedmineUserCache {

    INSTANCE;

    private static final long MAX_SIZE = 200;
    private final Cache<String, User> cache;

    private RedmineUserCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(MAX_SIZE)
                .expireAfterWrite(4, TimeUnit.HOURS)
                .build();
    }

    /**
     * Returns the {@link User} for the given username.
     *
     * @param username the username.
     * @return the user or {@code null} if there is no cached value for the
     * username.
     */
    public User get(String username) {
        return cache.getIfPresent(username);
    }

    public User getUserById(Integer id) {
        final Collection<User> users = this.cache.asMap().values();
        for (final User user : users) {
            if (id.equals(user.getId())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Stores the {@link User} for the given username.
     *
     * @param username the username.
     * @param user the user.
     */
    public void put(String username, User user) {
        cache.put(username, user);
    }
}
