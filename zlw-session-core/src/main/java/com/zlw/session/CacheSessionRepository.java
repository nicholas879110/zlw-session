package com.zlw.session;

import com.zlw.redis.CacheServiceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;

import java.time.Duration;

/**
 * @author zhangliewei
 * @date 2018/3/8 16:38
 * @opyright(c) pwc.sdc inc pwc.sdc Co.,LTD
 */
public class CacheSessionRepository implements SessionRepository<MapSession> {
    private Logger logger = LoggerFactory.getLogger(CacheSessionRepository.class);

    private CacheServiceTemplate<String, MapSession> cacheServiceTemplate;

    private Integer defaultMaxInactiveInterval;
    private long expiringTime = 1800;

    public CacheSessionRepository(CacheServiceTemplate<String, MapSession> cacheServiceTemplate, long expiringTime) {
        this.cacheServiceTemplate = cacheServiceTemplate;
        this.expiringTime = expiringTime;
    }


    @Override
    public MapSession createSession() {
        MapSession session = new MapSession();
        if (this.defaultMaxInactiveInterval != null) {
            session.setMaxInactiveInterval(Duration.ofSeconds(this.defaultMaxInactiveInterval));
        }
        logger.debug("create session:{}", session.getId());
        return session;
    }

    @Override
    public void save(MapSession expiringSession) {
        logger.debug("save session:{}", expiringSession.getId());
        cacheServiceTemplate.set(expiringSession.getId(), expiringSession, expiringTime);
    }

    @Override
    public MapSession findById(String id) {
        logger.debug("get session by id:{}", id);
        MapSession mapSession = cacheServiceTemplate.get(id);
        if (mapSession == null) {
            return null;
        }
        //todo  session逻辑
        if (mapSession.isExpired()) {
            this.deleteById(mapSession.getId());
            return null;
        } else {
            return new MapSession(mapSession);
        }

    }

    @Override
    public void deleteById(String id) {
        logger.debug("delete session by id:{}", id);
        cacheServiceTemplate.remove(id);
    }

    public Integer getDefaultMaxInactiveInterval() {
        return defaultMaxInactiveInterval;
    }

    public void setDefaultMaxInactiveInterval(Integer defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    public long getExpiringTime() {
        return expiringTime;
    }

    public void setExpiringTime(long expiringTime) {
        this.expiringTime = expiringTime;
    }
}
