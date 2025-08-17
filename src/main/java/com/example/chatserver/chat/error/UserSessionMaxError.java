package com.example.chatserver.chat.error;

public class UserSessionMaxError extends IllegalStateException {

    public UserSessionMaxError(String message) {
        super(message);
    }
}
