package login;

import filter.BloomFilter;

/**
 * Container for user login information.
 * @author Reed
 *
 */
public class Credentials {
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
		return username;
	}

	/**
	 * @return the hashed password.
	 */
	protected BloomFilter getPassword() {
		return password;
	}
}
