package edu.nesl.maxient;

import com.jcraft.jsch.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class JSchSftpUpload {

    private static final Logger logger = LoggerFactory.getLogger(JSchSftpUpload.class);

    public static void main(String args[]) {
        final JSchSftpUpload JSchSftpUpload = new JSchSftpUpload();
        JSchSftpUpload.upload("");
    }

    /**
     * Uploads data to SFTP end point
     */
    public void upload(final String fileContent) {

        // Externalize to Maxient or provide as config

        final String privateKeyPath = "";
        final String host = "your_sftp_host";
        final String username = "your_username";
        // final String password = "your_password";
        int port = 22;

        final String remoteFilePath = "/remote/path/new_file.txt";

        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            final JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);
            session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            final Channel channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            final InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
            channelSftp.put(inputStream, remoteFilePath);

            logger.info("File uploaded successfully to path:{}", remoteFilePath);

        } catch (JSchException | SftpException e) {
            logger.error("Error with SFTP file upload:", e);
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
                logger.info("SFTP channel succesfully disconnected");
            }
            if (session != null) {
                session.disconnect();
                logger.info("Session with SFTP server successfully disconnected");
            }
        }
    }
}