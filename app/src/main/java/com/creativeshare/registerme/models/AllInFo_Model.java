package com.creativeshare.registerme.models;

import java.io.Serializable;
import java.util.List;

public class AllInFo_Model implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public  class Data implements Serializable
        {
            private List<EmailTypes>emailTypes;
private List<Gcompanies>Gcompanies;
private List<Scompanies>Scompanies;

            public List<EmailTypes> getEmailTypes() {
                return emailTypes;
            }

            public List<Data.Gcompanies> getGcompanies() {
                return Gcompanies;
            }

            public List<Data.Scompanies> getScompanies() {
                return Scompanies;
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
          public class   Gcompanies implements Serializable
            {
                private int id;
                    private String name;
                    private String logo;
                    private String address;
                    private String type;
                    private String available;

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }

                public String getLogo() {
                    return logo;
                }

                public String getAddress() {
                    return address;
                }

                public String getType() {
                    return type;
                }

                public String getAvailable() {
                    return available;
                }
            }
            public class   Scompanies implements Serializable
            {
                private int id;
                private String name;
                private String logo;
                private String address;
                private String type;
                private String available;

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }

                public String getLogo() {
                    return logo;
                }

                public String getAddress() {
                    return address;
                }

                public String getType() {
                    return type;
                }

                public String getAvailable() {
                    return available;
                }
            }
        }

}
