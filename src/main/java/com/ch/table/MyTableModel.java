package com.ch.table;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.eclipse.jetty.util.StringUtil;

import com.google.common.collect.Table;

/**
 * 被调用
 * MyTableModel myTableModel=new MyTableModel(list,columnNames,columnNames_s);
 * JTable table = new JTable(myTableModel);
 * 加滚动条
 * JScrollPane scroll = new JScrollPane(table);
 * add(scroll);
 */

/**
 * table模型，可获得数据结构
 */
public class MyTableModel extends AbstractTableModel {
	/**
	 * 定义显示的字段名和真实的字段名，顺序一致
	 */
	String[] columnNames = null;// 表格字段名，固定名字，不能修改

	/**
	 * 数据库数据
	 */
	Object[][] data = null;

	/**
	 * 构造方法，初始化二维数组data对应的数据
	 * 
	 * datalist Spring LIST原数据 columnNames 显示字段名 columnNames_s 实际字段名
	 */
	public MyTableModel(List datalist, String[] columnNames, String[] columnNames_s) {
		if (datalist == null) {
			return;
		}
		this.columnNames = columnNames;

		data = null;
		data = new Object[datalist.size()][columnNames_s.length];
		for (int i = 0; i < datalist.size(); i++) {// 行数
			Map map = (Map) datalist.get(i);
			for (int j = 0; j < columnNames.length; j++) {// 列数
				data[i][j] = map.get(columnNames_s[j].toString());
			}
		}
	}

	// 以下为继承自AbstractTableModle的方法，可以自定义
	/**
	 * 得到列名
	 */
	public String getColumnName(int column) {
//		System.out.println("得到列名");
//		System.out.println(columnNames[column]);
		return columnNames[column];
	}

	/**
	 * 重写方法，得到表格列数
	 */
	public int getColumnCount() {
//		System.out.println("表格列数");
//		System.out.println(columnNames.length);
		return columnNames.length;
	}

	/**
	 * 得到表格行数
	 */
	public int getRowCount() {
//		System.out.println("表格行数");
//		System.out.println(data.length);
		return data.length;
	}

	/**
	 * 得到数据所对应对象
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
//		System.out.println("对应数据");
//		System.out.println(data[rowIndex][columnIndex]);
		if(rowIndex<0){
			rowIndex=0;
		}
		if(columnIndex<0){
			columnIndex=0;
		}
		return data[rowIndex][columnIndex];
	}

	/**
	 * 得到指定列的数据类型
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
//		System.out.println("数据类型");
//		System.out.println(data[0][columnIndex].getClass());
		return data[0][columnIndex].getClass();
	}

	/**
	 * 指定设置数据单元是否可编辑.这里设置"姓名","学号"不可编辑
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
//		System.out.println("是否可编辑");
//		System.out.println(columnIndex);
		if (columnIndex < 2)
			return false;
		else
			return true;
	}

	/**
	 * 如果数据单元为可编辑，则将编辑后的值替换原来的值
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		System.out.println("替换原来的值");
		data[rowIndex][columnIndex] = aValue;
		/* 通知监听器数据单元数据已经改变 */
		fireTableCellUpdated(rowIndex, columnIndex);
		System.out.println(aValue);
	}

	// 隐藏列
	public void HiddenCell(JTable table, int column) {
//		System.out.println("隐藏列");
		TableColumn tc = table.getTableHeader().getColumnModel().getColumn(column);
		tc.setMaxWidth(0);
		tc.setPreferredWidth(0);
		tc.setWidth(0);
		tc.setMinWidth(0);
		table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(0);
		table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(0);
	}
}
