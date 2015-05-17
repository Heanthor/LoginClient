package login;

/**
 * Contains response codes for various outcomes of authentication.
 * @author Reed
 *
 */
public class AuthenticateResponse {
	public static final int RESPONSE_USERNAME_NOT_FOUND = 1;
	public static final int RESPONSE_AUTHENTICATED = 2;
	public static final int RESPONSE_WRONG_PASSWORD = 3;
	public static final int RESPONSE_DEBUG = 4;
	public static final int RESPONSE_ERROR = 5;

	/**
	 * The authentication result.
	 */
	public int reponseCode;

	public AuthenticateResponse(int code) {
		reponseCode = code;
	}
}
