package com.endpoint.registerme.models;

import java.io.Serializable;
import java.util.List;

public class ImageTypeModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
    {
        private int id;
            private String ar_title;
        private String en_title;
        private String type;
        private String post;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getType() {
            return type;
        }

        public String getPost() {
            return post;
        }
    }
}
