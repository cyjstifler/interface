package com.superhard.lbsw.cer;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class GeneratePfxByJks {
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
