package org.tooldelta;

import org.tooldelta.logging.ColorPrint;
import java.util.HashMap;
import java.util.Map;

public class PacketIDMap {
    private static final Map<Integer, String> classMap = new HashMap<>();
    static {
        classMap.put(1, "org.tooldelta.packet.InitialDataInteraction");
    }
    public static Class<?> FromIDGetClass(int pkt_id) {
        String className = classMap.get(pkt_id);
        if (className != null) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException error) {
                ColorPrint.printErr("[数据包解析]通过ID获取解析类过程中出现异常:" + error.getMessage());
            }
        } else {
            return null;
        }
        return null;
    }
}
