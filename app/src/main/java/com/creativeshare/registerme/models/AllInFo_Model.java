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
            private List<Quallifcation> quallifcation;
            private List<EmailTypes>emailTypes;
private List<Gcompanies>Gcompanies;
private List<Scompanies>Scompanies;
private List<Skills> skills;
private List<HandGraduations> handGraduations;
            public List<EmailTypes> getEmailTypes() {
                return emailTypes;
            }

            public List<Data.Gcompanies> getGcompanies() {
                return Gcompanies;
            }

            public List<Data.Scompanies> getScompanies() {
                return Scompanies;
            }

            public List<Quallifcation> getQuallifcation() {
                return quallifcation;
            }

            public List<Skills> getSkills() {
                return skills;
            }

            public List<HandGraduations> getHandGraduations() {
                return handGraduations;
            }

            public static class Quallifcation implements Serializable
            {
                private int id;
                    private String ar_title;
                    private String en_title;

                private String available;
                private String created_at;
                private String updated_at;

                public Quallifcation(String ar_title, String en_title) {
                    this.ar_title = ar_title;
                    this.en_title = en_title;
                }

                public int getId() {
                    return id;
                }

                public String getAr_title() {
                    return ar_title;
                }

                public String getEn_title() {
                    return en_title;
                }

                public String getAvailable() {
                    return available;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }
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
            public static class Skills implements Serializable
            {
                private int id;
                    private String ar_title;
                private String en_title;
                private String available;

                public Skills(String ar_title, String en_title) {
                    this.ar_title = ar_title;
                    this.en_title = en_title;
                }

                public int getId() {
                    return id;
                }

                public String getAr_title() {
                    return ar_title;
                }

                public String getEn_title() {
                    return en_title;
                }

                public String getAvailable() {
                    return available;
                }
            }
             public static class HandGraduations implements Serializable
            {
                private int id;
                private String ar_title;
                private String en_title;
                private String available;

                public HandGraduations(String ar_title, String en_title) {
                    this.ar_title = ar_title;
                    this.en_title = en_title;
                }

                public int getId() {
                    return id;
                }

                public String getAr_title() {
                    return ar_title;
                }

                public String getEn_title() {
                    return en_title;
                }

                public String getAvailable() {
                    return available;
                }
            }
        }

}
