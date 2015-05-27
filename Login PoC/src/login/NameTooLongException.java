package login;

public class NameTooLongException extends Exception {

	private static final long serialVersionUID = 5744958520150195131L;

	public NameTooLongException() {
	}

	public NameTooLongException(String arg0) {
		super(arg0);
	}

	public NameTooLongException(Throwable arg0) {
		super(arg0);
	}

	public NameTooLongException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NameTooLongException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
