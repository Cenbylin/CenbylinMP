package cn.cenbylin.mp.common;

import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class SysListener implements ServletContextListener{
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	public void contextInitialized(ServletContextEvent event) {
		//触发中控
		while (InfoProvider.getAccessToken()==null ) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("【zhuchao】："+ InfoProvider.getAccessToken());
	}
}