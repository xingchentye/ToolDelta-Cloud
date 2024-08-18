package org.tooldelta.packet;

import com.alibaba.fastjson.JSONObject;

public class GetPacketID {
    public static int GetPacketID(String Packet) {
        JSONObject jsonObject = JSONObject.parseObject(Packet);
        return jsonObject.getIntValue("PktID");
    }
}
