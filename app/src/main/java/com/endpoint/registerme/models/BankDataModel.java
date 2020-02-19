package com.endpoint.registerme.models;

import java.io.Serializable;
import java.util.List;

public class BankDataModel implements Serializable {

    private List<BankModel> data;

    public List<BankModel> getData() {
        return data;
    }

    public class BankModel implements Serializable
    {
         private int id;
                 private String iban;
        private String number;
        private String name;
private String bank_name;

        public int getId() {
            return id;
        }

        public String getIban() {
            return iban;
        }

        public String getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public String getBank_name() {
            return bank_name;
        }
    }
}
