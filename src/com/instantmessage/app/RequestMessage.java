package com.instantmessage.app;

import java.io.Serializable;

public class RequestMessage implements Serializable {
    private String messageType; // history atau useronline
    private String key; // history key (ignored if user online)

    // not kepake

    // costructor jika broadcast
    public RequestMessage(String messageType, String key) {
        this.messageType = messageType;
        this.key = key;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getKey() {
        return key;
    }
}
