package com.ch.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

import com.ch.selenium.Selenium;
import com.ch.table.MyTable;
import com.ch.table.MyTableModel;

/**
 * 工程设计，配置SP_TEST_MANAGE工程使用
 * @author 陈辉
 *
 */
public class GuiClient extends JFrame implements ActionListener {
	Jdbcbean jdbcbean = new Jdbcbean();
	private JFrame jframe;
	int table_values = 0;

	// 页面1使用的组件
	JPanel panel_1 = new JPanel(false);
	JLabel jLabel_1;
	JButton jbtn1_1;
	JButton jbtn1_2;
	JScrollPane scroll_1;
	JTable jTable_1;
	// 设置表格的显示字段
	String[] columnNames = { "projectid", "团队名称", "项目名称" };
	// 设置表格的数据库字段名
	String[] columnNames_s = { "projectid", "teamname", "projectname" };

	// 页面2使用的组件
	JPanel panel_2 = new JPanel(false);
	JLabel jLabel_2;
	JButton jbtn2_1;
	JButton jbtn2_2;
	JScrollPane scroll_2;
	JTable jTable_2;
	// 设置表格的显示字段
	final String[] columnNames1 = { "testid", "项目名称", "案例名称","案例编号" };
	// 设置表格的数据库字段名
	final String[] columnNames_s1 = { "testid", "projectname","testname", "testno" };
	
	// 页面3使用的组件
	JPanel panel_3 = new JPanel(false);
	JLabel jLabel_3;
	JButton jbtn3_1;
	JButton jbtn3_2;
	JScrollPane scroll_3;
	JTable jTable_3;
	// 设置表格的显示字段
	final String[] columnNames2 = { "testid", "项目名称", "案例编号" };
	// 设置表格的数据库字段名
	final String[] columnNames_s2 = { "testid", "projectname", "testno" };

	GuiClient() {
		j_frame();
	}

	/**
	 * 生成GUI
	 */
	public void j_frame() {
		jframe = new JFrame();
		System.out.println("自动化测试窗口");
		jframe.setLocation(400, 200);
		jframe.setTitle("自动化测试窗口");
		jframe.setVisible(true);
		
		//关闭窗口方法1
//		jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);

		//关闭窗口方法2
		jframe.setDefaultCloseOperation(jframe.DO_NOTHING_ON_CLOSE);
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				int n=JOptionPane.showConfirmDialog(null, "要退出该程序吗？", "友情提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(n==0){
					System.out.println("关闭");
					System.exit (0);
				}else{
					System.out.println("停止关闭");
					return;
				}
			}
		});

		// JPanel面板
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(1, 1));
		jPanel.setBackground(Color.decode("#A6FFFF"));

		// JTabbedPane选项卡
		JTabbedPane tabbedPane = new JTabbedPane();

		// 页面1
		tabbedPane.addTab("one", ComponentPanel());
		tabbedPane.setSelectedIndex(0);// 默认显示标签
		// 页面2
		tabbedPane.addTab("two", ComponentPanel1());
		// 页面3
//		tabbedPane.addTab("three", ComponentPanel2());

		// 将选项卡添加到panl中
		jPanel.add(tabbedPane);

		jframe.add(jPanel);
		jframe.pack();
		jframe.setSize(500, 600);
		jframe.setResizable(false);
	}

	/**
	 * <br>
	 * 方法说明：添加信息到选项卡中 <br>
	 * 输入参数：String text 显示的信息内容 <br>
	 * 返回类型：Component 成员对象(Component表示GUI组件，可返回JPanel,JButton等等)
	 */
	// 选项卡1
	protected Component ComponentPanel() {
		panel_1.setLayout(null);
		/**
		 * 查询
		 */
		jLabel_1 = new JLabel("项目名:");
		jLabel_1.setBounds(10, 10, 50, 25);
		panel_1.add(jLabel_1);
		final JTextArea jTextArea_1 = new JTextArea();
		jTextArea_1.setBounds(60, 10, 80, 25);
		panel_1.add(jTextArea_1);

		jbtn1_1 = new JButton("查询");
		jbtn1_1.setBounds(150, 10, 60, 25);
		jbtn1_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				List list = null;
				// TODO Auto-generated method stub
				try {
					list = jdbcbean.project(jTextArea_1.getText().toString());
				} catch (Exception e1) {
					System.out.println("查询失败");
				}
				// 生成表格
				scroll_1.setVisible(false);// 要删除的组件，需要先停止工作
				panel_1.remove(scroll_1);
				jTable_1 = myJTable(list, columnNames, columnNames_s);
				scroll_1 = new JScrollPane(jTable_1);
				scroll_1.setBounds(10, 40, 460, 400);
				panel_1.add(scroll_1);
			}
		});
		panel_1.add(jbtn1_1);

		/**
		 * 输入框
		 */
		jLabel_1 = new JLabel("浏览器路径:");
		jLabel_1.setBounds(220, 10, 70, 25);
		panel_1.add(jLabel_1);
		final JTextArea jTextArea_2 = new JTextArea();
		jTextArea_2.setBounds(300, 10, 80, 25);
		panel_1.add(jTextArea_2);

		/**
		 * 按钮操作
		 */
		jbtn1_2 = new JButton("测试");
		jbtn1_2.setBounds(390, 10, 80, 25);
		jbtn1_2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (table_values == 0 || "".equals(jTextArea_2.getText())) {
					JOptionPane.showMessageDialog(null, "请选择测试数据或输入浏览器路径");
					return;
				}
				Selenium selenium = new Selenium();
				int userid = 0;
				try {
					//取消掉通过登录名查找浏览器路径
//					userid = jdbcbean.userid(jTextArea_2.getText());
//					String seleniumresult=selenium.autoall(table_values, userid);
					String seleniumresult=selenium.autoall(table_values, jTextArea_2.getText());
					JOptionPane.showMessageDialog(null, seleniumresult,"测试结果", JOptionPane.INFORMATION_MESSAGE); 
				} catch (Exception e1) {
					System.out.println("userid获取失败或者测试失败");
				}
		        
			}
		});
		panel_1.add(jbtn1_2);

		/**
		 * 生成表格和操作
		 */
		// 获取生成表格需要的数据
		List list = null;
		try {
			list = jdbcbean.project("");
		} catch (Exception e) {
			System.out.println("项目数据获取失败");
		}
		jTable_1 = myJTable(list, columnNames, columnNames_s);
		scroll_1 = new JScrollPane(jTable_1);
		scroll_1.setBounds(10, 40, 460, 400);
		panel_1.add(scroll_1);

		return panel_1;
	}

	// 选项卡2
	protected Component ComponentPanel1() {
		panel_2.setLayout(null);

		/**
		 * 查询
		 */
		jLabel_2 = new JLabel("案例名称/编号:");
		jLabel_2.setBounds(10, 10, 100, 25);
		panel_2.add(jLabel_2);
		final JTextArea jTextArea_1 = new JTextArea();
		jTextArea_1.setBounds(100, 10, 80, 25);
		panel_2.add(jTextArea_1);

		jbtn2_1 = new JButton("查询");
		jbtn2_1.setBounds(190, 10, 60, 25);
		jbtn2_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				List list = null;
				// TODO Auto-generated method stub
				// if("".equals(jTextArea_1.getText())){
				// JOptionPane.showMessageDialog(null,"输入项目名称");
				// return;
				// }
				try {
					list = jdbcbean.testmng(jTextArea_1.getText().toString());
				} catch (Exception e1) {
					System.out.println("查询失败");
				}

				// 生成表格
				scroll_2.setVisible(false);// 要删除的组件，需要先停止工作
				panel_2.remove(scroll_2);
				jTable_2 = myJTable(list, columnNames1, columnNames_s1);
				scroll_2 = new JScrollPane(jTable_2);
				scroll_2.setBounds(10, 40, 460, 400);
				panel_2.add(scroll_2);
			}
		});
		panel_2.add(jbtn2_1);

		/**
		 * 输入框
		 */
		jLabel_2 = new JLabel("浏览器路径:");
		jLabel_2.setBounds(260, 10, 80, 25);
		panel_2.add(jLabel_2);
		final JTextArea jTextArea_2 = new JTextArea();
		jTextArea_2.setBounds(340, 10, 60, 25);
		panel_2.add(jTextArea_2);

		/**
		 * 按钮操作
		 */
		jbtn2_2 = new JButton("测试");
		jbtn2_2.setBounds(410, 10, 60, 25);
		jbtn2_2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (table_values == 0 || "".equals(jTextArea_2.getText())) {
					JOptionPane.showMessageDialog(null, "请选择测试数据或输入浏览器路径");
					return;
				}
				Selenium selenium = new Selenium();

				try {
					//取消掉通过登录名查找浏览器路径
//					int userid = jdbcbean.userid(jTextArea_2.getText());
//					String seleniumresult=selenium.autoone(table_values, userid);
					
					String seleniumresult=selenium.autoone(table_values, jTextArea_2.getText());
					JOptionPane.showMessageDialog(null, seleniumresult,"测试结果", JOptionPane.INFORMATION_MESSAGE); 
				} catch (Exception e1) {
					System.out.println("userid获取失败或者测试失败");
				}

			}
		});
		panel_2.add(jbtn2_2);

		/**
		 * 生成表格和操作
		 */
		List list = null;
		// 获取生成表格需要的数据
		try {
			list = jdbcbean.testmng("");
		} catch (Exception e) {
			System.out.println("案例数据获取失败");
		}

		jTable_2 = myJTable(list, columnNames1, columnNames_s1);
		scroll_2 = new JScrollPane(jTable_2);
		scroll_2.setBounds(10, 40, 460, 400);
		panel_2.add(scroll_2);

		return panel_2;
	}

	// 选项卡3
	protected Component ComponentPanel2() {
		panel_3.setLayout(null);

		/**
		 * 查询
		 */
		jLabel_3 = new JLabel("案例名:");
		jLabel_3.setBounds(10, 10, 50, 25);
		panel_3.add(jLabel_3);
		final JTextArea jTextArea_1 = new JTextArea();
		jTextArea_1.setBounds(60, 10, 80, 25);
		panel_3.add(jTextArea_1);

		jbtn3_1 = new JButton("查询");
		jbtn3_1.setBounds(150, 10, 60, 25);
		jbtn3_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				List list = null;
				// TODO Auto-generated method stub
				// if("".equals(jTextArea_1.getText())){
				// JOptionPane.showMessageDialog(null,"输入项目名称");
				// return;
				// }
				try {
					list = jdbcbean.testmng(jTextArea_1.getText().toString());
				} catch (Exception e1) {
					System.out.println("查询失败");
				}

				// 生成表格
				scroll_3.setVisible(false);// 要删除的组件，需要先停止工作
				panel_3.remove(scroll_2);
				jTable_3 = myJTable(list, columnNames1, columnNames_s1);
				scroll_3 = new JScrollPane(jTable_2);
				scroll_3.setBounds(10, 40, 460, 400);
				panel_3.add(scroll_3);
			}
		});
		panel_3.add(jbtn3_1);

		/**
		 * 输入框
		 */
		jLabel_3 = new JLabel("系统登录名:");
		jLabel_3.setBounds(220, 10, 70, 25);
		panel_3.add(jLabel_3);
		final JTextArea jTextArea_2 = new JTextArea();
		jTextArea_2.setBounds(300, 10, 80, 25);
		panel_3.add(jTextArea_2);

		/**
		 * 按钮操作
		 */
		jbtn3_2 = new JButton("测试");
		jbtn3_2.setBounds(390, 10, 80, 25);
		jbtn3_2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				//编译并运行java文件
				
		        //动态获取当前启动的class目录
//			        File dir=new File("C:\\Users\\陈辉\\Desktop\\selenium");
		        File dir = new File("");//参数为空 
		        String courseFile=null;
				try {
					courseFile = dir.getCanonicalPath()+"\\selenium_script";
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} 

				//获取本地文件夹里面的所有文件
		        File file=new File(courseFile);
		        String files[];
		        files=file.list();
			    
		        //动态组织jar包路径
		        String javacode=null;
		        List listold=new ArrayList();
				for (int i = 0; i < files.length; i++) {
					System.out.println(files[i]);
					if(!files[i].contains("java")){
						continue;
					}
					for(int j=0;j<listold.size();j++){
						if(files[i].split("\\.")[0].equals(listold.get(j))){
							return;
						}
					}
					//引入该文件夹在所有jar包：-Djava.ext.dirs=\"C:\\Users\\陈辉\\Desktop\\test1\"
					javacode = "javac -Djava.ext.dirs=\""+courseFile+"\" "+courseFile+"\\"+files[i];
					System.out.println("生成CLASS："+javacode);

					try {
						Runtime.getRuntime().exec(javacode, null, new File(courseFile));
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						System.out.println("脚本编译失败");
					}
					///////////////
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						javacode = null;
						System.out.println(files[i].split("\\.")[0]);
						javacode = "java -Djava.ext.dirs=\""+courseFile+"\" "+files[i].split("\\.")[0];
						System.out.println("执行CLASS："+javacode);
						
						Runtime.getRuntime().exec(javacode, null, new File(courseFile));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("脚本运行失败");
					}
					///////////
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					
					
					listold.add(files[i].split("\\.")[0]);
				}
				
				System.out.println("测试结束");
			}
		});
		panel_3.add(jbtn3_2);

		/**
		 * 生成表格和操作
		 */
		List list = null;
		// 获取生成表格需要的数据
		try {
			list = jdbcbean.testmng("");
		} catch (Exception e) {
			System.out.println("案例数据获取失败");
		}

		jTable_3 = myJTable(list, columnNames2, columnNames_s2);
		scroll_3 = new JScrollPane(jTable_3);
		scroll_3.setBounds(10, 40, 460, 400);
		panel_3.add(scroll_3);

		return panel_3;
	}
		
	// 按钮按键事件，貌似这个方法必须存在，不能删除
	public void actionPerformed(ActionEvent e) {
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

	/**
	 * 公共方法，生成表格，并增加点击事件
	 * 
	 * @param list
	 * @param columnNames
	 * @param columnNames_s
	 */
	public JTable myJTable(List list, String[] columnNames, String[] columnNames_s) {
		//生成表格
		MyTableModel myTableModel = new MyTableModel(list, columnNames, columnNames_s);
		final JTable table = new JTable(myTableModel);
		// 隐藏列
		myTableModel.HiddenCell(table, 0);

		
		//点击事件，仅当鼠标单击时响应
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				// 得到选中的行列的索引值
				int row = table.getSelectedRow();
				// int column= table.getSelectedColumn();
				int column = 0;
				
				// 得到选中的单元格的值，表格中都是字符串
				Object value = table.getValueAt(row, column);
				
				// 提示框
//				String info = row + "行" + column + "列值 : " + value.toString();
//				JOptionPane.showMessageDialog(null, info);

				table_values = Integer.parseInt(value.toString());
			}
		});
		return table;
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		new GuiClient();
	}
}
