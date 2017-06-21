package com.superhard.lbsw.cer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

public class PublicCert {

	static {
        Security.addProvider(new BouncyCastleProvider());
    }
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
     * 产生数字公钥证书
     * String[] info长度为9，分别是{cn,ou,o,c,l,st,starttime,endtime,serialnumber}
     * @throws SignatureException 
     * @throws SecurityException 
     * @throws NoSuchProviderException 
     * @throws InvalidKeyException 
     */
    public X509Certificate generateCert(String[] info, KeyPair keyPair_root,KeyPair keyPair_user) throws InvalidKeyException, NoSuchProviderException, SecurityException, SignatureException {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        X509Certificate cert = null;
        certGen.setSerialNumber(new BigInteger(info[8]));
        certGen.setIssuerDN(new X509Name(
                "CN=superhard, OU=superhard, O=superhard , C=china"));
        certGen.setNotBefore(new Date(Long.parseLong(info[6])));
        certGen.setNotAfter(new Date(Long.parseLong(info[7])));
        certGen.setSubjectDN(new X509Name("C=" + info[0] + ",OU=" + info[1]
                + ",O=" + info[2] + ",C=" + info[3] + ",L=" + info[4] + ",ST="
                + info[3]));
        certGen.setPublicKey(keyPair_user.getPublic());
        certGen.setSignatureAlgorithm("SHA1WithRSA");
        cert = certGen.generateX509Certificate(keyPair_root.getPrivate(), "BC");
        return cert;
    }
    /**
     * 在D盘产生公钥数字证书了
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     * @throws SecurityException
     * @throws SignatureException
     * @throws CertificateEncodingException
     * @throws IOException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SecurityException, SignatureException, CertificateEncodingException, IOException{
    	PublicCert myCert = new PublicCert();
        KeyPair keyPair_root = myCert.generateKeyPair(10);
        KeyPair keyPair_user = myCert.generateKeyPair(100);
        String[] info = {"superhard","superhard","superhard","china","shanghai","shanghai","111111","11111111","88888888"};
        X509Certificate cert = myCert.generateCert(info, keyPair_root, keyPair_user);
        String certPath = "d:/"+info[0]+".cer";
        FileOutputStream fos = new FileOutputStream(certPath);
        fos.write(cert.getEncoded());
        fos.close();
    }
}
