package hocqu.entities;

import java.util.Calendar;

public class Product {
    private String name;
    private Calendar expirationDate;

    public Product(String name, Calendar expirationDate) {
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }
}
