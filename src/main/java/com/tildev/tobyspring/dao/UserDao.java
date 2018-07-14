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

	@Value("${jdbc.driver}")
	private String jdbcDriver;

	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${jdbc.username}")
	private String jdbcUsername;

	@Value("${jdbc.password}")
	private String jdbcPassword;

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

	/**
	 * getConnection 리펙토링 **관심사의 분리** 중복되는 사항을 뽑아내자. - 메소드 추출 기법
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
		insertValue();

		Class.forName(jdbcDriver);
		Connection c = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

		return c;
	}

	/**
	 * add
	 * 
	 * @param user
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void add(UserDto user) throws ClassNotFoundException, SQLException, IOException {

		Connection c = getConnection();

		PreparedStatement ps = c.prepareStatement("insert into users(userid, name, password) values(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		ps.executeUpdate();

		ps.close();
		c.close();
	}

	/**
	 * get
	 * 
	 * @param userId
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public UserDto get(String userId) throws ClassNotFoundException, SQLException, IOException {
		Connection c = getConnection();

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
		user.setId("id7");
		user.setName("이름7");
		user.setPassword("pass7");

		dao.add(user);

		System.out.println(user.getId() + " 등록 성공");

		UserDto user2 = dao.get(user.getId());
		System.out.println(user2.getName());

	}
}
