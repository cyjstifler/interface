package com.superhard.lbsw.cer;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.x509.X509V3CertificateGenerator;

public class MyJKS {
	/**
     * 根据seed产生密钥对
     * @param seed
     * @return
     * @throws NoSuchAlgorithmException
     */
    public KeyPair generateKeyPair(int seed) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024, new SecureRandom(new byte[seed]));
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }


    /**
     * 创建空的jks文件
     * String[] info长度为9，分别是{cn,ou,o,c,l,st,starttime,endtime,serialnumber}
     */
    public void generateJKS(String[] info){
        try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(null,null);
            keyStore.store(new FileOutputStream("D:/"+info[0]+".jks"), "password".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用空的jks创建自己的jks
     *  String[] info长度为9，分别是{cn,ou,o,c,l,st,starttime,endtime,serialnumber}
     */
    public void storeJKS(String[] info,KeyPair keyPair_root,KeyPair keyPair_user){
        KeyStore keyStore;
        try {
            //use exited jks file
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("D:/"+info[0]+".jks"),
                    "password".toCharArray());
            //generate user's keystore by info[8]  -----keypair
            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            certGen.setSerialNumber(new BigInteger(info[8]));
            certGen.setIssuerDN(new X509Name(
                    "CN=superhard, OU=superhard, O=superhard , C=china"));
            certGen.setNotBefore(new Date(Long.parseLong(info[6])));
            certGen.setNotAfter(new Date(Long.parseLong(info[7])));
            certGen.setSubjectDN(new X509Name(
                    "C="+info[0]+",OU="+info[1]+",O="+info[2]+",C="+info[3]+",L="+info[4]+",ST="+info[3]));
            certGen.setPublicKey(keyPair_user.getPublic());
            certGen.setSignatureAlgorithm("SHA1WithRSA");
            X509Certificate cert = null;
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
            cert = certGen.generateX509Certificate( keyPair_root.getPrivate(), "BC");
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = cert;
            keyStore.setKeyEntry("mykey", keyPair_user.getPrivate(), "password".toCharArray(), chain);
            keyStore.setCertificateEntry("single_cert", cert);
            keyStore.store(new FileOutputStream("D:/"+info[0]+".jks"),
                    "password".toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }       
    }

    /** 
     * @author God 
     * @cerPath Java读取Cer证书信息 
     * @throws Exception  
     * @return X509Cer对象 
     */  
    public static X509Certificate getX509CerCate(String cerPath) throws Exception {  
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
        return x509Certificate;  
    }  
    
    
    public static void main(String[] args) throws NoSuchAlgorithmException,Exception{
    	PublicCert myCert = new PublicCert();
        KeyPair keyPair_root = myCert.generateKeyPair(10);
        KeyPair keyPair_user = myCert.generateKeyPair(100);
        String[] info = {"superhard","superhard","superhard","china","shanghai","shanghai","111111","11111111","88888888"};
        X509Certificate cert = myCert.generateCert(info, keyPair_root, keyPair_user);
        String certPath = "d:/"+info[0]+".cer";
        FileOutputStream fos = new FileOutputStream(certPath);
        fos.write(cert.getEncoded());
        fos.close();
        
//        MyJKS myJks = new MyJKS();
//        String[] info = {"superhard","superhard","superhard","china","shanghai","shanghai","111111","11111111","88888888"};
//        X509Certificate cert = getX509CerCate("d:\\huahua_user.cer");  
//        KeyPair keyPair_root = myJks.generateKeyPair(10);
//        KeyPair keyPair_user = myJks.generateKeyPair(100);
        MyJKS myJKS = new MyJKS();
        myJKS.generateJKS(info);
        myJKS.storeJKS(info, keyPair_root, keyPair_user);
    }   
}
