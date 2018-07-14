/**
 * 
 */
package com.tildev.tobyspring.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

import com.tildev.tobyspring.domain.UserDto;

/**
 * @author tildev
 * @date 2018. 7. 14.
 */
public class UserDao {

	private void insertValue() throws IOException {

		ClassLoader cl;
		cl = Thread.currentThread().getContextClassLoader();

		if (cl == null) {
			cl = ClassLoader.getSystemClassLoader();
		}

		URL url = cl.getResource("config/database.properties");

		// 클래스 패스를 통해 info.property 있는 위치를 찾기
		File propFile = new File(url.getPath());
		FileInputStream is;

		try {
			is = new FileInputStream(propFile);
			Properties props = new Properties();
			props.load(is);

			jdbcDriver = props.getProperty("jdbc.driver");
			jdbcUrl = props.getProperty("jdbc.url");
			jdbcUsername = props.getProperty("jdbc.username");
			jdbcPassword = props.getProperty("jdbc.password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Value("${jdbc.driver}")
	private String jdbcDriver;

	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${jdbc.username}")
	private String jdbcUsername;

	@Value("${jdbc.password}")
	private String jdbcPassword;

	public void add(UserDto user) throws ClassNotFoundException, SQLException, IOException {

		insertValue();

		Class.forName(jdbcDriver);
		Connection c = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

		PreparedStatement ps = c.prepareStatement("insert into users(userid, name, password) values(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		ps.executeUpdate();

		ps.close();
		c.close();
	}

	public UserDto get(String userId) throws ClassNotFoundException, SQLException, IOException {
		insertValue();
		Class.forName(jdbcDriver);

		Connection c = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

		PreparedStatement ps = c.prepareStatement("select * from users where userid = ?");
		ps.setString(1, userId);

		ResultSet rs = ps.executeQuery();
		rs.next();

		UserDto user = new UserDto();
		user.setId(rs.getString("userid"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));

		rs.close();
		ps.close();
		c.close();

		return user;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		UserDao dao = new UserDao();

		UserDto user = new UserDto();
		user.setId("id6");
		user.setName("이름6");
		user.setPassword("pass6");

		dao.add(user);

		System.out.println(user.getId() + " 등록 성공");

		UserDto user2 = dao.get(user.getId());
		System.out.println(user2.getName());

	}
}
