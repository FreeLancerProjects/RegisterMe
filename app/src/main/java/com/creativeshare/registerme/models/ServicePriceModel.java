package com.creativeshare.registerme.models;

import java.io.Serializable;

public class ServicePriceModel  implements Serializable {
    private int id;
            private String create_cv;
            private String create_email;
            private String apply_job;

    public int getId() {
        return id;
    }

    public String getCreate_cv() {
        return create_cv;
    }

    public String getCreate_email() {
        return create_email;
    }

    public String getApply_job() {
        return apply_job;
    }
}
