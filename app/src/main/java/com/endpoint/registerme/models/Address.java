package com.endpoint.registerme.models;

public class Address {
    private static String address;

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Address.address = address;
    }
}
