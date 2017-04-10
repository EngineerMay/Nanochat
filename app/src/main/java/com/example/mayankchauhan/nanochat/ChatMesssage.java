package com.example.mayankchauhan.nanochat;

/**
 * Created by mayankchauhan on 20/02/17.
 */

public class ChatMesssage {

    String name;
    String messag;

    public ChatMesssage() {
    }

    public ChatMesssage(String name, String messag) {
        this.name = name;
        this.messag = messag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessag() {
        return messag;
    }

    public void setMessag(String messag) {
        this.messag = messag;
    }
}
