package com.superhard.lbsw.cer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class ConvertToPFX {

	public static final String PKCS12 = "PKCS12";
    public static  String KEYSTORE_PASSWORD = "password";
    public void toPFX(String[] info){
        try {
            String pfx_keystore_file = "D:/"+info[0]+".pfx";
            String jkx_keystore_file = "D:/"+info[0]+".jks";
            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(jkx_keystore_file);
            char[] nPassword = null;
            if ((KEYSTORE_PASSWORD == null)
                    || KEYSTORE_PASSWORD.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = KEYSTORE_PASSWORD.toCharArray();
            }
            inputKeyStore.load(fis, nPassword);
            fis.close();
            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12");
            outputKeyStore.load(null, KEYSTORE_PASSWORD.toCharArray());
            Enumeration enums = inputKeyStore.aliases();
            while (enums.hasMoreElements()) { 
                String keyAlias = (String) enums.nextElement();
                System.out.println("alias=[" + keyAlias + "]");
                if (inputKeyStore.isKeyEntry(keyAlias)) {
                    Key key = inputKeyStore.getKey(keyAlias, nPassword);
                    java.security.cert.Certificate[] certChain = inputKeyStore
                            .getCertificateChain(keyAlias);
                    outputKeyStore.setKeyEntry(keyAlias, key, KEYSTORE_PASSWORD
                            .toCharArray(), certChain);
                }
            }
            FileOutputStream out = new FileOutputStream(pfx_keystore_file);
            outputKeyStore.store(out, nPassword);
            out.close();
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
        
//        String[] info = {"superhard","superhard","superhard","china","shanghai","shanghai","111111","11111111","88888888"};
        ConvertToPFX ctf = new ConvertToPFX();
        ctf.toPFX(info);
    }
    
}
