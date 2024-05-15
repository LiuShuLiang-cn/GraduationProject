package org.zucc.controller.websocket;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.zucc.dao.DeployDao;
import org.zucc.dao.NumberOfPeopleDao;
import org.zucc.dao.OperateDao;
import org.zucc.dao.SystemDao;
import org.zucc.entity.*;
import org.zucc.entity.vo.DataVo;
import org.zucc.service.ChatService;
import org.zucc.service.SystemService;
import org.zucc.utils.CastClass;
import org.zucc.utils.RoleUtils;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * WebSocket传送数据
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/data/{systemId}/{role}")
public class DataWS {
    /**
     * 线程安全的无序的集合
     */
    private static final CopyOnWriteArraySet<Session> SESSIONS = new CopyOnWriteArraySet<>();

    /**
     * 存储在线连接数
     */
    private static final Map<String, Session> SESSION_POOL = new HashMap<>();
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        DataWS.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "systemId") String systemId,@PathParam("role")String role) {
        try {
            SESSIONS.add(session);
            SESSION_POOL.put(systemId+"-"+role, session);
            log.info("【WebSocket数据】有新的连接，总数为：{},连接系统为：{}" , SESSIONS.size(),systemId);
        } catch (Exception e) {
            log.error("【WebSocket数据】连接发生异常");
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            SESSIONS.remove(session);
            log.info("【WebSocket数据】连接断开，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            log.error("【WebSocket数据】断开发生异常");
            e.printStackTrace();
        }
    }

    /**
     * 此为广播消息
     *
     * @param message 消息
     */
    public void sendAllMessage(DataVo message, @PathParam(value = "systemId") String systemId, @PathParam("role") String role) {
        //log.info("【WebSocket消息】广播消息：" + message);
        ArrayList<Session> sessions = new ArrayList<>();
        for (String s : RoleUtils.getAllRole(systemId, role)) {
            if (SESSION_POOL.containsKey(s)) {
                sessions.add(SESSION_POOL.get(s));
            }
        }
        for (Session session : sessions) {
            try {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(JSON.toJSONString(message));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    private  NumberOfPeopleDao numberOfPeopleDao;
    @Resource
   private SystemDao systemDao;
    @Resource
    private OperateDao operateDao;
    @Resource
    private DeployDao deployDao;
    @org.springframework.scheduling.annotation.Scheduled(cron = "*/5 * * * * ?")
    public void sendData( ){
        //RedisTemplate redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
        Set<String> keys = redisTemplate.keys("*" + "_NumberOfPeople");
        assert keys != null;
        if (keys.isEmpty()||keys.size()==0) {
            return;
        }
        for (String key : keys) {
            String[] parts = key.split("_");
            String systemName = parts[0];
            Object object;
            object = redisTemplate.opsForValue().get(systemName + "_NumberOfPeople");
            List<NumberOfPeople> peoples = CastClass.castList(object, NumberOfPeople.class);
            Object object2 = redisTemplate.opsForValue().get(systemName + "_Deploys");
            List<Deploy> deploys = CastClass.castList(object2, Deploy.class);
            String time = (String) redisTemplate.opsForValue().get(systemName + "_Time");
            Operate operate = (Operate) redisTemplate.opsForValue().get(systemName + "_operate");
            if (peoples == null || deploys == null || time == null||deploys.isEmpty()||operate==null) {
                peoples = numberOfPeopleDao.getNumBySys(systemName);
                deploys = deployDao.getNumBySys(systemName);
                QueryWrapper<Operate> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("systemName", systemName);
                operate = operateDao.selectList(queryWrapper).get(0);
                QueryWrapper<Systems> systemsQueryWrapper = new QueryWrapper<>();
                systemsQueryWrapper.eq("systemname", systemName);
                time = systemDao.selectOne(systemsQueryWrapper).getRunTime();
                if (peoples.size()!=9){
                    throw new RuntimeException("人数列表不够9个");
                }

                redisTemplate.opsForValue().set(systemName + "_NumberOfPeople", peoples);
                redisTemplate.opsForValue().set(systemName + "_Deploys", deploys);
                redisTemplate.opsForValue().set(systemName + "_Time", time);
                redisTemplate.opsForValue().set(systemName + "_operate", operate);
            }
            DataVo vo = new DataVo();
            vo.setNumberOfPeopleList(peoples);
            vo.setDeployList(deploys);
            vo.setSystemTime(time);
            vo.setOperate(operate);
            QueryWrapper<Systems> wrapper = new QueryWrapper<Systems>();
            wrapper.eq("systemname", systemName);
            sendAllMessage(vo,String.valueOf(systemDao.selectOne(wrapper).getId()),"");
        }

    }



}
