package edu.nesl.maxient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyPairGenerator {

    public static void main(String[] args) {
        try {
            // 1. Get a KeyPairGenerator instance for the desired algorithm (e.g., RSA)
            java.security.KeyPairGenerator keyPairGenerator = java.security.KeyPairGenerator.getInstance("RSA");

            // 2. Initialize the KeyPairGenerator (optional, but recommended)
            // You can specify the key size (in bits) and a SecureRandom instance
            keyPairGenerator.initialize(2048, new SecureRandom()); // 2048 bits is a common key size for RSA

            // 3. Generate the KeyPair
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 4. Access the public and private keys
            java.security.PublicKey publicKey = keyPair.getPublic();
            java.security.PrivateKey privateKey = keyPair.getPrivate();

            // Now you can use the publicKey and privateKey for encryption/decryption or signing/verification
            try {
                try (FileOutputStream fos = new FileOutputStream("public.key")) {
                    fos.write(publicKey.getEncoded());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Private Key: " + privateKey.toString());

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm not supported: " + e.getMessage());
        }
    }
}
