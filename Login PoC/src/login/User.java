package login;

import java.io.Serializable;

/**
 * A user encapsulates a set of Credentials, and a PermissionLevel
 * to designate their allowed permissions.
 * @author Reed
 *
 */
public class User implements Serializable {
	private static final long serialVersionUID = -439345683340920287L;
	private int permission;
	private Credentials info;
	
	public User(int permissionLevel, Credentials info) {
		this.permission = permissionLevel;
		this.info = info;
	}

	/**
	 * @return the permission
	 */
	public int getPermission() {
		return permission;
	}

	/**
	 * @return login credentials for this User.
	 */
	public Credentials getCredentials() {
		return info;
	}
}
