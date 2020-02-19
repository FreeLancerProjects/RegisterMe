package com.endpoint.registerme.models;

import java.io.Serializable;
import java.util.List;

public class Profile_Order_Model implements Serializable {
    private List<Orders>orders;

    public List<Orders> getOrders() {
        return orders;
    }

    public class Orders  implements Serializable
    {
        private int id;
            private String type;
            private String employee_id_fk;
            private String user_id_fk;
            private String status;
            private String available;
            private String created_at;
            private String updated_at;
            private String job_link;
            private String company_name;
            private String company_logo;
            private String company_address;
               private String email_name;
        private String email_password;
        private String email_type_name;
        private String email_type_logo;
        private long date;
private String cv;
        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getEmployee_id_fk() {
            return employee_id_fk;
        }

        public String getUser_id_fk() {
            return user_id_fk;
        }

        public String getStatus() {
            return status;
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

        public String getJob_link() {
            return job_link;
        }

        public String getCompany_name() {
            return company_name;
        }

        public String getCompany_logo() {
            return company_logo;
        }

        public String getCompany_address() {
            return company_address;
        }

        public String getEmail_name() {
            return email_name;
        }

        public String getEmail_password() {
            return email_password;
        }

        public String getEmail_type_name() {
            return email_type_name;
        }

        public String getEmail_type_logo() {
            return email_type_logo;
        }

        public long getDate() {
            return date;
        }

        public String getCv() {
            return cv;
        }
    }
}
