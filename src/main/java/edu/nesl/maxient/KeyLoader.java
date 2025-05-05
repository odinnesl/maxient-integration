package edu.nesl.maxient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class KeyLoader {

    // Helper method to remove PEM headers and footers robustly.
    private static String removePemHeaders(String keyPEM) {
        StringBuilder sb = new StringBuilder();
        // Split the PEM file into separate lines.
        String[] lines = keyPEM.split("\\r?\\n");
        for (String line : lines) {
            // Skip any line that starts with dashes (header or footer)
            if (!line.startsWith("-----")) {
                sb.append(line.trim());
            }
        }
        return sb.toString();
    }

    // Method to load a public key from a PEM file on the filesystem
    public PublicKey loadPublicKey(String filePath, String algorithm) throws Exception {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("Public key file not found: " + filePath);
        }
        String keyPEM = new String(Files.readAllBytes(path));
        // Remove PEM headers/footers and whitespace
        keyPEM = removePemHeaders(keyPEM);
        keyPEM = keyPEM.replaceAll("\\s", ""); // Remove all whitespace
        keyPEM = keyPEM.replaceAll("ssh-rsa ", ""); // Remove "ssh-rsa " if present
        keyPEM = keyPEM.replaceAll(".*@", ""); // Remove anything after @

        byte[] keyBytes = Files.readAllBytes(path);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(spec);
    }

    // Method to load a private key from a PEM file on the filesystem
    public PrivateKey loadPrivateKey(String filePath, String algorithm) throws Exception {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("Private key file not found: " + filePath);
        }
        String keyPEM = new String(Files.readAllBytes(path));
        // Remove PEM headers/footers and whitespace
        keyPEM = removePemHeaders(keyPEM);
        byte[] keyBytes = Base64.getDecoder().decode(keyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(spec);
    }

    public static void main(String[] args) {
        // Ensure the user provides file paths for both keys
        if (args.length < 2) {
            System.err.println("Usage: java KeyLoader <public_key_file> <private_key_file>");
            System.exit(1);
        }

        String publicKeyPath = args[0];
        String privateKeyPath = args[1];

        try {
            // Replace "RSA" with your algorithm if needed
            //PublicKey publicKey = loadPublicKey(publicKeyPath, "RSA");
            //PrivateKey privateKey = loadPrivateKey(privateKeyPath, "RSA");

            System.out.println("Successfully loaded keys from the filesystem.");
            //System.out.println("Public Key: " + publicKey);
            //System.out.println("Private Key: " + privateKey);
        } catch (Exception e) {
            System.err.println("Error loading keys: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

