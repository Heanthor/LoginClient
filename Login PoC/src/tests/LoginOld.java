package tests;
import filter.BloomFilter;
import filter.IllegalSizeException;
import hashes.*;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Runs login script test. Has methods that can be used elsewhere. Users/hashed passwords are
 * stored in system's temporary directory.
 * Debug commands (enter as username): 
 * _delete removes temporary file.
 * _tempdir prints temporary directory
 * _list_users lists users currently stored.
 * @deprecated Old version of this class for testing.
 * @author Reed
 *
 */
public class LoginOld {
	private HashMap<String, BloomFilter> vals; //<Username, BloomFilter of password>
	private String tempDir = System.getProperty("java.io.tmpdir"); //System's temporary directory
	private int numHashes = 5; //Number of hashes this Login instance is designed to work with.

	/**
	 * On creation, loads the user list into memory, whether from the present file, or
	 * by creating a new one.
	 */
	public LoginOld() {
		vals = getFromFile(tempDir + "users.ser");
	}

	public static void main(String[] args) {
		LoginOld l = new LoginOld();

		while(l.authenticate()) {} //Authenticates over and over for debugging
	}

	/**
	 * Creates a new BloomFilter for use with a new password.
	 * Size: 100, Hashes: 5.
	 * @return Created filter, or
	 * null if number of hashes does not match numHashes.
	 */
	public BloomFilter createFilter() {
		int size = 100;
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

		if (m.numHashes == this.numHashes) {
			return m;
		} else {
			return null;
		}
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
	 * Runs the authentication tree. 
	 * Asks for username, if it is provided but not in the table, it asks to create a username and pw.
	 * If username is in the table, it asks for the password, falling out at any point if input is incorrect.
	 * @return True if authenticated, false if not.
	 * 
	 */
	private boolean authenticate() {
		System.out.print("Enter username: ");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String username = sc.nextLine();

		/*
		 * Debug Commands
		 */

		if (username.substring(0, 1).equals("_")) {
			if (username.equals("_delete")) { // Clears temp dir
				deleteUsersFile(tempDir + "users.ser");
				System.exit(0);
			} else if (username.equals("_tempdir")) { // Temporary directory location
				System.out.println(tempDir + "users.ser");
				System.exit(0);
			} else if (username.equals("_list_users")) { // User List
				System.out.println("User List: ");
				for (String s: vals.keySet()) {
					System.out.println(s);
				}
				System.exit(0);
			} else {
				System.out.println("Invalid debug command: " + username);
				System.exit(1);
			}
		}

		if (!vals.containsKey(username)) {
			System.out.print("Username not found. Register? (y/n): ");

			if (sc.nextLine().equals("y")) { // Enter new account
				System.out.print("Enter password: ");
				String password = sc.nextLine();

				BloomFilter b = createFilter();
				b.storeValue(password);
				vals.put(username, b); // Store new username/pw combination

				System.out.println("Thank you, u:" + username + ". Account created.");
				serialize(vals, tempDir + "users.ser"); //Save to file

				secret();

				return true;
			} else { // No account, no register
				System.out.println("Exiting.");

				return false;
			}
		} else { //Contains key
			System.out.print("Password: ");
			String tempPW = sc.nextLine();

			BloomFilter ret = vals.get(username);

			if (ret.retrieve(tempPW)) { // Password is correct
				System.out.println("Thank you, u:" + username + ". Authenticated.");
				secret();

				return true;
			} else { // Password is incorrect
				System.out.print("Incorrect password.");

				return false;
			}
		}
	}

	/**
	 * Deletes file located at path. Prints status to console.
	 * @param path - Path to file to delete.
	 * @return True if file is deleted successfully, false otherwise.
	 */
	private boolean deleteUsersFile(String path) {
		File file = new File(path);

		if (file.delete()) {
			System.out.println("Users purged.");
			return true;
		} else {
			System.out.println("Delete operation failed.");
			return false;
		}
	}
	
	// Getters and Setters
	public int getNumHashes() {
		return numHashes;
	}

	public void setNumHashes(int numHashes) {
		this.numHashes = numHashes;
	}

	/**
	 * This is what you get if you log in correctly.
	 * Can be easily replaced with a call to whatever other useful function necessary.
	 */
	private void secret() {
		System.out.println("\n            _,'|             _.-''``-...___..--';) \n" +
				"           /_ \'.      __..-' ,      ,--...--'''\n" +
				"          <\\    .`--'''       `     /'\n" +
				"           `-';'               ;   ; ;\n" +
				"     __...--''     ___...--_..'  .;.'\n" +
				"    (,__....----'''       (,..--''            ");
	}
}