package com.suprised.ftp.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.listener.nio.NioListener;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

/**
 * 内嵌ftp服务器启动类
 */
public class EmbedFtpServerTest {

    public static void main(String[] args) throws Exception {
        
        FtpServerFactory serverFactory = new FtpServerFactory();
        FtpServer server = serverFactory.createServer();
        // 端口
        ListenerFactory factory = new ListenerFactory();
        
        factory.setPort(2221);
        factory.setServerAddress("192.168.1.109");
        //factory.set
        
        NioListener listener = (NioListener) factory.createListener();
        // listener.
        
        // DataConnectionConfiguration config = listener.getDataConnectionConfiguration();
        
        // factory.set
        serverFactory.addListener("default", listener);
        
        // 设置用户名
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        // userManagerFactory.setFile(new File("myusers.properties"));
        // 创建用户名
        UserManager um = userManagerFactory.createUserManager();
        serverFactory.setUserManager(um);
        
        BaseUser user = new BaseUser();
        user.setEnabled(true);
        List<Authority> authoritys = new ArrayList<Authority>();
        authoritys.add(new WritePermission());
        user.setAuthorities(authoritys);
        user.setName("suite");
        user.setPassword("dascom");
        // user.setHomeDirectory("D:\\files-management");
        user.setHomeDirectory("D:/ftpserver/root");
        
        
        
        um.save(user);
        
        // 更新用户信息后，使用最新的设置
        /*BaseUser updateUser = (BaseUser) um.getUserByName("suite");
        updateUser.setHomeDirectory("D:/ftpserver/root2");
        updateUser.setEnabled(true);
        um.save(updateUser);*/
        
        // start the server
        server.start();
    }
    
    
    
}
