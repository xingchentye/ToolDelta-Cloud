package org.tooldelta.server;

import com.alibaba.fastjson.JSONObject;
import org.tooldelta.PacketIDMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.tooldelta.logging.ColorPrint;
import org.tooldelta.packet.GetPacketID;

import org.tooldelta.packet.InitialDataInteraction;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class WebSocketServer extends AbstractWebSocketHandler {
    private WebSocketSession session;
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    private static final CopyOnWriteArrayList<WebSocketSession> webSocketSessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        WebSocketSessionManager.add(session.getId(), session);
        int count = onlineCount.incrementAndGet();
        webSocketSessions.add(session);
        ColorPrint.printInf(String.format("[ToolDelta-Server]有新的客户端接入 ID -> %s Address -> %s 当前在线客户端数量 -> %s", session.getId(), session.getLocalAddress(), count));
        sendMessage(InitialDataInteraction.PacketBuild());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        ColorPrint.printErr(String.format("[ToolDelta-Server]运行时发生异常:%s", exception.getMessage()));
        WebSocketSessionManager.removeAndClose(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        int count = onlineCount.decrementAndGet();
        webSocketSessions.remove(session);
        WebSocketSessionManager.removeAndClose(session.getId());
        ColorPrint.printInf(String.format("[ToolDelta-Server]与客户端 ID -> %s Address -> %s 断开连接，当前在线客户端数量 -> %s", session.getId(), session.getLocalAddress(), count));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String PayLoad = message.getPayload();
        ColorPrint.printInf(String.format("[ToolDelta-Server]收到来自客户端 ID -> %s Address -> %s 的消息 -> %s", session.getId(), session.getLocalAddress(), message.getPayload()));
        int PacketID = GetPacketID.GetPacketID(message.getPayload());
        Class<?> PacketTypeClass = PacketIDMap.FromIDGetClass(PacketID);
        if (PacketTypeClass == null) {
            ColorPrint.printErr(String.format("[数据包解析]异常的数据包 ID -> %s 未在数据包ID表中找到!", PacketID));
            return;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(PayLoad);
            Constructor<?> constructor = PacketTypeClass.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            Method handlePacketMethod = PacketTypeClass.getMethod("handlePacket", String.class);
            handlePacketMethod.invoke(instance, PayLoad);
        } catch (Exception error) {
            ColorPrint.printErr(String.format("[数据包解析]无法调用数据包 ID -> %s 的处理函数，异常原因:%s", PacketID, error.getMessage()));
        }
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(PayLoad);
//            Constructor<?> constructor = PacketTypeClass.getDeclaredConstructor(getParamTypes(jsonObject));
//            Object instance = constructor.newInstance(getParamValues(jsonObject));
//            Constructor<?> constructor = PacketTypeClass.getDeclaredConstructor();
//            Object instance = constructor.newInstance();
//            Method handlePacketMethod = PacketTypeClass.getMethod("handlePacket", String.class);
//            handlePacketMethod.invoke(instance, PacketID);
//        } catch (Exception error) {
//            ColorPrint.printErr(String.format("[数据包解析]无法调用数据包ID:%s的处理函数:%s", PacketID, error.getMessage()));
//        }
    }

    public void sendMessage(String message) {
        if (this.session != null && this.session.isOpen()) {
            try {
                this.session.sendMessage(new TextMessage(message));
            } catch (IOException error) {
                ColorPrint.printErr(String.format("[ToolDelta-Server]sendMessage方法推送消息时出现异常 -> %s", error.getMessage()));
            }
            ColorPrint.printInf(String.format("[ToolDelta-Server]推送消息给客户端 ID -> %s Address -> %s 消息内容为 -> %s", this.session.getId(), this.session.getLocalAddress(), message));
        }
    }

    public void sendMessageToAll(String message) {
        for (WebSocketSession webSocketSession : webSocketSessions) {
            if (webSocketSession.isOpen()) {
                try {
                    webSocketSession.sendMessage(new TextMessage(message));
                } catch (IOException error) {
                    ColorPrint.printErr(String.format("sendMessageToAll方法推送消息时出现异常 -> %s", error.getMessage()));
                }
            }
        }
    }

//    private static Class<?>[] getParamTypes(JSONObject jsonObject) {
//        JSONArray paramTypesArray = jsonObject.getJSONArray("paramTypes");
//        Class<?>[] paramTypes = new Class[paramTypesArray.size()];
//        for (int i = 0; i < paramTypesArray.size(); i++) {
//            String typeName = paramTypesArray.getString(i);
//        }
//        return paramTypes;
//    }
//
//    private static Object[] getParamValues(JSONObject jsonObject) {
//        JSONArray paramValuesArray = jsonObject.getJSONArray("paramValues");
//        Object[] paramValues = new Object[paramValuesArray.size()];
//        for (int i = 0; i < paramValuesArray.size(); i++) {
//            if (Objects.equals(paramValuesArray.get(i).toString(), "null")){
//                paramValues[i] = null;
//                continue;
//            }
//            paramValues[i] = paramValuesArray.get(i);
//        }
//        return paramValues;
//    }
}
