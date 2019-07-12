package com.zlw.session;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;

import java.util.HashMap;

/**
 * @author zhangliewei
 * @date 2018/3/8 16:53
 * @opyright(c) pwc.sdc inc pwc.sdc Co.,LTD
 */
public class SessionRepositoryFilterFactoryBean implements FactoryBean<SessionRepositoryFilter<MapSession>> {

    private SessionRepository<MapSession> sessionRepository = new MapSessionRepository(new HashMap<>());

    public void setSessionRepository(SessionRepository<MapSession> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public SessionRepositoryFilter<MapSession> getObject() throws Exception {
        return new SessionRepositoryFilter<MapSession>(sessionRepository);
    }

    @Override
    public Class<?> getObjectType() {
        return SessionRepositoryFilter.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
