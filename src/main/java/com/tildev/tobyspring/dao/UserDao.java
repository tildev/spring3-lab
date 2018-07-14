/**
 * 
 */
package com.tildev.tobyspring.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tildev.tobyspring.domain.UserDto;

/**
 * @author tildev
 * @date 2018. 7. 14.
 */
public class UserDao {

	// 상태를 관리하는 것이 아니므로, 한 번만 만들어 인스턴스 변수에 저장해두고 메소드에서 사용하게 한다.
	private SimpleConnectionMaker simpleConnectionMaker;

	/**
	 * @param simpleConnectionMaker
	 */
	public UserDao() {
		this.simpleConnectionMaker = new SimpleConnectionMaker();
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
		Connection c = simpleConnectionMaker.getConnection();

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
		Connection c = simpleConnectionMaker.getConnection();

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
		user.setId("id10");
		user.setName("이름10");
		user.setPassword("pass10");

		dao.add(user);

		System.out.println(user.getId() + " 등록 성공");

		UserDto user2 = dao.get(user.getId());
		System.out.println(user2.getName());

	}
}