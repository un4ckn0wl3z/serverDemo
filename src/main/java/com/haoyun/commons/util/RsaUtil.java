package com.haoyun.commons.util;

import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Created by songcz on 2017/6/28.
 */
public class RsaUtil {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static String publicKey = PropertiesUtil.getString("rsa_public_key");

    private static String privateKey = PropertiesUtil.getString("rsa_private_key");

    private static KeyFactory keyFactory = null;
    private static PublicKey pubKey = null;
    private static PrivateKey priKey = null;
    private static Signature signatureCheck = null;
    private static Signature signatureSign = null;

    static {
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
            priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
            signatureCheck = Signature.getInstance(SIGN_ALGORITHMS);
            signatureCheck.initVerify(pubKey);

            signatureSign = Signature.getInstance(SIGN_ALGORITHMS);
            signatureSign.initSign(priKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static boolean check(String content, String sign) {

        boolean result = false;
        try {
            signatureCheck.update(content.getBytes("UTF-8"));
            result = signatureCheck.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String sign(String data) {

        String result = "";
        try {
            signatureSign.update(data.getBytes("UTF-8"));
            byte[] signed = signatureSign.sign();
            result = Base64.encodeBase64String(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String formatMap(Map<String, String> paraMap) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(tmpMap.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds){
                String val = item.getValue();
                buf.append(val);
            }
            buff = buf.toString();
        }catch (Exception e){
            return null;
        }
        return buff;
    }
}
