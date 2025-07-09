package com.example.productviewapp.models;

/*
 * The Product class represents a product in your app with properties
 * like ID, name, description, seller, price, and image.
 * Implements Parcelable so it can be passed between Activities or Fragments.
 */

import android.os.Parcel;
import android.os.Parcelable;


public class Product implements Parcelable {

    // Class properties (fields) for each product
    private final int id;                 // Unique product ID
    private final String name;           // Name of the product
    private final String description;    // Description of the product
    private final String seller;         // Seller's name
    private final double price;          // Price of the product
    private byte[] image;                // Image data stored as a byte array (BLOB)

    /**
     * Standard constructor used when creating a Product from database or manually.
     *
     * @param id          Unique ID of the product
     * @param name        Product name
     * @param description Product description
     * @param seller      Seller's name
     * @param price       Product price
     * @param image       Product image as a byte array
     */
    public Product(int id, String name, String description,String seller, double price, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.image = image;
    }

    /**
     * Constructor used when recreating a Product from a Parcel.
     * Note: This only restores fields written in writeToParcel().
     *
     * @param in The Parcel containing the product data
     */
    protected Product(Parcel in) {
        id = in.readInt();               // Read ID from the parcel
        name = in.readString();          // Read name
        description = in.readString();   // Read description
        seller = in.readString();        // Read seller
        price = in.readDouble();         // Read price
    }

    /**
     * This CREATOR field is required for all Parcelable classes.
     * It's used to generate instances of the Product class from a Parcel.
     */
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in); // Calls the Parcel constructor
        }


        @Override
        public Product[] newArray(int size) {
            return new Product[size]; // Creates an array of Product
        }
    };

    // Getter methods for each field
    public int getId() {return id;}
    public String getName() {return name;}
    public String getDescription() { return description; }
    public String getSeller() { return seller; }
    public double getPrice() { return price; }
    public byte[] getImage() { return image; }

    /**
     * Required override for Parcelable.
     * Usually returns 0 unless your object has a special file descriptor.
     */
    public int describeContents() { return 0; }

    /**
     * Writes the object data into the Parcel.
     * This determines what data is saved when passing the object between Activities.
     *
     * @param parcel Parcel where the object is written to
     * @param flags  Additional flags (usually 0)
     */
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(seller);
        parcel.writeDouble(price);
    }
}
