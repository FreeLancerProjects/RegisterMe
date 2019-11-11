package com.creativeshare.registerme.models;

import java.io.Serializable;
import java.util.List;

public class Order_Model implements Serializable {

        private List<Data> data;



    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
        {
          private int id;
                  private int type;
                  private String employee_id_fk;
                  private int user_id_fk;
                  private int status;
            private int available;

                  private String campany_id_fk;
                  private String link;
                  private String email;
                  private String password;
private long date;
            public int getId() {
                return id;
            }

            public int getType() {
                return type;
            }

            public String getEmployee_id_fk() {
                return employee_id_fk;
            }

            public int getUser_id_fk() {
                return user_id_fk;
            }

            public int getStatus() {
                return status;
            }

            public int getAvailable() {
                return available;
            }

            public String getCampany_id_fk() {
                return campany_id_fk;
            }

            public String getLink() {
                return link;
            }

            public String getEmail() {
                return email;
            }

            public String getPassword() {
                return password;
            }

            public long getDate() {
                return date;
            }
        }
}
