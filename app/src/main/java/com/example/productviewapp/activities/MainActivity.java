package com.example.productviewapp.activities;

/**
 * The MainActivity displays a list of products and allows the user to select items.
 * It also populates the database and handles navigation to the second screen.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.productviewapp.R;
import com.example.productviewapp.adapters.ProductAdapter;
import com.example.productviewapp.database.ProductDatabaseHelper;
import com.example.productviewapp.models.Product;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView; // RecyclerView shows the list of products
    ProductAdapter adapter;  // Adapter to connect product data to the RecyclerView



    /**
     * Called when the activity is created.
     * Sets up the layout, populates data, and handles user interaction.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the UI layout defined in activity_main.xml

        // Create database helper to manage SQLite operations
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);

        // Reset database for a fresh catalog
        dbHelper.deleteAllProducts();  //  wipes previous data
        populateDatabase(dbHelper);   //   inserts updated products

        // Load products from database and display them
        List<Product> productList = dbHelper.getAllProducts();
        Log.d("MainActivity", "Real products loaded: " + productList.size());


        recyclerView = findViewById(R.id.recyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);

        // When "Next" button clicked
        findViewById(R.id.nextButton).setOnClickListener(v -> {
            Log.d("MainActivity", "Next button clicked!");
            ArrayList<Product> selectedProducts = new ArrayList<>(adapter.getSelectedProducts());
            if (selectedProducts.size() < 3) {
                Toast.makeText(this, getString(R.string.no_products_selected), Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass selected products to SecondActivity
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putParcelableArrayListExtra("selectedProducts", selectedProducts);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.e("MainActivity", "Intent failed", e);
                Toast.makeText(this, "Could not launch next screen.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Converts an image resource into a byte array.
     * This is used to store images in the database.
     * @param resId Resource ID of the image
     * @return Byte array containing image data
     */
    private byte[] getImageByteArray(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    /**
     * Inserts a fixed set of products into the database.
     * Called once when app starts to initialize the catalog.
     * @param dbHelper The database helper instance
     */    private void populateDatabase(ProductDatabaseHelper dbHelper) {
        dbHelper.insertProduct(getString(R.string.cam_name), getString(R.string.cam_description), getString(R.string.am), 99.99, getImageByteArray(R.drawable.cameras));
        dbHelper.insertProduct(getString(R.string.coffee_name), getString(R.string.coffee_description), getString(R.string.tg), 549.99, getImageByteArray(R.drawable.expresso));
        dbHelper.insertProduct(getString(R.string.fire_name), getString(R.string.fire_description), getString(R.string.wm), 24.99, getImageByteArray(R.drawable.firestick));
        dbHelper.insertProduct(getString(R.string.mat_name), getString(R.string.mat_description), getString(R.string.am), 189.01, getImageByteArray(R.drawable.mattress));
        dbHelper.insertProduct(getString(R.string.case_name), getString(R.string.case_description), getString(R.string.eb), 7.99, getImageByteArray(R.drawable.phonecase));
        dbHelper.insertProduct(getString(R.string.pool_name), getString(R.string.pool_description), getString(R.string.jl), 489.99, getImageByteArray(R.drawable.pool));
        dbHelper.insertProduct(getString(R.string.tab_name), getString(R.string.tab_description), getString(R.string.eb), 69.99, getImageByteArray(R.drawable.tablet));
        dbHelper.insertProduct(getString(R.string.tv_name), getString(R.string.tv_description), getString(R.string.am), 569.99, getImageByteArray(R.drawable.tv));
        dbHelper.insertProduct(getString(R.string.vac_name), getString(R.string.vac_description), getString(R.string.jl), 149.01, getImageByteArray(R.drawable.vacuum));
        dbHelper.insertProduct(getString(R.string.time_name), getString(R.string.time_description), getString(R.string.wm), 549.99, getImageByteArray(R.drawable.watch));
    }
}
