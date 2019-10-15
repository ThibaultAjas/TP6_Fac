/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingwithhsqldb;

/**
 *
 * @author Dalfrak
 */
public class Product {

    private int id;
    private String name;
    private int price;

    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return this.id + " | " + this.name + " | " + this.price;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == Product.class && ((Product) obj).getId() == this.id && ((Product) obj).getName().equals(this.name) && ((Product) obj).getPrice() == this.price;
    }

}
