package com.ch.windows_cmd;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

public class Helloworld {
	public static void main(String args[]) throws IOException, InterruptedException{
		//调用bat文件
		//工作目录
//    	File dir = new File("C:\\Users\\陈辉\\Desktop\\selenium");
//    	Process process=null;
		
		//执行bat文件
//    	process = Runtime.getRuntime().exec("C:\\Users\\陈辉\\Desktop\\selenium\\start.bat",null,dir);
//    	process.waitFor();
    	
		//////////////////////////////////////////
    	//编译并运行java文件
		//工作目录
        File dir=new File("C:\\Users\\陈辉\\Desktop\\test");
        Process process=null;
        
        //编译文件
        //如果编译时需要引入JAR包：javac -Djava.ext.dirs=\"C:\\Users\\陈辉\\Desktop\\test1\" Test1.java
        process =Runtime.getRuntime().exec("javac HelloWord.java",null,dir);
        
        //执行文件
//        process =Runtime.getRuntime().exec("java HelloWord",null,dir);
	 
        ////////////////////////////////////////
        //下面是将执行内容打印输出的（system.out）
//        DataInputStream ls_in = new DataInputStream(
//                process.getInputStream());
//        String ls_str;
//		while ((ls_str = ls_in.readLine()) != null) {
//			System.out.println(ls_str);
//			try {
//				ls_proc.waitFor();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		ls_in.close();
		
	}
}
