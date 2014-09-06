package blossm.exception;

public class TopLevelBlossomException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String reason;

	public TopLevelBlossomException(final String reason) {
		super();
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
}
