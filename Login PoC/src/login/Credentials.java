package login;

import java.io.Serializable;

import filter.BloomFilter;

/**
 * Container for user login information.
 * @author Reed
 *
 */
public class Credentials implements Serializable {
	private static final long serialVersionUID = 2810723588689745498L;
	private String username;
	private BloomFilter password;

	public Credentials(String username, BloomFilter pw) {
		this.username = username;
		password = pw;
	}

	public Credentials(String username, String password) {
		this.username = username;
		this.password = LoginServer.createFilter(BloomFilter.DEFAULT_SIZE).storeValue(password);
	}

	/**
	 * @return the username.
	 */
	public String getUsername() {
		//The best place I could find to put this, clears register tag from username
		if (username.length() > 10 && 
				username.substring(0, 10).equals("$register$")) { 
			return username.substring(10);
		} else {
			return username;
		}
	}

	/**
	 * @return the hashed password.
	 */
	protected BloomFilter getPassword() {
		return password;
	}
}
