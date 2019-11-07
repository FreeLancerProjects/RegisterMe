package com.creativeshare.registerme.models;

import java.io.Serializable;
import java.util.List;

public class Slider_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public  class  Data implements Serializable
    {
        private int id;

            private String image;
private int type;

        public int getId() {
            return id;
        }



        public String getImage() {
            return image;
        }

        public int getType() {
            return type;
        }
    }
}
