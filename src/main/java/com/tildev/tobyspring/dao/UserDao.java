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

	private ConnectionMaker connectionMaker;

	/**
	 * @param simpleConnectionMaker
	 */
	public UserDao() {
		// 여전히 클래스 안에서 종속된다. 수정 필요!
		connectionMaker = new NConnectionMaker();
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
		Connection c = connectionMaker.makeConnection();

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
		Connection c = connectionMaker.makeConnection();

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
		user.setId("id12");
		user.setName("이름12");
		user.setPassword("pass12");

		dao.add(user);

		System.out.println(user.getId() + " 등록 성공");

		UserDto user2 = dao.get(user.getId());
		System.out.println(user2.getName());

	}
}