package com.creativeshare.registerme.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User user;

    public User getUser() {
        return user;
    }

    public class User implements Serializable {
        private int id;
                private String name;
                private String phone_code;
        private String phone;
        private String image;
        private String bannar_image;
        private String Validation_status;
        private String gender;
        private String software_type;
        private String available;
        private String email;
        private String email_verified_at;
        private String notes;
        private String other_info;
        private String qualification_id_fk;
        private String hand_graduation_id_fk;
        private String skills_id;
                private int is_login;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getImage() {
            return image;
        }

        public String getBannar_image() {
            return bannar_image;
        }

        public String getValidation_status() {
            return Validation_status;
        }

        public String getGender() {
            return gender;
        }

        public String getSoftware_type() {
            return software_type;
        }

        public String getAvailable() {
            return available;
        }

        public String getEmail() {
            return email;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public String getNotes() {
            return notes;
        }

        public String getOther_info() {
            return other_info;
        }

        public String getQualification_id_fk() {
            return qualification_id_fk;
        }

        public String getHand_graduation_id_fk() {
            return hand_graduation_id_fk;
        }

        public String getSkills_id() {
            return skills_id;
        }

        public int getIs_login() {
            return is_login;
        }
    }
}
