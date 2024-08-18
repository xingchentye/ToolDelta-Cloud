package org.tooldelta.leveldb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import org.tooldelta.logging.ColorPrint;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelDataBaseUtils {
    private DB DataBase;
    private final String DataBaseFolder;
    private final String CharSet;

    public LevelDataBaseUtils (String DataBaseFilePath, String CharSet) {
        this.DataBaseFolder = DataBaseFilePath;
        this.CharSet = CharSet;
    }

    public void InitLevelDB () {
        DBFactory DataBaseFactory = new Iq80DBFactory();
        Options DataBaseOptions = new Options();
        DataBaseOptions.createIfMissing(true);
        try {
            this.DataBase = DataBaseFactory.open(new File(DataBaseFolder), DataBaseOptions);
        } catch (IOException error) {
            ColorPrint.printErr(String.format("依赖-LevelDB初始化过程中出现异常:%s", error.getMessage()));
        }
    }

    private byte[] Serializer (Object Obj) {
        String jsonString = JSON.toJSONString(Obj, SerializerFeature.DisableCircularReferenceDetect);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONObject newJsonObject = new JSONObject();
        for (String key : jsonObject.keySet()) {
            newJsonObject.put(Capitalize(key), jsonObject.get(key));
        }
        return newJsonObject.toJSONString().getBytes();
    }

    private String Capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private Object Deserializer (byte[] bytes) {
        String Str = new String(bytes);
        return JSON.parse(Str);
    }

    public void Put (String Key, Object Val) {
        try {
            this.DataBase.put(Key.getBytes(CharSet), this.Serializer(Val));
        } catch (UnsupportedEncodingException error) {
            ColorPrint.printErr(String.format("依赖-LevelDB使用PUT方法时候编码转化异常:%s", error.getMessage()));
        }
    }

    public Object Get (String Key) {
        byte[] Val = null;
        try {
            Val = DataBase.get(Key.getBytes(CharSet));
        } catch (Exception error) {
            ColorPrint.printErr(String.format("依赖-LevelDB使用GET方法时获取失败:%s", error.getMessage()));
            return null;
        }
        if (Val == null) {
            return null;
        }
        return Deserializer(Val);
    }

    public void Delete (String Key) {
        try {
            DataBase.delete(Key.getBytes(CharSet));
        } catch (Exception error) {
            ColorPrint.printErr(String.format("依赖-LevelDB使用DELETE方法时删除失败:%s", error.getMessage()));
        }
    }

    public void CloseDataBase () {
        if (DataBase != null) {
            try {
                DataBase.close();
            } catch (IOException error) {
                ColorPrint.printErr(String.format("依赖-LevelDB使用CLOSE方法时关闭失败:%s", error.getMessage()));
            }
        }
    }

    public List<String> GetKeys () {
        List<String> KeyList = new ArrayList<>();
        DBIterator Iterator = null;
        try {
            Iterator = DataBase.iterator();
            while (Iterator.hasNext()) {
                Map.Entry<byte[], byte[]> Item = Iterator.next();
                String Key = new String(Item.getKey(), CharSet);
                KeyList.add(Key);
            }
        } catch (Exception error) {
            ColorPrint.printErr(String.format("依赖-LevelDB使用GETKEYS方法时编译异常:%s", error.getMessage()));
        } finally {
            if (Iterator != null) {
                try {
                    Iterator.close();
                } catch (IOException error) {
                    ColorPrint.printErr(String.format("依赖-LevelDB使用GETKEYS方法时编译异常:%s", error.getMessage()));
                }
            }
        }
        return KeyList;
    }
}
