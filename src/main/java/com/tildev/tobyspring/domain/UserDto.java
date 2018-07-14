/**
 * 
 */
package com.tildev.tobyspring.domain;

/**
 * @author tildev
 * @date 2018. 7. 14.
 */
public class UserDto {
	String id;
	String name;
	String password;

	/**
	 * 생성자
	 */
	public UserDto() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param password
	 */
	public UserDto(String id, String name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
