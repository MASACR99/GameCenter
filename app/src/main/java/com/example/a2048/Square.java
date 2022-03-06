package com.example.pegsolitaire;

public class Square {
    private boolean valid;
    private boolean pegged;

    public Square(boolean valid, boolean pegged){
        this.valid = valid;
        this.pegged = pegged;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isPegged() {
        return pegged;
    }

    public void setPegged(boolean pegged) {
        this.pegged = pegged;
    }
}
