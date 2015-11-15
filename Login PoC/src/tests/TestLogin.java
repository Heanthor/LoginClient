package tests;

import login.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestLogin {
	LoginServer u = new LoginServer();
	User heanthor;
	
	public TestLogin() {
		try {
			heanthor = new User(PermissionLevel.ADMIN, new Credentials("Heanthor", "test"));
		} catch (NameTooLongException e) {
			e.printStackTrace();
		}
	}

	/* Proof of concept on how this is meant to be used */
	@Test
	public void testLoginKnownUser() {
		AuthenticateResponse r = u.authenticate(heanthor.getCredentials());
		assertEquals(AuthenticateResponse.RESPONSE_AUTHENTICATED, r.reponseCode);
	}

	@Test
	public void testLoginWrongPassword() throws NameTooLongException {
		String username = "Heanthor";
		String password = "wrong";

		AuthenticateResponse r = u.authenticate(new Credentials(username, password));
		assertEquals(AuthenticateResponse.RESPONSE_WRONG_PASSWORD, r.reponseCode);
	}

	@Test
	public void testLoginUnknownUser() throws NameTooLongException {
		String username = "abcdx";
		String password = "xxx";

		AuthenticateResponse r = u.authenticate(new Credentials(username, password));
		assertEquals(AuthenticateResponse.RESPONSE_USERNAME_NOT_FOUND, r.reponseCode);
	}

	@Test
	public void testNewUser() throws IOException, NameTooLongException {
		String username = "testUser";
		String password = "test";

		assertTrue(u.newUser(new Credentials(username, password)));
	}

	@Test
	public void testUserList() throws NameTooLongException {
		String username = "_list_users";
		String password = "";

		AuthenticateResponse r = u.authenticate(new Credentials(username, password));
		assertEquals(AuthenticateResponse.RESPONSE_DEBUG, r.reponseCode);
		//Heanthor, testUser
	}
}
