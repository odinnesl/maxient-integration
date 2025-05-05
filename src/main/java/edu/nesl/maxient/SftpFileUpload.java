package edu.nesl.maxient;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SftpFileUpload {

    private static final Logger logger = LoggerFactory.getLogger(SftpFileUpload.class);

    private static String host = ReadProperties.read("feed.string");

    public void upload() throws IOException {
        SshClient client = SshClient.setUpDefaultClient();

        KeyLoader keyLoader = new KeyLoader();

        try {

            String keyStorePath = "C:\\Users\\odin_ga\\mykeystore";
            String keyStorePassword = "metal4309$";
            String keyStoreType = "JKS"; // or "PKCS12"

            try (FileInputStream fis = new FileInputStream(keyStorePath)) {
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(fis, keyStorePassword.toCharArray());


                String alias = "my_alias"; // Alias of the key
                String keyPassword = "metal4309$"; // Password for the specific key (may be the same as the keystore password)

                try {
                    java.security.Key key = keyStore.getKey(alias, keyPassword.toCharArray());
                    Certificate certificate = keyStore.getCertificate(alias);
                    PublicKey publicKey = certificate.getPublicKey();

                    if (key instanceof java.security.PrivateKey) {
                        java.security.PrivateKey privateKey = (java.security.PrivateKey) key;
                        // Use the privateKey

                        client.addPublicKeyIdentity(new KeyPair(publicKey, privateKey));
                    }
                } catch (Exception e) {
                    // Handle exceptions (e.g., NoSuchAlgorithmException, UnrecoverableKeyException, etc.)
                    e.printStackTrace();
                }


            } catch (Exception e) {
                // Handle exceptions (e.g., FileNotFoundException, KeyStoreException, etc.)
                e.printStackTrace();
            }






            //PublicKey publicKey = keyLoader.loadPublicKey("C:\\Users\\odin_ga\\public_key.pem", "RSA");
            //PrivateKey privateKey = keyLoader.loadPrivateKey("C:\\Users\\odin_ga\\nesl-api-privkey", "RSA");
            //client.addPublicKeyIdentity(new KeyPair(publicKey, privateKey));
        } catch (Exception e) {
            logger.error("Error with reading keys:", e);
            throw new RuntimeException();
        }
        client.start();

        try (ClientSession session = client.connect("nesl", host,22).verify().getSession()) {
            //session.addPasswordIdentity("password");
            session.auth().verify();

            try (SftpClient sftpClient = SftpClientFactory.instance().createSftpClient(session)) {
                final Path demographicFilePath = Paths.get("NESL_DEMOGRAPHICS_DATA.txt");

                // final String remotePath = "/remote/path/remote_file.txt";
                final String remotePath = "/incoming";

                // Ensure remote directory exists
                try {
                    sftpClient.mkdir(Paths.get(remotePath).getParent().toString());
                    logger.info("Created directory");
                } catch (final IOException e) {
                    // Directory might already exist, check for specific exception if needed
                    logger.error("Error with remote directory", e);
                }

                try (final FileInputStream fis = new FileInputStream(demographicFilePath.toFile())) {
                    sftpClient.put(fis, remotePath); //TODO check if it is put or write
                    logger.info("Put file:{}", demographicFilePath);
                    // sftpClient.upload(fis, remotePath);
                }
                logger.info("Schedule file:{} uploaded to remote path:{}", demographicFilePath, remotePath);

                final Path scheduleFilePath = Paths.get("NESL_SCHEDULES_DATA.txt");

                try (final FileInputStream fis = new FileInputStream(scheduleFilePath.toFile())) {
                    sftpClient.put(fis, remotePath); //TODO check if it is put or write
                    logger.info("Put file:{}", remotePath);
                    // sftpClient.upload(fis, remotePath);
                }
                logger.info("Demographic file:{} uploaded to remote path:{}", demographicFilePath, remotePath);
            }
        } finally {
            client.stop();
            logger.info("SFTP client stopped successfully");
        }
    }
}