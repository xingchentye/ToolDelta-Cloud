package org.tooldelta.packet;

import com.alibaba.fastjson.JSONObject;
import org.tooldelta.server.WebSocketServerCore;

public class UserAuthentication {
    public static final int PktID = 2;
    public static String PktData;

    public static String PacketBuild(String username, String password) {
        JSONObject MainJson = new JSONObject();
        JSONObject SubJson = new JSONObject();
        MainJson.put("PktID", PktID);
        SubJson.put("UserName", username);
        SubJson.put("PassWord", password);
        MainJson.put("Data",SubJson.toJSONString());
        return MainJson.toJSONString();
    }

    public static String PacketParse(String Packet) {
        JSONObject MainJson = JSONObject.parseObject(Packet);
        return MainJson.toJSONString();
    }
}
