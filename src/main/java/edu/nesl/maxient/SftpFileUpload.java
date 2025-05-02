package edu.nesl.maxient;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SftpFileUpload {

    private static final Logger logger = LoggerFactory.getLogger(SftpFileUpload.class);

    private static String host = ReadProperties.read("feed.string");

    public static void main(String[] args) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();
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