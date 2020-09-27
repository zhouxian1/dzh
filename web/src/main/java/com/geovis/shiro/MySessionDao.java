package com.geovis.shiro;

import com.geovis.util.RedisUtil;
import com.geovis.util.SerializableUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于redis的sessionDao，缓存共享session
 */
@Component
public class MySessionDao extends CachingSessionDAO {

    private static Logger _log = LoggerFactory.getLogger(MySessionDao.class);
    // 会话key
    private final static String SYS_SHIRO_SESSION_ID = "sys-shiro-session-id";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    private  static Jedis jedis;

    public Jedis getJedis(){
        if(jedis==null){
            jedis=(Jedis)jedisConnectionFactory.getConnection().getNativeConnection();
            return  jedis;
        }
        return jedis;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        redisUtil.set(SYS_SHIRO_SESSION_ID + "_" + sessionId, SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
        _log.debug("doCreate >>>>> sessionId={}", sessionId);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String session = (String) redisUtil.get(SYS_SHIRO_SESSION_ID + "_" + sessionId);
        _log.debug("doReadSession >>>>> sessionId={}", sessionId);
        return SerializableUtil.deserialize(session);
    }

    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要再更新了
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return;
        }
        // 更新session的最后一次访问时间
        MySession mySession = (MySession) session;
        MySession cacheSession = (MySession) doReadSession(session.getId());
        if (null != cacheSession) {
        	mySession.setStatus(cacheSession.getStatus());
        	mySession.setAttribute("FORCE_LOGOUT", cacheSession.getAttribute("FORCE_LOGOUT"));
        }
        redisUtil.set(SYS_SHIRO_SESSION_ID + "_" + session.getId(), SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
        // 更新ZHENG_UPMS_SERVER_SESSION_ID、ZHENG_UPMS_SERVER_CODE过期时间 TODO
        _log.debug("doUpdate >>>>> sessionId={}", session.getId());
    }

    @Override
    protected void doDelete(Session session) {
        String sessionId = session.getId().toString();
        // 删除session
        redisUtil.del(SYS_SHIRO_SESSION_ID + "_" + sessionId);
        _log.debug("doUpdate >>>>> sessionId={}", sessionId);
    }

    /**
     * 获取会话列表
     * @param offset
     * @param limit
     * @return
     */
    public Map getActiveSessions(int offset, int limit) {
        Map sessions = new HashMap();
        // 获取在线会话总数
        long total = jedis.llen(SYS_SHIRO_SESSION_ID);
        // 获取当前页会话详情
        List<String> ids = jedis.lrange(SYS_SHIRO_SESSION_ID, offset, (offset + limit - 1));
        List<Session> rows = new ArrayList<>();
        for (String id : ids) {
            String session = (String)redisUtil.get(SYS_SHIRO_SESSION_ID + "_" + id);
            // 过滤redis过期session
            if (null == session) {
                total = total - 1;
                continue;
            }
             rows.add(SerializableUtil.deserialize(session));
        }
        jedis.close();
        sessions.put("total", total);
        sessions.put("rows", rows);
        return sessions;
    }

    /**
     * 强制退出
     * @param ids
     * @return
     */
    public int forceout(String ids) {
        String[] sessionIds = ids.split(",");
        for (String sessionId : sessionIds) {
            // 会话增加强制退出属性标识，当此会话访问系统时，判断有该标识，则退出登录
            String session = (String)redisUtil.get(SYS_SHIRO_SESSION_ID + "_" + sessionId);
            MySession upmsSession = (MySession) SerializableUtil.deserialize(session);
            upmsSession.setStatus(MySession.OnlineStatus.force_logout);
            upmsSession.setAttribute("FORCE_LOGOUT", "FORCE_LOGOUT");
            redisUtil.set(SYS_SHIRO_SESSION_ID + "_" + sessionId, SerializableUtil.serialize(upmsSession), (int) upmsSession.getTimeout() / 1000);
        }
        return sessionIds.length;
    }

    /**
     * 更改在线状态
     *
     * @param sessionId
     * @param onlineStatus
     */
    public void updateStatus(Serializable sessionId, MySession.OnlineStatus onlineStatus) {
        MySession session = (MySession) doReadSession(sessionId);
        if (null == session) {
            return;
        }
        session.setStatus(onlineStatus);
        redisUtil.set(SYS_SHIRO_SESSION_ID + "_" + session.getId(), SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
    }

}
