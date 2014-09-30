package blossom.exception;

public class TopLevelBlossomException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String reason;

    public TopLevelBlossomException(final String reason) {
        super();
        this.reason = reason;
    }

    public TopLevelBlossomException(final Throwable throwable, final String reason) {
        super(throwable);
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    @Override
    public String getMessage() {
        return this.reason;
    }
}
