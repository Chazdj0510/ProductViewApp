package com.example.productviewapp.database;

/*
 * ProductDatabaseHelper manages the creation, upgrade,
 * and CRUD (Create, Read, Update, Delete) operations
 * on the products database.
 */

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import com.example.productviewapp.models.Product;
import java.util.*;


public class ProductDatabaseHelper extends SQLiteOpenHelper {
    // Constants for the database name and version
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor - calls the parent SQLiteOpenHelper constructor
     * @param context Application context for database access
     */
    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * Creates the "products" table with columns for product details.
     * @param db SQLiteDatabase instance where commands are executed
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE products (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +   // Unique product ID auto-incremented
                        "name TEXT," +                              // Product name (text)
                        "description TEXT," +                       // Product description (text)
                        "seller TEXT," +                            // Seller name (text)
                        "price REAL," +                             // Price (decimal number)
                        "image BLOB)"                              // Image stored as binary large object
        );
    }

    /**
     * Called when the database needs to be upgraded.
     * Drops the old table and creates a new one.
     * This will erase existing data on upgrade.
     * @param db SQLiteDatabase instance
     * @param oldVersion previous database version number
     * @param newVersion new database version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete existing table if it exists
        db.execSQL("DROP TABLE IF EXISTS products");
        // Recreate the database
        onCreate(db);
    }


    /**
     * Inserts a new product into the "products" table.
     * @param name product name
     * @param description product description
     * @param seller seller name
     * @param price product price
     * @param image product image as byte array (blob)
     */
    public void insertProduct(String name, String description, String seller, double price, byte[] image) {
        // Get writable access to the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Use ContentValues to store key-value pairs for the columns
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("seller", seller);
        values.put("price", price);
        values.put("image", image);

        // Insert the new row into the products table
        db.insert("products", null, values);
//        db.close();
    }

    /**
     * Retrieves all products from the database.
     * @return List of Product objects representing all entries in the database
     */
    public List<Product> getAllProducts() {
        // List to store retrieved products
        List<Product> productList = new ArrayList<>();

        // Try-with-resources to automatically close database and cursor
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM products", null)) {

            // Iterate through all rows in the cursor
            while (cursor.moveToNext()) {
                // Retrieve each column's data using the column name
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String seller = cursor.getString(cursor.getColumnIndexOrThrow("seller"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

                // Create a new Product object and add it to the list
                productList.add(new Product(id, name, description, seller, price, image));
            }
        }

        // Return the full list of products
        return productList;
    }

    /**
     * Deletes all products from the "products" table.
     * Use with caution as this removes all data.
     */
    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable access to the database
        db.delete("products", null, null); // Delete all rows in the products table without any WHERE clause
        db.close(); // Close the database after operation
    }

    /**
     * Retrieves a single product by its unique ID.
     * @param id The product's unique identifier
     * @return Product object if found, null otherwise
     */
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase(); // Open readable database
        // Query database for product with matching ID using parameterized query to prevent SQL injection
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE id = ?", new String[]{String.valueOf(id)});

        Product result = null;
        // If a matching row is found, extract its data
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String seller = cursor.getString(cursor.getColumnIndexOrThrow("seller"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

            // Create a Product object from retrieved data
            result = new Product(id, name, description, seller, price, image);
        }
        // Close cursor and database to free resources
        cursor.close();
        db.close();

        // Return the found product or null if none found
        return result;
    }
}
