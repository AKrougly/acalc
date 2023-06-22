package com.example.acalc;

public class Subtotal {
    private String result;
    Subtotal() {
        clearSubtotal();
    }
    void clearSubtotal() {
        result = "";
    }
    String readSubtotal() {
        return result == "" ? "0" : result;
    }
    void setSubtotal(String res) {
        result = res;
    }
}
