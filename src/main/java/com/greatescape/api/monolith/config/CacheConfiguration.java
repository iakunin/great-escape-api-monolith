package com.greatescape.api.monolith.config;

import com.greatescape.api.monolith.domain.Authority;
import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.City;
import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.domain.Metro;
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.QuestPhoto;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.domain.User;
import com.greatescape.api.monolith.repository.UserRepository;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, User.class.getName());
            createCache(cm, Authority.class.getName());
            createCache(cm, User.class.getName() + ".authorities");
            createCache(cm, Player.class.getName());
            createCache(cm, Quest.class.getName());
            createCache(cm, Quest.class.getName() + ".thematics");
            createCache(cm, Thematic.class.getName());
            createCache(cm, Thematic.class.getName() + ".quests");
            createCache(cm, City.class.getName());
            createCache(cm, Location.class.getName());
            createCache(cm, Location.class.getName() + ".metros");
            createCache(cm, Metro.class.getName());
            createCache(cm, Metro.class.getName() + ".locations");
            createCache(cm, Company.class.getName());
            createCache(cm, Slot.class.getName());
            createCache(cm, Subscriber.class.getName());
            createCache(cm, QuestPhoto.class.getName());
            createCache(cm, QuestIntegrationSetting.class.getName());
            createCache(cm, Booking.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
