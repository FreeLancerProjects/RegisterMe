package com.creativeshare.registerme.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User user;

    public class User implements Serializable {
        private int id;
        private String email;
        private String username;
        private String phone_code;
        private String phone;
        private String gender;
        private String logo;

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getGender() {
            return gender;
        }

        public String getLogo() {
            return logo;
        }

    }
}
