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
public abstract class UserDao {

	@Value("${jdbc.driver}")
	protected String jdbcDriver;

	@Value("${jdbc.url}")
	protected String jdbcUrl;

	@Value("${jdbc.username}")
	protected String jdbcUsername;

	@Value("${jdbc.password}")
	protected String jdbcPassword;

	/**
	 * 1. 템플릿 메소드 패턴 (template method pattern)
	 * 슈퍼클래스에 기본적인 로직의 흐름을 만들고
	 * 추상메소드나 오버라이딩이 가능한 protected 메소드 등으로 만들어
	 * 서브 클래스에서 필요에 맞게 구현해서 사용하는 디자인 패턴
	 * 
	 * 2. 팩토리 메소드 패턴(factory method pattern)
	 * 서브클래스에서 구체적인 오브젝트 생성 방법을 결정하게 하는 디자인 패턴
	 * 
	 * getConnection 을 상속을 받아 구현 
	 * 
	 * 문제점 : getConnection()의 구현 코드가 매 DAO 클래스마다 중복됨
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public abstract Connection getConnection() throws IOException, ClassNotFoundException, SQLException;

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
		UserDao dao = new NUserDao();

		UserDto user = new UserDto();
		user.setId("id8");
		user.setName("이름8");
		user.setPassword("pass8");

		dao.add(user);

		System.out.println(user.getId() + " 등록 성공");

		UserDto user2 = dao.get(user.getId());
		System.out.println(user2.getName());

	}
}

class NUserDao extends UserDao {
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

	@Override
	public Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
		insertValue();

		Class.forName(jdbcDriver);
		Connection c = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

		return c;
	}
}

class DUserDao extends UserDao {

	@Override
	public Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
		return null;
	}
}
