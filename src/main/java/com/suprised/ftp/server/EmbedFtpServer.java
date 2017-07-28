package com.suprised.ftp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;


/**
 * 内嵌ftp服务器启动类
 */
public class EmbedFtpServer {
    
    private static EmbedFtpServer embedServer = new EmbedFtpServer();
    public static final String DEF_FTP_PASS = "ftpserver_dascom";
    public static final String DEF_FTP_NAME = "内嵌上传服务";
    // public String defUserPrefix = "suite";
    
    private FtpServerFactory serverFactory;
    private FtpServer server = null;
    private UserManager um = null;
    
    private ReentrantLock lock = new ReentrantLock();
    
    private EmbedFtpServer() {
        int port = 10021; 
        serverFactory = new FtpServerFactory();
        
        // 端口
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);
        DataConnectionConfigurationFactory configFactory = new DataConnectionConfigurationFactory();
        // ftp被动模式的端口范围
        String passivePortScope = "10022-10222";
        configFactory.setPassivePorts(passivePortScope);
        
        DataConnectionConfiguration dataConnectionConfig = configFactory.createDataConnectionConfiguration();
        factory.setDataConnectionConfiguration(dataConnectionConfig);
        
        // factory.setServerAddress("192.168.1.109");
        serverFactory.addListener("default", factory.createListener());
        
        /*this.server = serverFactory.createServer();
        // 使用属性文件来保存用户
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        this.um = userManagerFactory.createUserManager();
        serverFactory.setUserManager(um);*/
    }
    
    /**
     * 获取内嵌服务
     */
    public synchronized static final EmbedFtpServer getEmbedServer() {
        return embedServer;
    }
    
    /**
     * 重启服务
     */
    public void restart(List<ServiceInstanceBean> services) throws Exception {
        if (this.server != null && isRunning()) {
            this.server.stop();
            this.server = null;
        }
        // connectionco
        // serverFactory.setConnectionConfig(connectionConfig);
        lock.lock();
        try {
            this.server = serverFactory.createServer();
            
            // 使用属性文件来保存用户
            PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
            this.um = userManagerFactory.createUserManager();
            this.serverFactory.setUserManager(um);
            
            for (ServiceInstanceBean service : services) {
                this.addUser(service.getAccount(), service.getPassword(), service.getResourcePath());
            }
            
            this.server.start();
        } finally {
            lock.unlock();
        }
    }
    

    /**
     * 启动服务
     */
    /*public void start() throws Exception {
        // 默认账号
        this.addUser("suite", DEF_FTP_PASS, "D:\\files-management");
        server.start();
    }*/
    
    /**
     * 是否运行中
     */
    public boolean isRunning() {
        return (server.isStopped() || server.isSuspended()) ? false : true;
    }
    
    /**
     * 添加一个ftp账号
     * 
     * @param userName ftp账号
     * @param password ftp密码
     * @param storagePath 存储方案路径
     */
    private void addUser(String userName, String password, String storagePath) throws Exception {
        BaseUser user = (BaseUser) um.getUserByName(userName);
        if (user == null) {
            user = new BaseUser();
            user.setEnabled(true);
            List<Authority> authoritys = new ArrayList<Authority>();
            authoritys.add(new WritePermission());
            user.setAuthorities(authoritys);
            user.setName(userName);
            user.setPassword(password);
            user.setHomeDirectory(storagePath);
        } else {
            user.setHomeDirectory(storagePath);
            user.setPassword(password);
        }
        um.save(user);
    }
    
    /**
     * 添加一个ftp账号
     * 
     * @param storagePath 存储方案路径
     */
    /*public void addUser(String storagePath) throws Exception {
        // 需要创建默认的用户名密码，不能和已有的重复
        String defaultUserName = "";
        String defaultPassword = "";
        this.addUser(defaultUserName, defaultPassword, storagePath);
    }*/
}
