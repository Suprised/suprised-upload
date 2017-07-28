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
 * ��Ƕftp������������
 */
public class EmbedFtpServerTest {

    public static void main(String[] args) throws Exception {
        
        FtpServerFactory serverFactory = new FtpServerFactory();
        FtpServer server = serverFactory.createServer();
        // �˿�
        ListenerFactory factory = new ListenerFactory();
        
        factory.setPort(2221);
        factory.setServerAddress("192.168.1.109");
        //factory.set
        
        NioListener listener = (NioListener) factory.createListener();
        // listener.
        
        // DataConnectionConfiguration config = listener.getDataConnectionConfiguration();
        
        // factory.set
        serverFactory.addListener("default", listener);
        
        // �����û���
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        // userManagerFactory.setFile(new File("myusers.properties"));
        // �����û���
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
        
        // �����û���Ϣ��ʹ�����µ�����
        /*BaseUser updateUser = (BaseUser) um.getUserByName("suite");
        updateUser.setHomeDirectory("D:/ftpserver/root2");
        updateUser.setEnabled(true);
        um.save(updateUser);*/
        
        // start the server
        server.start();
    }
    
    
    
}
