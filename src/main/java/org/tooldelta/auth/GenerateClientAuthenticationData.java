package org.tooldelta.auth;

import org.tooldelta.auth.GeneralApproach;
import org.tooldelta.leveldb.LevelDataBaseUtils;
import org.tooldelta.logging.ColorPrint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenerateClientAuthenticationData {
    public static final String CHARSET = StandardCharsets.UTF_8.name();

    public GenerateClientAuthenticationData(String UserName, String PassWord) {
        Key VKey = GeneralApproach.GenerateKeysThroughAlgorithms("DES");
        UUID VUid = UUID.randomUUID();
        ColorPrint.printInf("1");
//        if (VKey == null){
//            ColorPrint.printErr("[Auth]生成的Key内容为NULL!");
//            return;
//        }
        ColorPrint.printInf("2");
        LevelDataBaseUtils LevelDataBase = new LevelDataBaseUtils("DataBase/Auth", CHARSET);
        LevelDataBase.InitLevelDB();
        ColorPrint.printInf("3");
        LevelDataBase.Put("Key", VKey);
        ColorPrint.printInf("6");
        LevelDataBase.Put("Uid", VUid.toString());
        ColorPrint.printInf("4");
        LevelDataBase.Put("UserName", GeneralApproach.EncryptDataWithKeys(UserName, CHARSET, VKey));
        ColorPrint.printInf("5");
        LevelDataBase.Put("PassWord", GeneralApproach.EncryptDataWithKeys(PassWord, CHARSET, VKey));
        LevelDataBase.CloseDataBase();
    }
}
