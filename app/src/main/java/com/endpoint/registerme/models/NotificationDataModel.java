package com.endpoint.registerme.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {
    private int current_page;
    private List<NotificationModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<NotificationModel> getData() {
        return data;
    }

    public class NotificationModel implements Serializable{
       private int id;
               private String from_id;
        private String to_id;
        private String type;
               private int order_id;
               private long notification_date;
               private String status;
        private String is_read;
        private String notification_name;
        private String is_deleted;

        private String ar_notification_title;
        private String ar_notification_body;
        private String en_notification_title;
        private String en_notification_body;
        private String provider;

        public int getId() {
            return id;
        }

        public String getFrom_id() {
            return from_id;
        }

        public String getTo_id() {
            return to_id;
        }

        public String getType() {
            return type;
        }

        public int getOrder_id() {
            return order_id;
        }

        public long getNotification_date() {
            return notification_date;
        }

        public String getStatus() {
            return status;
        }

        public String getIs_read() {
            return is_read;
        }

        public String getNotification_name() {
            return notification_name;
        }

        public String getIs_deleted() {
            return is_deleted;
        }

        public String getAr_notification_title() {
            return ar_notification_title;
        }

        public String getAr_notification_body() {
            return ar_notification_body;
        }

        public String getEn_notification_title() {
            return en_notification_title;
        }

        public String getEn_notification_body() {
            return en_notification_body;
        }

        public String getProvider() {
            return provider;
        }
    }



}
