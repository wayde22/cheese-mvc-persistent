package org.launchcode.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private int Id;

    @NotNull
    @Size(min=3, max=15)
    private String name;

    @OneToMany //one cat. many cheeses
    @JoinColumn(name = "category_id")// tells Hibernate to use the category_id column of the cheese table to determine which cheese belong to a given category
    private List<Cheese> cheeses = new ArrayList<>();

    public Category(){ //used by Hibernate in the process of creating objects from data retrieved from the database

    }

    public Category(String name) {
        this.name = name;
    }


    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
