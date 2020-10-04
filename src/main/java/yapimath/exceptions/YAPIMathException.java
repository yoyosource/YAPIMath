package yapimath.exceptions;

public class YAPIMathException extends RuntimeException {

    public YAPIMathException() {
        super();
    }

    public YAPIMathException(String message) {
        super(message);
    }

    public YAPIMathException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIMathException(Throwable cause) {
        super(cause);
    }

    protected YAPIMathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
