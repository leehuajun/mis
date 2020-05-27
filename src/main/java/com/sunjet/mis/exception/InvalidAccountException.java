package com.sunjet.mis.exception;

/**
 * Created by lhj on 16/7/2.
 * <p>
 * 帐号无效异常
 */
public class InvalidAccountException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidAccountException(String message) {
        //super(message);
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "帐号无效!";
    }
}
