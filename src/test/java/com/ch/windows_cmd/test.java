package com.ch.windows_cmd;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

public class test {
	public static void main(String args[]) throws IOException{
		//编译并运行java文件
        //java文件所在目录
        File dir=new File("C:\\Users\\陈辉\\Desktop\\test1");
        Process ls_proc=null;
        //引入该文件夹在所有jar包：-Djava.ext.dirs=\"C:\\Users\\陈辉\\Desktop\\test1\"
        ls_proc =Runtime.getRuntime().exec("javac -Djava.ext.dirs=\"C:\\Users\\陈辉\\Desktop\\test1\" Test1.java",null,dir);
        
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ls_proc =Runtime.getRuntime().exec("java -Djava.ext.dirs=\"C:\\Users\\陈辉\\Desktop\\test1\" Test1",null,dir);
	 
//        String ls_str;
//        DataInputStream ls_in = new DataInputStream(
//                ls_proc.getInputStream());
//
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
