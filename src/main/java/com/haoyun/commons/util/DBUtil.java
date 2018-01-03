package com.haoyun.commons.util;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * DBUtil，数据库访问工具类<br/>
 * 对应测试类： {@link }
 * 
 * @preserve all
 */
public class DBUtil {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  获得连接
 	 */
	public static Connection getConnection(String dataname) {
		Connection conn = null;

		String url = "jdbc:mysql://" + PropertiesUtil.getString("mysql.ip") + ":"
				+ PropertiesUtil.getString("mysql.port") + "/" + dataname + "?useUnicode=true&characterEncoding=utf8";
		String user = PropertiesUtil.getString("jdbc.slave1.username");
		String password = PropertiesUtil.getString("jdbc.slave1.password");
		System.out.println("++++" + url);

		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("connection is successful!");
		} catch (SQLException e) {
			System.out.println("connection error！");
			e.printStackTrace();
		}

		return conn;
	}

	// 获得连接
	public static Connection getConnection(String dataname, HttpSession session, String type) {
		Connection conn = null;
		// String url
		// ="jdbc:mysql://112.124.31.105:3306/"+dataname+"?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true";
		String url = "";
		String user = "";
		String password = "";
		if (session.getAttribute("DATAMAP") != null) {
			Map dataSource = (Map) session.getAttribute("DATAMAP");
			if (type == "1") {
				url = "jdbc:mysql://" + dataSource.get("url").toString() + "/" + dataname
						+ "?useUnicode=true&characterEncoding=utf8";
				user = dataSource.get("user").toString();
				password = dataSource.get("pass").toString();
			}else{
				url = "jdbc:mysql://" + dataSource.get("url2").toString() + "/" + dataname
						+ "?useUnicode=true&characterEncoding=utf8";
				user = dataSource.get("user2").toString();
				password = dataSource.get("pass2").toString();
			}
		} else {
			url = "jdbc:mysql://" + PropertiesUtil.getString("mysql.ip") + ":" + PropertiesUtil.getString("mysql.port")
					+ "/" + dataname + "?useUnicode=true&characterEncoding=utf8";
			user = PropertiesUtil.getString("username");
			password = PropertiesUtil.getString("password");
		}
		/*
		 * String user = "root"; String password = "123456";
		 */
		try {
			System.out.println(url);
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("connection is successful!");
		} catch (SQLException e) {
			System.out.println("connection error！");
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 释放JDBC资源（关闭顺序与声明时的顺序相反）
 	 */
	public static void release(Connection conn, Statement state, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (state != null) {
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将rs结果转换成对象列表
	 * 
	 * @param rs jdbc结果集
	 * 
	 * @param clazz 对象的映射类 return 封装了对象的结果列表
	 */
	public static List populate(ResultSet rs, Class clazz)
			throws SQLException, InstantiationException, IllegalAccessException {
		// 结果集的元素对象
		ResultSetMetaData rsmd = rs.getMetaData();
		// 获取结果集的元素个数
		int colCount = rsmd.getColumnCount();
		// 返回结果的列表集合
		List list = new ArrayList();
		// 业务对象的属性数组
		Field[] fields = clazz.getDeclaredFields();

		// 对每一条记录进行操作
		while (rs.next()) {
			// 构造业务对象实体
			Object obj = clazz.newInstance();
			// 将每一个字段取出进行赋值
			for (int i = 1; i <= colCount; i++) {
				Object value = rs.getObject(i);
				// 寻找该列对应的对象属性
				for (int j = 0; j < fields.length; j++) {
					Field f = fields[j];
					// 如果匹配进行赋值
					String coname = rsmd.getColumnName(i).replace("_", "");
					if (f.getName().equalsIgnoreCase(coname)) {
						boolean flag = f.isAccessible();
						f.setAccessible(true);
						f.set(obj, value);
						f.setAccessible(flag);
					}
				}
			}
			list.add(obj);
		}

		return list;
	}

	public static int getCurrentNum(ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException {
		// 结果集的元素对象
		ResultSetMetaData rsmd = rs.getMetaData();
		// 获取结果集的元素个数
		int result = 0;

		if (rs.next())

		{
			result = rs.getInt("record");
		}
		return result;
	}
}
