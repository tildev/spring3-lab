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
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

/**
 * 1. 템플릿 메소드 패턴 (template method pattern)
 * 슈퍼클래스에 기본적인 로직의 흐름을 만들고
 * 추상메소드나 오버라이딩이 가능한 protected 메소드 등으로 만들어
 * 서브 클래스에서 필요에 맞게 구현해서 사용하는 디자인 패턴
 * 
 * 2. 팩토리 메소드 패턴(factory method pattern)
 * 서브클래스에서 구체적인 오브젝트 생성 방법을 결정하게 하는 디자인 패턴
 */

/**
 * @author tildev
 * @date 2018. 7. 14.
 */
public class SimpleConnectionMaker {

	@Value("${jdbc.driver}")
	private String jdbcDriver;

	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${jdbc.username}")
	private String jdbcUsername;

	@Value("${jdbc.password}")
	private String jdbcPassword;

	/**
	 * connectionInfoInsert
	 * 
	 * @throws IOException
	 */
	private void connectionInfoInsert() throws IOException {
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
	 * getConnection
	 * 
	 * 문제점 : 자유로운 확장이 어렵다.
	 * 
	 * 1. getConnection 이라는 메소드 이름의 한정성.
	 * 메소드 명이 바뀌면 모든 Dao 의 connection 부분을 수정해야 한다.
	 * 2. UserDao 가 DB 커넥션을 제공하는 클래스가 어떤 것인지를 알아야 함.
	 * UserDao 에 SimpleConnectionMaker 라는 클래스의 인스턴스 변수까지 정의해 놓았기 때문.
	 * 
	 * 근본 원인 : UserDao가 바뀔수 있는 정보 (DB 커넥션) 의 클래스, 메소드에 대해 일일이 알고 있어야 함. (종속적)
	 * -> 해결방법? 인터페이스를 도입하자!
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
		connectionInfoInsert();

		Class.forName(jdbcDriver);
		Connection c = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

		return c;
	}
}
