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

	public Credentials(String username, BloomFilter pw) throws NameTooLongException {
		if (username.length() > 60) {
			throw new NameTooLongException("Username length of " + username.length() +
					" exceeds maximum of length 60");
		} else {
			this.username = username;
			password = pw;
		}
	}

	public Credentials(String username, String password) throws NameTooLongException {
		if (username.length() > 60) {
			throw new NameTooLongException("Username length of " + username.length() +
					" exceeds maximum of length 60");
		} else {
		this.username = username;
		this.password = LoginServer.createFilter(BloomFilter.DEFAULT_SIZE).storeValue(password);
		}
	}

	/**
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the hashed password.
	 */
	protected BloomFilter getPassword() {
		return password;
	}
}
