package org.tooldelta.auth;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;

import org.tooldelta.logging.ColorPrint;
import org.tooldelta.leveldb.LevelDataBaseUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class GeneralApproach {
    @SneakyThrows
    public static Key GenerateKeysThroughAlgorithms(String KeyAlgorithm) {
        LevelDataBaseUtils LevelDataBase = new LevelDataBaseUtils("DataBase/NodeInfo", "utf-8");
        LevelDataBase.InitLevelDB();

        SecureRandom VSecureRandom = new SecureRandom();
        JSONObject MainJson = JSONObject.parseObject(LevelDataBase.Get("NodeInfo").toString());
        String NodeUID = MainJson.getString("NodeUID");
        VSecureRandom.setSeed(GenerateToken(NodeUID).getBytes());

        KeyGenerator VKeyGenerator = null;
        try {
            VKeyGenerator = KeyGenerator.getInstance(KeyAlgorithm);
        } catch (NoSuchAlgorithmException error) {
            ColorPrint.printErr(String.format("[Auth]生成 %s 密钥时出现异常 -> %s", KeyAlgorithm, error.getMessage()));
            return null;
        }

        VKeyGenerator.init(VSecureRandom);
        return VKeyGenerator.generateKey();
    }


    public static String GenerateToken(String Data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static String EncryptDataWithKeys(String Data, String CharSet, Key VKey) {
        byte[] VBytes = Data.getBytes(CharSet);
        Cipher VCipher = Cipher.getInstance(VKey.getAlgorithm());
        VCipher.init(Cipher.ENCRYPT_MODE, VKey);
        byte[] DoFinal = VCipher.doFinal(VBytes);
        return Base64.getEncoder().encodeToString(DoFinal);
    }
}