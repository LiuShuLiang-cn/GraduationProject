package org.zucc.controller.websocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.zucc.entity.Chat;
import org.zucc.entity.Systems;
import org.zucc.service.ChatService;
import org.zucc.service.SystemService;
import org.zucc.utils.RoleUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Component
@Slf4j
@ServerEndpoint("/websocket/{systemId}/{role}")
public class MessageWS {

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
        MessageWS.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "systemId") String systemId, @PathParam("role") String role) {
        try {
            SESSIONS.add(session);
            SESSION_POOL.put(systemId + "-" + role, session);
            log.info("【WebSocket消息】有新的连接，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            SESSIONS.remove(session);
            log.info("【WebSocket消息】连接断开，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam(value = "systemId") String systemId, @PathParam("role") String role) {
        log.info("【WebSocket消息】收到客户端{}消息{}：", systemId + "-" + role, message);
        ChatService chatService = (ChatService) applicationContext.getBean("chatServiceImpl");
        SystemService systemService = (SystemService) applicationContext.getBean("systemServiceImpl");
        Chat chat = JSON.parseObject(message, Chat.class);
        Systems systems = systemService.getById(systemId);
        chat.setSystemName(systems.getSystemName());
        chatService.save(chat);
        if (chat.getType().equals("1")) {
            // 指令
            sendOneMessage(systemId, role, chat);
        } else if (chat.getType().equals("2")) {
            // 普通消息
            sendOneMessage(systemId, role, chat);
        } else if ((chat.getType().equals("3"))) {
            // 群发
            sendAllMessage(chat, systemId, role);
        } else if (chat.getType().equals("4")) {
            sendAllMessage(chat, systemId, role);
        }
    }


    /**
     * 此为广播消息
     *
     * @param message 消息
     */
    private void sendAllMessage(Chat message, @PathParam(value = "systemId") String systemId, @PathParam("role") String role) {
        log.info("【WebSocket消息】广播消息：" + message);
        ChatService chatService = (ChatService) applicationContext.getBean("chatServiceImpl");
        SystemService systemService = (SystemService) applicationContext.getBean("systemServiceImpl");
        ArrayList<Session> sessions = new ArrayList<>();
        for (String s : RoleUtils.getRole(systemId, role)) {
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

    /**
     * 此为单点消息
     */
    public void sendOneMessage(String systemId, String role, Chat message) {
        Session session = SESSION_POOL.get(systemId + "-" + message.getToRole());
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    session.getAsyncRemote().sendText(JSON.toJSONString(message));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息(多人)
     *
     * @param userIds 用户编号列表
     * @param message 消息
     */
    public void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session session = SESSION_POOL.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【WebSocket消息】单点消息：" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
