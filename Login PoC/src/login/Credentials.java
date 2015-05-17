package login;
/**
 * Container for user login information.
 * @author Reed
 *
 */
public class Credentials {
	private String username;
	private String password;

	public Credentials(String username, String pw) {
		this.username = username;
		password = pw;
	}

	/**
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password.
	 */
	protected String getPassword() {
		return password;
	}
}
