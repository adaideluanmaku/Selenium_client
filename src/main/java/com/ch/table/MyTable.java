package com.ch.table;

import java.util.List;
import java.util.Map;

import javax.swing.JTable;

public class MyTable extends JTable{
	static String[] columnNames = null;// 表格字段名，固定名字，不能修改
	static Object[][] data = null;
	
	public MyTable(List datalist, String[] columnNames, String[] columnNames_s) {
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
}
