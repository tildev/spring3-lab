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
 * @author tildev
 * @date 2018. 7. 14.
 */
public class NConnectionMaker implements ConnectionMaker {
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

	@Override
	public Connection makeConnection() throws IOException, ClassNotFoundException, SQLException {
		connectionInfoInsert();

		Class.forName(jdbcDriver);
		Connection c = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

		return c;
	}

}
