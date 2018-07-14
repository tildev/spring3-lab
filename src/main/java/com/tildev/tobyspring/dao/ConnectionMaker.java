/**
 * 
 */
package com.tildev.tobyspring.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author tildev
 * @date 2018. 7. 14.
 */
public interface ConnectionMaker {
	public Connection makeConnection() throws IOException, ClassNotFoundException, SQLException;
}
