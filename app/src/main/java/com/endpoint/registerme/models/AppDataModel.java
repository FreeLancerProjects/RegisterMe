package com.endpoint.registerme.models;

import java.io.Serializable;

public class AppDataModel implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
    private int id;

            private String ar_content;
            private String en_content;

    public int getId() {
        return id;
    }

    public String getAr_content() {
        return ar_content;
    }

    public String getEn_content() {
        return en_content;
    }
}}
