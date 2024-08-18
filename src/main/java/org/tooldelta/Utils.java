package org.tooldelta;

import java.util.UUID;
import org.tooldelta.leveldb.LevelDataBaseUtils;

public class Utils {

    public static String MakeNodeUID (int length) {
        LevelDataBaseUtils LevelDataBase = new LevelDataBaseUtils("DataBase/NodeUID", "utf-8");
        LevelDataBase.InitLevelDB();
        Object NodeUID = LevelDataBase.Get("Node-UID");
        if (NodeUID != null) {
            LevelDataBase.CloseDataBase();
            return NodeUID.toString();
        }
        String fullUuid = UUID.randomUUID().toString().replace("-", "");
        LevelDataBase.Put("Node-UID", fullUuid.substring(0, length).toUpperCase());
        LevelDataBase.CloseDataBase();
        return fullUuid.substring(0, length).toUpperCase();
    }
}
