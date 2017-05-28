package com.superhard.lbsw.websocket;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Mr.Yangxiufeng on 2016/5/27.
 * 09
 * 23
 * SpringWebApp
 */
public class AnalysisWebSocketHandler extends TextWebSocketHandler{
	private static Logger logger = LogManager.getLogger(HandshakeInterceptor.class);
	private static Gson gson = new Gson();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("afterConnectionEstablished");
//        String userId = MapUtils.getString(session.getAttributes(),"userId");
//        //记录连接的session
//        WebSocketSessionUtils.add(userId,session);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	logger.info("handleTextMessage"+message);
        super.handleTextMessage(session, message);
        System.out.println("handleTextMessage");
        Map <String,String> map = gson.fromJson(message.getPayload(), new TypeToken<Map<String, String>>(){}.getType());
//        String userId = MapUtils.getString(session.getAttributes(),"userId");
        String userId = MapUtils.getString(map,"userId");
//        String partnerInfo = MapUtils.getString(session.getAttributes(),"partnerInfo");
        String partnerInfo = MapUtils.getString(map,"partnerInfo");
        if (null != partnerInfo){
            //TODO 进行配对
        }else{
            //TODO 处理其他消息
            TextMessage returnMessage = new TextMessage("Hi:"+userId+"..your message:"+message.getPayload()+" I have received,now I tell you");
            WebSocketSessionUtils.sendMessageToTarget(userId,returnMessage);
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        logger.info("handleTransportError");
        System.out.println("handleTransportError");
        String userId = MapUtils.getString(session.getAttributes(),"userId");
        WebSocketSessionUtils.remove(userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        
        logger.info("afterConnectionClosed");
        System.out.println("afterConnectionClosed");
        String userId = MapUtils.getString(session.getAttributes(),"userId");
        WebSocketSessionUtils.remove(userId);
    }
}