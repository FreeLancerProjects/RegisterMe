package com.creativeshare.registerme.models;

import java.io.Serializable;
import java.util.List;

public class AllInFo_Model implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable
        {
            private List<EmailTypes>emailTypes;

            public List<EmailTypes> getEmailTypes() {
                return emailTypes;
            }

            public class  EmailTypes implements Serializable
            {
                private int id;
                    private String name;
                    private String logo;

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }

                public String getLogo() {
                    return logo;
                }
            }
        }

}
