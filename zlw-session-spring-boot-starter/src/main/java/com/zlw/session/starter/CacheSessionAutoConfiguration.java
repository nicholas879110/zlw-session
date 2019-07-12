package com.zlw.session.starter;

import com.zlw.redis.CacheServiceTemplate;
import com.zlw.session.CacheSessionRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.session.SessionRepository;

@Configuration
@EnableConfigurationProperties(SessionProperties.class)
@ConditionalOnClass({CacheSessionRepository.class, CacheServiceTemplate.class})
@Order
public class CacheSessionAutoConfiguration{


    private CacheServiceTemplate cacheServiceTemplate;

    @Bean
    @ConditionalOnBean(value = CacheServiceTemplate.class)
    @ConditionalOnMissingBean(value = SessionRepository.class)
    public CacheSessionRepository sessionRepository() {
        return new CacheSessionRepository(this.cacheServiceTemplate, 1800);
    }

    @Autowired
    public void setCacheServiceTemplate(
            @SpringSessionCacheFactory ObjectProvider<CacheServiceTemplate> springSessionRedisConnectionFactory,
            ObjectProvider<CacheServiceTemplate> cacheServiceTemplateFactory) {
        CacheServiceTemplate cacheServiceTemplateFactoryToUse = springSessionRedisConnectionFactory
                .getIfAvailable();
        if (cacheServiceTemplateFactoryToUse == null) {
            cacheServiceTemplateFactoryToUse = cacheServiceTemplateFactory.getObject();
        }
        this.cacheServiceTemplate = cacheServiceTemplateFactoryToUse;
    }

}



