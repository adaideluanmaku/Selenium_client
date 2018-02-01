package com.ch.selenium;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.util.StringUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.ch.dao.Mysqljdbc;

public class Selenium2 {
	public String autoone(int testid , int userid) throws ClassNotFoundException, SQLException, IOException{
		WebDriver driver=null;
		List list=null;
		Map resultmap=new HashMap();
		Map scriptmap=null;
		String sql=null;
		String result=null;
		
		Mysqljdbc mysqljdbc=new Mysqljdbc();
		Connection conn=mysqljdbc.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		// 启动浏览器
		sql="select browserpath from sys_users where userid=?";
		try{
			pst=conn.prepareStatement(sql);
			pst.setInt(1, userid);
			rs=pst.executeQuery();
			list=mysqljdbc.getlist(rs);
		}catch (Exception e){
			System.out.println("用户表获取数据失败");
			return "用户表获取数据失败";
		}
		
		scriptmap=(Map)list.get(0);
		if("".equals(scriptmap.get("browserpath"))){
			return "本地浏览器地址为空";
		}
		try{
			if(scriptmap.get("browserpath").toString().contains("IEDriverServer")){
				System.setProperty("webdriver.ie.driver", scriptmap.get("browserpath").toString());
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				driver = new InternetExplorerDriver(ieCapabilities);
			}
			if(scriptmap.get("browserpath").toString().contains("firefox")){
				System.setProperty("webdriver.firefox.bin", scriptmap.get("browserpath").toString());
				driver = new FirefoxDriver();
				
			}
			if(scriptmap.get("browserpath").toString().contains("chromedriver")){
				System.setProperty("webdriver.chrome.driver", scriptmap.get("browserpath").toString());
				driver = new ChromeDriver();
			}
		}catch(Exception e){
			return "浏览器调用失败";
		}
		
		// 全局设置延迟，如果操作无响应，则等待最多10S
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// 浏览器最大化
		 driver.manage().window().maximize();

		//开始测试
		sql="update testmng set testresult='' where testid=?";
		pst=conn.prepareStatement(sql);
		pst.setInt(1, testid);
		pst.execute();
			
		//获取案例脚本
		sql="select a.*,b.testno,b.testname from script a, testmng b where a.testid=b.testid and a.testid=? order by a.step asc";
		try{
			pst=conn.prepareStatement(sql);
			pst.setInt(1, testid);
			rs=pst.executeQuery();
			list=mysqljdbc.getlist(rs);
		}catch (Exception e){
			System.err.println("更新脚本表失败");
		}
		
		if(list.size()==0){
			sql="update testmng set testresult=? where testid=?";
			try{
				pst=conn.prepareStatement(sql);
				pst.setString(1, "未找到测试脚本");
				pst.setInt(2, testid);
				pst.executeUpdate();
				rs.close();
				pst.close();
				conn.close();
			}catch (Exception e){
				System.out.println("更新案例表失败");
			}
			
			driver.close();
			return "未找到测试脚本";
		}
		
		//客户端从这里开始复制代码
		try{
			//循环脚本
			for(int i=0;i<list.size();i++){
				scriptmap=(Map)list.get(i);
				System.out.println("开始执行selenium脚本第"+scriptmap.get("step")+"步");
				
				//是否输入新网址
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==0){
					if(StringUtil.isBlank(scriptmap.get("testurl").toString())){
						return result="第"+scriptmap.get("step")+"步，测试异常失败";
					}
					// 浏览器输入地址
					driver.get(scriptmap.get("testurl").toString());
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				
				//是否为输入的脚本
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==1){
					driver.findElement(By.xpath(scriptmap.get("xpath").toString())).clear();
					driver.findElement(By.xpath(scriptmap.get("xpath").toString())).sendKeys(scriptmap.get("testvalue").toString());	
				}
				
				//是否为批量输入的脚本
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==2){
					List<WebElement> inputs=driver.findElements(By.xpath(scriptmap.get("xpath").toString()));
					for(int k=0;k<inputs.size();k++){
						WebElement input=inputs.get(k);
						if(input.getAttribute("type").equals("hidden")){
							continue;
						}else if(input.getAttribute("type").equals("radio")){
							input.click();
						}else{
							input.clear();
							input.sendKeys(scriptmap.get("testvalue").toString());
						}
					}	
				}
				
				//是否为iframe页面
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==3){
					WebElement iframe = driver.findElement(By.xpath(scriptmap.get("xpath").toString()));
			        driver.switchTo().frame(iframe);
				}
				
				//是否为点击事件
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==88){
					driver.findElement(By.xpath(scriptmap.get("xpath").toString())).click();	
				}
				
				//是否为断言,全等比较
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==98){
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					result=driver.findElement(By.xpath(scriptmap.get("xpath").toString())).getText();
					if("".equals(result)){
						result=driver.findElement(By.xpath(scriptmap.get("xpath").toString())).getAttribute("value");
					}
					System.out.println("页面获取测试对象是：" + result);
					if (result.equals(scriptmap.get("testvalue").toString())) {
						result="测试通过";
						System.out.println("测试案例："+scriptmap.get("testname")+"||"+scriptmap.get("testno")+"||"+result);
					} else {
						result="存在BUG";
						System.out.println("测试案例："+scriptmap.get("testname")+"||"+scriptmap.get("testno")+"||"+result);
					}
				}
				
				//是否为断言，包含比较
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==99){
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					result=driver.findElement(By.xpath(scriptmap.get("xpath").toString())).getText();
					if("".equals(result)){
						result=driver.findElement(By.xpath(scriptmap.get("xpath").toString())).getAttribute("value");
					}
					System.out.println("页面获取测试对象是：" + result);
					if (result.contains(scriptmap.get("testvalue").toString())) {
						result="测试通过";
						System.out.println("测试案例："+scriptmap.get("testname")+"||"+scriptmap.get("testno")+"||"+result);
					} else {
						result="存在BUG";
						System.out.println("测试案例："+scriptmap.get("testname")+"||"+scriptmap.get("testno")+"||"+result);
					}
				}
				
				//是否关闭当前窗口
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==100){
					driver.close();
				}
				
				//是否关闭所有窗口
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==102){
					driver.quit();
				}
				
				//是否是新标签页面
				if(Integer.parseInt(scriptmap.get("scripttype").toString())==101){
//					switchToWindow(driver,scriptmap.get("xpath").toString());
					driver.get(scriptmap.get("xpath").toString());  
				}
			}
		}catch (Exception e){
			result="第"+scriptmap.get("step")+"步，测试异常失败";
			System.out.println(result);
		}
		//客户端从这里结束复制代码
		
		sql="update testmng set testresult=? where testid=?";
		try{
			pst=conn.prepareStatement(sql);
			pst.setString(1, result);
			pst.setInt(2, testid);
			pst.executeUpdate();
		}catch (Exception e){
			System.out.println("更新案例表失败");
		}
		
		rs.close();
		pst.close();
		conn.close();
		return "ok";
	}
	
	public String autoall(int projectid, int userid) throws ClassNotFoundException, SQLException, IOException{
		WebDriver driver=null;
		List list=null;
		Map resultmap=new HashMap();
		Map scriptmap=null;
		String sql=null;
		String result=null;
		
		Mysqljdbc mysqljdbc=new Mysqljdbc();
		Connection conn=mysqljdbc.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		// 启动浏览器
		sql="select browserpath from sys_users where userid=?";
		try{
			pst=conn.prepareStatement(sql);
			pst.setInt(1, userid);
			rs=pst.executeQuery();
			list=mysqljdbc.getlist(rs);
		}catch (Exception e){
			System.out.println("用户表获取数据失败");
			return "用户表获取数据失败";
		}
		
		scriptmap=(Map)list.get(0);
		if("".equals(scriptmap.get("browserpath"))){
			return "本地浏览器地址为空";
		}
		try{
			if(scriptmap.get("browserpath").toString().contains("IEDriverServer")){
				System.setProperty("webdriver.ie.driver", scriptmap.get("browserpath").toString());
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				driver = new InternetExplorerDriver(ieCapabilities);
			}
			if(scriptmap.get("browserpath").toString().contains("firefox")){
				System.setProperty("webdriver.firefox.bin", scriptmap.get("browserpath").toString());
				driver = new FirefoxDriver();
				
			}
			if(scriptmap.get("browserpath").toString().contains("chrome")){
				System.setProperty("webdriver.chrome.driver", scriptmap.get("browserpath").toString());
				driver = new ChromeDriver();
			}
		}catch(Exception e){
			return "浏览器调用失败";
		}
		
		// 全局设置延迟，如果操作无响应，则等待最多10S
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// 浏览器最大化
		driver.manage().window().maximize();

		//开始测试
		sql="update testmng set testresult='' where projectid=?";
		pst=conn.prepareStatement(sql);
		pst.setInt(1, projectid);
		pst.execute();
			
		//获取所有案例
		sql="select * from testmng where projectid=? and status=1 "
				+ "order by CAST(SUBSTRING_INDEX(testno, '-', 1) as SIGNED),testname, CAST(SUBSTRING_INDEX(testno, '-', -1) as SIGNED) asc";
		List listgroup=null;
		try {
			pst=conn.prepareStatement(sql);
			pst.setInt(1, projectid);
			rs=pst.executeQuery();
			
			listgroup=mysqljdbc.getlist(rs);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("获取案例表数据失败");
			return "获取案例表数据失败";
		}
		
		//循环案例
		for(int j=0;j<listgroup.size();j++){
			Map testmap=(Map)listgroup.get(j);
			System.out.println("======开始案例:"+testmap.get("testname")+"||"+testmap.get("testno")+"======");
			
			//获取案例脚本
			sql="select a.*,b.testno,b.testname from script a, testmng b where a.testid=b.testid and a.testid=? "
					+ "order by a.step asc";
			
			try {
				pst=conn.prepareStatement(sql);
				pst.setInt(1, Integer.parseInt(testmap.get("testid").toString()));
				rs=pst.executeQuery();
				
				list=mysqljdbc.getlist(rs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("获取脚本数据失败");
				continue;
				
			}
			
			if(list.size()==0){
				sql="update testmng set testresult=? where testid=?";
				try {
					pst=conn.prepareStatement(sql);
					pst.setString(1, "未找到测试脚本");
					pst.setInt(2, Integer.parseInt(testmap.get("testid").toString()));
					pst.executeUpdate();
					
					rs.close();
					pst.close();
					conn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("更新案例表失败");
				}
				continue;
			}
			
			//客户端从这里开始复制代码
			try{
				//循环脚本
				for(int i=0;i<list.size();i++){
					scriptmap=(Map)list.get(i);
					System.out.println("开始执行selenium脚本第"+scriptmap.get("step")+"步");
					
					//是否输入新网址
					if(Integer.parseInt(scriptmap.get("scripttype").toString())==0){
						if(StringUtil.isBlank(scriptmap.get("testurl").toString())){
							return result="第"+scriptmap.get("step")+"步，未找到测试web地址";
						}
						// 浏览器输入地址
						driver.get(scriptmap.get("testurl").toString());
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
					}
					
					//是否为输入的脚本
					if(Integer.parseInt(scriptmap.get("scripttype").toString())==1){
						driver.findElement(By.xpath(scriptmap.get("xpath").toString())).clear();
						driver.findElement(By.xpath(scriptmap.get("xpath").toString())).sendKeys(scriptmap.get("testvalue").toString());	
					}
					
					//是否为批量输入的脚本
					if(Integer.parseInt(scriptmap.get("scripttype").toString())==2){
						List<WebElement> inputs=driver.findElements(By.xpath(scriptmap.get("xpath").toString()));
						for(int k=0;k<inputs.size();k++){
							WebElement input=inputs.get(k);
							if(input.getAttribute("type").equals("hidden")){
								continue;
							}else if(input.getAttribute("type").equals("radio")){
								input.click();
							}else{
								input.clear();
								input.sendKeys(scriptmap.get("testvalue").toString());
							}
						}	
					}
					
					//是否为iframe页面
					if(Integer.parseInt(scriptmap.get("scripttype").toString())==3){
						WebElement iframe = driver.findElement(By.xpath(scriptmap.get("xpath").toString()));
				        driver.switchTo().frame(iframe);
					}
					
					//是否为点击事件
					if(Integer.parseInt(scriptmap.get("scripttype").toString())==88){
						driver.findElement(By.xpath(scriptmap.get("xpath").toString())).click();	
					}
					
					//是否为断言,全等比较
					if(Integer.parseInt(scriptmap.get("scripttype").toString())==98){
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
						result=driver.findElement(By.xpath(scriptmap.get("xpath").toString())).getText();
						if("".equals(result)){
							result=driver.findElement(By.xpath(scriptmap.get("xpath").toString())).getAttribute("value");
						}
						System.out.println("页面获取测试对象是：" + result);
						if (result.equals(scriptmap.get("testvalue").toString())) {
							result="测试通过";
							System.out.println("测试案例："+scriptmap.get("testname")+"||"+scriptmap.get("testno")+"||"+result);
						} else {
							result="存在BUG";
							System.out.println("测试案例："+scriptmap.get("testname")+"||"+scriptmap.get("testno")+"||"+result);
						}
					}
					
					//是否为断言，包含比较
					if(Integer.parseInt(scriptmap.get("scripttype").toString())==99){
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
						result=driver.findElement(By.xpath(scriptmap.get("xpath").toString())).getText();
						if("".equals(result)){
							result=driver.findElement(By.xpath(scriptmap.get("xpath").toString())).getAttribute("value");
						}
						System.out.println("页面获取测试对象是：" + result);
						if (result.contains(scriptmap.get("testvalue").toString())) {
							result="测试通过";
							System.out.println("测试案例："+scriptmap.get("testname")+"||"+scriptmap.get("testno")+"||"+result);
						} else {
							result="存在BUG";
							System.out.println("测试案例："+scriptmap.get("testname")+"||"+scriptmap.get("testno")+"||"+result);
						}
					}
					
					//如果是全案例测试，则不允许关闭浏览器，否则会导致之后的案例无法使用浏览器
					//是否关闭当前窗口
//					if(Integer.parseInt(scriptmap.get("scripttype").toString())==100){
//						driver.close();
//					}
//					是否关闭所有窗口
//					if(Integer.parseInt(scriptmap.get("scripttype").toString())==102){
//						driver.quit();
//					}
					//只保留最后一个窗口，供下个案例只用
					if((i+1)==list.size() && (Integer.parseInt(scriptmap.get("scripttype").toString())==100
							 || Integer.parseInt(scriptmap.get("scripttype").toString())==102)){
						switchToWindowclose(driver);
					}
					//所有案例执行完后，直接关闭浏览器
					if((j+1)==listgroup.size() && (Integer.parseInt(scriptmap.get("scripttype").toString())==100
							 || Integer.parseInt(scriptmap.get("scripttype").toString())==102)){
						driver.quit();
					}
					
					//是否是新标签页面
					if(Integer.parseInt(scriptmap.get("scripttype").toString())==101){
//						switchToWindow(driver,scriptmap.get("xpath").toString());
						driver.get(scriptmap.get("xpath").toString());  
					}
				}
			}catch (Exception e){
				result="第"+scriptmap.get("step")+"步，测试异常失败";
				System.out.println(result);
			}
			//客户端从这里结束复制代码
			
			sql="update testmng set testresult=? where testid=?";
			try{
				pst=conn.prepareStatement(sql);
				pst.setString(1, result);
				pst.setInt(2, Integer.parseInt(testmap.get("testid").toString()));
				pst.executeUpdate();
				
			}catch (Exception e){
				System.out.println("更新案例表失败");
			}
			
			
//			for(int i=0;i<list.size();i++){
//				scriptmap=(Map)list.get(i);
//				if(Integer.parseInt(scriptmap.get("scripttype").toString())==1){
//					driver.findElement(By.xpath(scriptmap.get("xpath").toString())).clear();	
//				}
//			}
		}
		
		rs.close();
		pst.close();
		conn.close();
		return "ok";
	}
	
	//关闭多余窗口，保留最后一个
	public void switchToWindowclose(WebDriver driver){  
	    try {  
	        //获取浏览器所有窗口set集合
	        Set<String> handles = driver.getWindowHandles(); 
	        
	        int handlessum=handles.size();
	        for (String s : handles) {
	        	//先切换窗口
                driver.switchTo().window(s); 
                
	        	if(handlessum==1){
	        		return;
	        	}
                driver.close();
                handlessum=handlessum-1;
	        }  
	    } catch (Exception e) {  
	       System.out.println("关闭浏览器窗口异常");
	    }  
	}  
}
