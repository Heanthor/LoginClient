package login;
import filter.BloomFilter;
import filter.IllegalSizeException;
import hashes.*;

import java.io.*;
import java.util.HashMap;

/**
 * Provides methods for creating and authenticating users.
 * Debug commands (entered as username): 
 * _delete removes temporary file.
 * _tempdir prints temporary directory
 * _list_users lists users currently stored.
 * @author Reed
 */
public class LoginServer {
	private HashMap<String, BloomFilter> vals; //<Username, BloomFilter of password>
	private String tempDir = System.getProperty("java.io.tmpdir"); //System's temporary directory
	private String saveLocation = tempDir + "users.ser"; //Default save location

	/**
	 * On creation, loads the user list into memory, whether from the present file, or
	 * by creating a new one.
	 */
	public LoginServer() {
		vals = getFromFile(saveLocation);
	}

	/**
	 * @param saveLocation - The location of the stored users list. This string must
	 * have "users.ser" at the end.
	 */
	public LoginServer(String saveLocation) {
		this.saveLocation = saveLocation;
		vals = getFromFile(saveLocation);
	}

	/**
	 * Instead of loading from a file, supply a list from memory.
	 * @param vals - The user list.
	 */
	public LoginServer(HashMap<String, BloomFilter> vals) {
		this.vals = vals;
	}

	/**
	 * Creates a new BloomFilter for use with a new password.
	 * Size: 100, number of hashes: 5.
	 * @return Created filter.
	 * null if number of hashes does not match numHashes.
	 */
	public static BloomFilter createFilter(int size) {
		BloomFilter m = new BloomFilter(size);
		m.print(false);

		//Add hashes
		try {
			m.addHash(new Hash1("One", size)).
			addHash(new Hash2("Two", size)).
			addHash(new Hash3("Three", size)).
			addHash(new Hash4("Four", size)).
			addHash(new Hash5("Five", size));
		} catch (IllegalSizeException e) {
			e.printStackTrace();
		}

		return m;
	}

	/**
	 * Retrieves the <username, password> HashMap from the designated file.
	 * @param fileName - File to search for serialized object in.
	 * @return The retrieved object, or a new HashMap if the file is not found.
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, BloomFilter> getFromFile(String fileName) {
		//TODO get the file from the internet.
		try {
			FileInputStream b = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(b);

			HashMap<String, BloomFilter> in = (HashMap<String, BloomFilter>) ois.readObject();

			ois.close();
			return in;
		} catch (IOException | ClassNotFoundException e) { //File doesn't exist
			System.out.println("Unable to find previous username/password table. "
					+ "Using new table.");
			return new HashMap<String, BloomFilter>();
		}
	}

	/**
	 * Saves the argument to the given file.
	 * @param out - The HashMap to serialize.
	 * @param fileName - The file to save the object to.
	 * @return True if operation succeeds, false otherwise.
	 */
	@SuppressWarnings("rawtypes")
	private boolean serialize(HashMap out, String fileName) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);   

			oos.writeObject(out);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Adds a new user to the stored database of users, and update said database.
	 * If the user was already in the list, this method should do nothing to the
	 * database.
	 * @param in - The Credentials object for the new user.
	 * @return True if new user created, false if user already exists.
	 */
	public boolean newUser(Credentials in) {
		if (vals.put(in.getUsername(),
				in.getPassword()) == null) {
			serialize(vals, saveLocation);
			
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the user whose credentials are passed is contained within the list
	 * of users, and if their password matches the stored one.
	 * @return AuthenticateResponse object, depending on the results of the
	 * authentication or debug command.
	 */
	public AuthenticateResponse authenticate(Credentials in) {
		String username = in.getUsername();

		/*
		 * Debug Commands
		 */

		if (username.substring(0, 1).equals("_")) {
			if (username.equals("_delete")) { // Clears temp dir
				deleteUsersFile();
				return new AuthenticateResponse(AuthenticateResponse.RESPONSE_DEBUG);
			} else if (username.equals("_tempdir")) { // Temporary directory location
				System.out.println(saveLocation);
				return new AuthenticateResponse(AuthenticateResponse.RESPONSE_DEBUG);
			} else if (username.equals("_list_users")) { // User List
				System.out.println("User List: ");
				for (String s: vals.keySet()) {
					System.out.println(s);
				}

				return new AuthenticateResponse(AuthenticateResponse.RESPONSE_DEBUG);
			} else {
				System.err.println("Invalid debug command: " + username);
				return new AuthenticateResponse(AuthenticateResponse.RESPONSE_ERROR);
			}
		}

		if (!vals.containsKey(username)) {
			return new AuthenticateResponse(AuthenticateResponse.RESPONSE_USERNAME_NOT_FOUND);
		} else { //Contains key
			BloomFilter ret = vals.get(username);

			if (ret.equals(in.getPassword())) { // Password is correct
				return new AuthenticateResponse(AuthenticateResponse.RESPONSE_AUTHENTICATED);
			} else { // Password is incorrect
				return new AuthenticateResponse(AuthenticateResponse.RESPONSE_WRONG_PASSWORD);
			}
		}
	}

	/**
	 * Deletes file located at path. Prints status to console.
	 * @param path - Path to file to delete.
	 * @return True if file is deleted successfully, false otherwise.
	 */
	private boolean deleteUsersFile() {
		File file = new File(saveLocation);

		if (file.delete()) {
			System.out.println("Users purged.");
			return true;
		} else {
			System.out.println("Delete operation failed.");
			return false;
		}
	}
}