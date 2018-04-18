package com.acooly.core.utils.net;

import com.acooly.core.common.exception.BusinessException;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

/**
 * sftp 处理类
 *
 * <p>如果不重用连接请使用: {@link SFTP#download(java.lang.String, int, java.lang.String, java.lang.String,
 * java.lang.String, java.lang.String)}
 *
 * <p>如果要重用连接,请按照下面的步骤:
 *
 * <ul>
 * <li>1.创建Session {@link SFTP#createSession(java.lang.String, int, java.lang.String,
 * java.lang.String)}
 * <li>2.创建channel {@link SFTP#createChannel(com.jcraft.jsch.Session)}
 * <li>3.下载 {@link SFTP#download(com.jcraft.jsch.ChannelSftp, java.lang.String, java.lang.String)}
 * <li>4.关闭channel {@link SFTP#closeChannel(com.jcraft.jsch.ChannelSftp)}
 * <li>5.关闭session {@link SFTP#closeSession(com.jcraft.jsch.Session)}
 * <li>.上传 {@link SFTP#upload(com.jcraft.jsch.ChannelSftp, java.lang.String, java.lang.String)}
 * </ul>
 * <p>
 * 可以重用session,每次创建channel执行下载任务,在程序关闭时关闭sesison <br>
 * <br>
 * 如果要使用其他命令,可以使用 {@link SFTP#exeSftpCmd(java.lang.String, int, java.lang.String, java.lang.String,
 * com.acooly.core.utils.net.SFTP.SFTPCommand)} 来做其他操作. <br>
 */
@Slf4j
public class SFTP {

    /**
     * 创建session
     *
     * @param host     目标地址
     * @param port     端口
     * @param userName 用户名
     * @param password 密码
     * @return Session
     */
    public static Session createSession(String host, int port, String userName, String password) {
        JSch jsch = new JSch();
        JSch.setLogger(getLogger());
        try {
            Session session = jsch.getSession(userName, host, port);
            session.setUserInfo(new SFTPUserInfo(password));
            session.connect();
            return session;
        } catch (JSchException e) {
            throw new BusinessException(e);
        }
    }

    public static Logger getLogger() {
        return new Logger() {
            org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SFTP.class);

            @Override
            public boolean isEnabled(int level) {
                return true;
            }

            @Override
            public void log(int level, String message) {
                if (level == Logger.INFO) {
                    logger.info(message);
                } else if (level == Logger.DEBUG) {
                    logger.debug(message);
                } else if (level == Logger.WARN) {
                    logger.warn(message);
                } else if (level == Logger.ERROR) {
                    logger.error(message);
                } else if (level == Logger.FATAL) {
                    logger.error(message);
                }
            }
        };
    }

    /**
     * 创建ChannelSftp
     *
     * @param session Session
     * @return ChannelSftp
     */
    public static ChannelSftp createChannel(Session session) {
        try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            return (ChannelSftp) channel;
        } catch (JSchException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 从sftp服务器指定目录下载文件到本地目录
     *
     * @param channelSftp ChannelSftp对象
     * @param src         sftp上文件路径
     * @param dst         本地目录
     */
    public static void download(ChannelSftp channelSftp, String src, String dst) {
        try {
            channelSftp.get(src, dst, null, ChannelSftp.OVERWRITE);
        } catch (SftpException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 从sftp服务器指定目录下载文件到本地目录
     *
     * @param host     目标地址
     * @param port     端口
     * @param userName 用户名
     * @param password 密码
     * @param src      sftp上文件路径
     * @param dst      本地目录
     */
    public static void download(
            String host, int port, String userName, String password, final String src, final String dst) {
        exeSftpCmd(
                host,
                port,
                userName,
                password,
                channelSftp -> {
                    try {
                        channelSftp.get(src, dst, null, ChannelSftp.OVERWRITE);
                    } catch (SftpException e) {
                        throw new BusinessException(e);
                    }
                });
    }

    /**
     * 上传文件到sftp服务器指定目录下
     *
     * @param host     目标地址
     * @param port     端口
     * @param userName 用户名
     * @param password 密码
     * @param src      本地目录
     * @param dst      sftp上文件路径
     */
    public static void upload(
            String host, int port, String userName, String password, final String src, final String dst) {
        exeSftpCmd(
                host,
                port,
                userName,
                password,
                channelSftp -> {
                    try {
                        channelSftp.put(src, dst, null, ChannelSftp.OVERWRITE);
                    } catch (SftpException e) {
                        throw new BusinessException(e);
                    }
                });
    }

    /**
     * 上传文件到sftp服务器指定目录下
     *
     * @param channelSftp ChannelSftp对象
     * @param src         本地目录
     * @param dst         sftp上文件路径
     */
    public static void upload(ChannelSftp channelSftp, String src, String dst) {
        try {
            channelSftp.get(src, dst, null, ChannelSftp.OVERWRITE);
        } catch (SftpException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 连接到目标服务器执行command
     *
     * @param host     目标地址
     * @param port     端口
     * @param userName 用户名
     * @param password 密码
     * @param command  操作命令
     */
    public static void exeSftpCmd(
            String host, int port, String userName, String password, SFTPCommand command) {
        if (command != null) {
            Session session = null;
            ChannelSftp channelSftp = null;
            try {
                session = createSession(host, port, userName, password);
                channelSftp = createChannel(session);
                if (channelSftp != null) {
                    command.exe(channelSftp);
                }
            } finally {
                try {
                    closeChannel(channelSftp);
                } finally {
                    closeSession(session);
                }
            }
        }
    }

    /**
     * 关闭channel
     *
     * @param channelSftp ChannelSftp
     */
    public static void closeChannel(ChannelSftp channelSftp) {
        if (channelSftp != null) {
            channelSftp.exit();
        }
    }

    /**
     * 关闭session
     *
     * @param session Session
     */
    public static void closeSession(Session session) {
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * sftp命令
     */
    public interface SFTPCommand {
        void exe(ChannelSftp channelSftp);
    }

    /**
     * 处理密码输入
     */
    private static class SFTPUserInfo implements UserInfo {
        private String password;

        public SFTPUserInfo(String password) {
            this.password = password;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return this.password;
        }

        @Override
        public boolean promptPassword(String s) {
            return true;
        }

        @Override
        public boolean promptPassphrase(String s) {
            return true;
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;
        }

        @Override
        public void showMessage(String s) {
        }
    }
}
