package com.ch.gui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.ch.dao.Mysqljdbc;

public class Jdbcbean {
	
	public List project(String projectname) throws SQLException, ClassNotFoundException, IOException{
		Mysqljdbc mysqljdbc=new Mysqljdbc();
		Connection conn=mysqljdbc.getConn();
		PreparedStatement pst =null;
		String sql=null;
		List list=null;
		sql="select a.teamname,b.* from team a inner join project b on a.teamid=b.teamid where b.projectname like ?";
		pst=conn.prepareStatement(sql);
		pst.setString(1, "%"+projectname+"%");
		ResultSet rs=pst.executeQuery();
		list=mysqljdbc.getlist(rs);
		rs.close();
		pst.close();
		conn.close();
		return list;
	}
	
	public List testmng(String str) throws ClassNotFoundException, SQLException, IOException{
		String testno=str;
		String testname=str;
		Mysqljdbc mysqljdbc=new Mysqljdbc();
		Connection conn=mysqljdbc.getConn();
		PreparedStatement pst =null;
		String sql=null;
		List list=null;
		sql="select a.projectname,b.* from project a inner join testmng b on a.projectid=b.projectid where b.testno like ? or b.testname like ?";
		pst=conn.prepareStatement(sql);
		pst.setString(1, "%"+testno+"%");
		pst.setString(2, "%"+testname+"%");
		ResultSet rs=pst.executeQuery();
		list=mysqljdbc.getlist(rs);
		rs.close();
		pst.close();
		conn.close();
		return list;
	}
	
	public int userid(String loginname) throws NumberFormatException, SQLException, ClassNotFoundException, IOException{
		Mysqljdbc mysqljdbc=new Mysqljdbc();
		Connection conn=mysqljdbc.getConn();
		
		String sql=null;
		int userid=0;
		sql="select userid from sys_users where loginname=?";
		PreparedStatement pst=conn.prepareStatement(sql);
		pst.setString(1, loginname);
		ResultSet rs=pst.executeQuery();
		rs.next();
		userid=Integer.parseInt(rs.getObject(1).toString());
		rs.close();
		pst.close();
		conn.close();
		return userid;
	}
}
