package org.tooldelta.packet;

import com.alibaba.fastjson.JSONObject;

import org.tooldelta.auth.GenerateClientAuthenticationData;
import org.tooldelta.leveldb.LevelDataBaseUtils;
import org.tooldelta.logging.ColorPrint;
import org.tooldelta.type.NodeInfo;
import org.tooldelta.server.WebSocketServerCore;

public class InitialDataInteraction {
    public static final int PktID = 1;
    public static String PktData;

    public static String PacketBuild() {
        JSONObject MainJson = new JSONObject();
        JSONObject SubJson = new JSONObject();
        MainJson.put("PktID", PktID);
        SubJson.put("CoreVersion", WebSocketServerCore.CoreVersion);
        SubJson.put("ServerName", WebSocketServerCore.ServerName);
        SubJson.put("NodeUID", WebSocketServerCore.NodeUID);
        MainJson.put("PktData",SubJson.toJSONString());
        return MainJson.toJSONString();
    }

    public static NodeInfo PacketParse(String Message){
        JSONObject MainJson = JSONObject.parseObject(Message);
        JSONObject SubJson = JSONObject.parseObject(MainJson.getString("PktData"));
        return new NodeInfo(SubJson.getString("ServerName"), SubJson.getString("CoreVersion"), SubJson.getString("NodeUID"));
    }

    public static boolean handlePacket(String Message) {
        LevelDataBaseUtils LevelDataBase = new LevelDataBaseUtils("DataBase/NodeInfo", "utf-8");
        LevelDataBase.InitLevelDB();
        LevelDataBase.Delete("NodeInfo");
        NodeInfo NodeINFO = PacketParse(Message);
        LevelDataBase.Put("NodeInfo", NodeINFO);
        LevelDataBase.CloseDataBase();
        new GenerateClientAuthenticationData("xingchen", "123456");
        return true;
    }
}
