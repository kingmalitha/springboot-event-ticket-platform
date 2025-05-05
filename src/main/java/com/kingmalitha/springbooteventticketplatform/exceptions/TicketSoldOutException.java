package com.kingmalitha.springbooteventticketplatform.exceptions;

public class TicketSoldOutException extends EventTicketException {
    public TicketSoldOutException() {
    }

    public TicketSoldOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TicketSoldOutException(Throwable cause) {
        super(cause);
    }

    public TicketSoldOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketSoldOutException(String message) {
        super(message);
    }
}
