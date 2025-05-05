package edu.nesl.maxient;

import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

//import org.bouncycastle.util.io.pem.PemObject;
//import org.bouncycastle.util.io.pem.PemReader;

public class KeyReader {
//
//    public static PublicKey readPublicKey(String filePath) throws Exception {
//        PemReader pemReader = new PemReader(new FileReader(new File(filePath)));
//        PemObject pemObject = pemReader.readPemObject();
//        pemReader.close();
//
//        byte[] keyBytes = pemObject.getContent();
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePublic(spec);
//    }
//
//    public static PrivateKey readPrivateKey(String filePath) throws Exception {
//        PemReader pemReader = new PemReader(new FileReader(new File(filePath)));
//        PemObject pemObject = pemReader.readPemObject();
//        pemReader.close();
//
//        byte[] keyBytes = pemObject.getContent();
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePrivate(spec);
//    }

    public static void main(String[] args) throws Exception {
        //PublicKey pubKey = readPublicKey("path/to/public_key.pem");
        //PrivateKey privKey = readPrivateKey("path/to/private_key.pem");

        //System.out.println("Public Key: " + Base64.getEncoder().encodeToString(pubKey.getEncoded()));
        //System.out.println("Private Key: " + Base64.getEncoder().encodeToString(privKey.getEncoded()));
    }
}

