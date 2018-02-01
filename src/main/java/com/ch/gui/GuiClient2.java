package com.ch.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.eclipse.jetty.util.StringUtil;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.ch.dao.Mysqljdbc;
import com.ch.selenium.Selenium;
import com.ch.table.MyTable;
import com.ch.table.MyTableModel;

/**
 * 工程设计，配置SP_TEST_MANAGE工程使用
 * @author 陈辉
 *
 */
public class GuiClient2 extends JFrame implements ActionListener {
	private JFrame jframe;

	// 页面1使用的组件
	JPanel panel_1 = new JPanel(false);
	JLabel jLabel_1;
	JLabel jLabel_2;
	JButton jbtn1_1;
	JButton jbtn1_2;
	JScrollPane scroll_1;
	JTable jTable_1;

	GuiClient2() {
		
		jframe = new JFrame();
		System.out.println("自动化测试窗口");
		jframe.setLocation(400, 200);
		jframe.setTitle("自动化测试窗口");
		jframe.setVisible(true);
		
		jframe.setDefaultCloseOperation(jframe.DO_NOTHING_ON_CLOSE);
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				int n=JOptionPane.showConfirmDialog(null, "要退出该程序吗？", "友情提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(n==0){
					System.out.println("关闭");
//					System.exit (0);
					jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
				}else{
					System.out.println("停止关闭");
					return;
				}
			}
		});
		
//		jframe.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent arg0) {
//				int n=JOptionPane.showConfirmDialog(null, "choose one", "choose one", JOptionPane.YES_NO_OPTION);
//				if(n==0){
//					return;
//				}
//				
//				System.out.println("关闭事件");
//			}
//		});

		// JPanel面板
		JPanel jPanel = new JPanel();
		jPanel.setLayout(null);
		jPanel.setBackground(Color.decode("#ffffff"));
		
		
		jbtn1_1 = new JButton("启动监听服务");
		jbtn1_1.setBounds(10, 10, 120, 30);
		jbtn1_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbtn1_1.setEnabled(false);
			}
		});
		jPanel.add(jbtn1_1);
		
		jLabel_1 = new JLabel("<html>请启动监听服务！！！</html>");
		jLabel_1.setBounds(140, 10, 150, 30);
		jPanel.add(jLabel_1);
		
		jLabel_2 = new JLabel("<html>特别说明：请通过start.bat启动监听程序，否则可能无法获取系统最高权限。</html>");
		jLabel_2.setBounds(10, 50, 250, 60);
		jPanel.add(jLabel_2);
		
//		jbtn1_2 = new JButton("关闭监听服务");
//		jbtn1_2.setBounds(10, 40, 120, 30);
//		jbtn1_2.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				jbtn1_1.setEnabled(false);
//				jLabel_1.setBounds(140, 10, 150, 30);
//				jLabel_1.setText("<html>请启动监听服务请启动监听服务请启动监听服务请启动监听服务请启动监听服务！！！</html>");
//			}
//		});
//		jPanel.add(jbtn1_2);
		
		jframe.add(jPanel);
		jframe.pack();
		jframe.setSize(300, 200);
		jframe.setResizable(false);
	}

		
	// 按钮按键事件，貌似这个方法必须存在，不能删除
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() != null) {
			System.out.println("1111");
		}
		
		// if(e.getActionCommand().equals("测试")){
		// }
		// TODO Auto-generated method stub
		// if(e.getSource()==jbtn1){
		// myTableModel.getColumnCount();
		// System.out.println("aaaa"+myTableModel.getValueAt(1,2));
		// }
	}
	
	// @Override
	// public void keyTyped(KeyEvent e) {
	// // TODO Auto-generated method stub
	// int t = e.getKeyCode();
	// System.out.println("1111111--"+t);
	// }

	// @Override
	// public void keyPressed(KeyEvent e) {
	// // TODO Auto-generated method stub
	// int t = e.getKeyCode();
	// System.out.println("1111111--"+t);
	// }

	// @Override
	// public void keyReleased(KeyEvent e) {
	// // TODO Auto-generated method stub
	// int t = e.getKeyCode();
	// System.out.println("1111111--"+t);
	// }

	public static void main(String[] args) throws UnknownHostException, IOException {
		new GuiClient2();
	}
}
