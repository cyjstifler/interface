package com.superhard.lbsw.cer;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;

import javax.crypto.Cipher;

import com.alibaba.druid.util.Base64;

public class Test {
	
    /** 
     * @cerPath Java读取Cer证书信息 
     * @throws Exception  
     * @return X509Cer对象 
     */  
    public static PublicKey getX509CerCate(String cerPath) throws Exception {  
        X509Certificate x509Certificate = null;  
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");  
        FileInputStream fileInputStream = new FileInputStream(cerPath);  
        x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);  
        fileInputStream.close();  
        System.out.println("读取Cer证书信息...");  
        System.out.println("x509Certificate_SerialNumber_序列号___:"+x509Certificate.getSerialNumber());  
        System.out.println("x509Certificate_getIssuerDN_发布方标识名___:"+x509Certificate.getIssuerDN());   
        System.out.println("x509Certificate_getSubjectDN_主体标识___:"+x509Certificate.getSubjectDN());  
        System.out.println("x509Certificate_getSigAlgOID_证书算法OID字符串___:"+x509Certificate.getSigAlgOID());  
        System.out.println("x509Certificate_getNotBefore_证书有效期___:"+x509Certificate.getNotAfter());  
        System.out.println("x509Certificate_getSigAlgName_签名算法___:"+x509Certificate.getSigAlgName());  
        System.out.println("x509Certificate_getVersion_版本号___:"+x509Certificate.getVersion());  
        System.out.println("x509Certificate_getPublicKey_公钥___:"+x509Certificate.getPublicKey());  
        return x509Certificate.getPublicKey();  
    }  
    

    /** 
     * 读取私钥  返回PrivateKey 
     * @param path  包含私钥的证书路径 
     * @param password  私钥证书密码 
     * @return 返回私钥PrivateKey 
     * @throws KeyStoreException 
     * @throws NoSuchAlgorithmException 
     * @throws CertificateException 
     * @throws IOException 
     * @throws UnrecoverableKeyException 
     */  
    private static PrivateKey getPrivateKey(String path,String password)  
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException,  
            IOException, UnrecoverableKeyException {  
        KeyStore ks = KeyStore.getInstance("PKCS12");  
        FileInputStream fis = new FileInputStream(path);  
        char[] nPassword = null;  
        if ((password == null) || password.trim().equals("")) {  
            nPassword = null;  
        } else {  
            nPassword = password.toCharArray();  
        }  
        ks.load(fis, nPassword);  
        fis.close();  
        Enumeration<String> en = ks.aliases();  
        String keyAlias = null;  
        if (en.hasMoreElements()) {  
            keyAlias = (String) en.nextElement();  
        }  
   
        return (PrivateKey) ks.getKey(keyAlias, nPassword);  
    }  
    
    
    
    /** 
     * 私钥签名： 签名方法如下：BASE64(RSA(MD5(src),privatekey))，其中src为需要签名的字符串， 
privatekey是商户的CFCA证书私钥。 
     * @param plainText 待签名字符串 
     * @param path 签名私钥路径 
     * @param password  签名私钥密码 
     * @return 返回签名后的字符串 
     * @throws Exception 
     */  
    public static String sign(String plainText,String path,String password)  
            throws Exception  {  
        /* 
         * MD5加密 
         */  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        md5.update(plainText.getBytes("utf-8"));  
        byte[] digestBytes = md5.digest();  
        /* 
         * 用私钥进行签名 RSA 
         * Cipher负责完成加密或解密工作，基于RSA 
         */  
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
        //ENCRYPT_MODE表示为加密模式  
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(path, password));  
        //加密  
        byte[] rsaBytes = cipher.doFinal(digestBytes);  
        //Base64编码  
        return Base64.byteArrayToBase64(rsaBytes);  
    }
    
    
    /**  
     * 根据公钥n、e生成公钥 
     * @param modulus   公钥n串 
     * @param publicExponent  公钥e串 
     * @return 返回公钥PublicKey 
     * @throws Exception 
     */  
    public static PublicKey getPublicKey(String modulus, String publicExponent)  
            throws Exception {  
        KeySpec publicKeySpec = new RSAPublicKeySpec(  
                new BigInteger(modulus, 16), new BigInteger(publicExponent, 16));  
        KeyFactory factory = KeyFactory.getInstance("RSA");  
        PublicKey publicKey = factory.generatePublic(publicKeySpec);  
        return publicKey;  
    }  
    
    
    /** 
     * 用公钥证书进行验签 
     * @param message  签名之前的原文 
     * @param cipherText  签名 
     * @param pubKeyn 公钥n串 
     * @param pubKeye 公钥e串 
     * @return boolean 验签成功为true,失败为false 
     * @throws Exception 
     */  
    public static boolean verify(String message, String cipherText,String pubKeyn,  
            String pubKeye) throws Exception {  
        Cipher c4 = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
        // 根据密钥，对Cipher对象进行初始化,DECRYPT_MODE表示解密模式  
        c4.init(Cipher.DECRYPT_MODE, getPublicKey(pubKeyn,pubKeye));  
        // 解密  
        byte[] desDecTextBytes = c4.doFinal(Base64.base64ToByteArray(cipherText));  
        // 得到前置对原文进行的MD5  
        String md5Digest1 = Base64.byteArrayToBase64(desDecTextBytes);  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        md5.update(message.getBytes("utf-8"));  
        byte[] digestBytes = md5.digest();  
        // 得到商户对原文进行的MD5  
        String md5Digest2 = Base64.byteArrayToBase64(digestBytes);  
        // 验证签名  
        if (md5Digest1.equals(md5Digest2)) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    
    /** 
     * 用公钥证书进行验签 
     * @param message  签名之前的原文 
     * @param cipherText  签名 
     * @param pubKey	公钥
     * @return boolean 验签成功为true,失败为false 
     * @throws Exception 
     */  
    public static boolean verify(String message, String cipherText,PublicKey pubKey) throws Exception {  
        Cipher c4 = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
        // 根据密钥，对Cipher对象进行初始化,DECRYPT_MODE表示解密模式  
        c4.init(Cipher.DECRYPT_MODE, pubKey);  
        // 解密  
        byte[] desDecTextBytes = c4.doFinal(Base64.base64ToByteArray(cipherText));  
        // 得到前置对原文进行的MD5  
        String md5Digest1 = Base64.byteArrayToBase64(desDecTextBytes);  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        md5.update(message.getBytes("utf-8"));  
        byte[] digestBytes = md5.digest();  
        // 得到商户对原文进行的MD5  
        String md5Digest2 = Base64.byteArrayToBase64(digestBytes);  
        // 验证签名  
        if (md5Digest1.equals(md5Digest2)) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    
    
    
    /** 
     * 读取公钥cer 
     * @param path .cer文件的路径  如：c:/abc.cer 
     * @return  base64后的公钥串 
     * @throws IOException 
     * @throws CertificateException 
     */  
    public static String getPublicKey(String path) throws IOException,  
    CertificateException{  
        InputStream inStream = new FileInputStream(path);  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int ch;  
        String res = "";  
        while ((ch = inStream.read()) != -1) {  
            out.write(ch);  
        }  
        byte[] result = out.toByteArray();  
        res = Base64.byteArrayToBase64(result);  
        return res;  
    }  
    
    
    public static void main(String [] args) throws Exception{
    	String plainText = "cyj_test";
    	String pathPrivateKey = "d:/superhard.pfx";
    	String password = "password";
    	String stringEncrypt = sign(plainText, pathPrivateKey, password);
    	String pathPublicKey = "d:/superhard.cer";
    	PublicKey publicKey = getX509CerCate(pathPublicKey) ;
    	
//    	String stringDecrypt = 
    	boolean result = verify(plainText, stringEncrypt, publicKey);
    	System.out.println("验证结果----" + result);
    }
}
