package com.zlw.session;

import com.zlw.redis.CacheServiceTemplate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.session.MapSession;

/**
 * @author zhangliewei
 * @date 2018/3/8 16:56
 * @opyright(c) pwc.sdc inc pwc.sdc Co.,LTD
 */
public class CacheSessionRepositoryFactoryBean implements FactoryBean<CacheSessionRepository> {

    private CacheServiceTemplate<String, MapSession> cacheServiceTemplate;
    private long expringTime;


    @Override
    public CacheSessionRepository getObject() throws Exception {
        CacheSessionRepository cacheSessionRepository = null;
        if (null != cacheServiceTemplate) {
            cacheSessionRepository = new CacheSessionRepository(cacheServiceTemplate, expringTime);
            return cacheSessionRepository;
        } else {
            throw new RuntimeException("cache cannot be shared because cacheSessionRepository or serializationService is null");
        }
    }

    @Override
    public Class<?> getObjectType() {
        return CacheSessionRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setCacheServiceTemplate(CacheServiceTemplate<String, MapSession> cacheServiceTemplate) {
        this.cacheServiceTemplate = cacheServiceTemplate;
    }

    public void setExpringTime(Integer expringTime) {
        this.expringTime = expringTime;
    }
}
