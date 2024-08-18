package org.tooldelta.client;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.tooldelta.PacketIDMap;
import org.tooldelta.logging.ColorPrint;
import org.tooldelta.packet.GetPacketID;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketClientConfig {
    @Bean
    public WebSocketClient WebSocketClient(){
        WebSocketClient WebSocketCli = null;
        try {
            WebSocketCli = new WebSocketClient(new URI("ws://localhost:9213/websocket")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    ColorPrint.printSuc("[WebSocket-Client]成功与ToolDelta-Server建立活动连接!");
                }

                @Override
                public void onMessage(String Message) {
                    ColorPrint.printInf(String.format("[WebSocket-Client]收到来自服务端的消息，消息内容：%s", Message));
                    int PacketID = GetPacketID.GetPacketID(Message);
                    Class<?> PacketTypeClass = PacketIDMap.FromIDGetClass(PacketID);
                    if (PacketTypeClass == null) {
                        ColorPrint.printErr(String.format("[数据包解析]异常的数据包 ID -> %s 未在数据包ID表中找到!", PacketID));
                        return;
                    }
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(Message);
                        Constructor<?> constructor = PacketTypeClass.getDeclaredConstructor();
                        Object instance = constructor.newInstance();
                        Method handlePacketMethod = PacketTypeClass.getMethod("handlePacket", String.class);
                        handlePacketMethod.invoke(instance, Message);
                    } catch (Exception error) {
                        ColorPrint.printErr(String.format("[数据包解析]无法调用数据包 ID -> %s 的处理函数，异常原因:%s", PacketID, error.getMessage()));
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean suc) {
                    ColorPrint.printErr(String.format("[WebSocket-Client]与ToolDelta-Server断开连接，Code: %s Reason: %s %s", code, reason, suc));
                }

                @Override
                public void onError(Exception error) {
                    ColorPrint.printErr(String.format("[WebSocket-Client]与ToolDelta-Server连接时出现异常:%s", error.getMessage()));
                }
            };
            WebSocketCli.connect();
            return WebSocketCli;
        }catch (URISyntaxException error){
            ColorPrint.printErr(String.format("[WebSocket-Client]运行时出现异常:%s", error.getMessage()));
        }
        return WebSocketCli;
    }
}

