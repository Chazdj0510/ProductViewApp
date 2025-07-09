package com.example.productviewapp.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.productviewapp.R;
import com.example.productviewapp.adapters.ProductAdapter;
import com.example.productviewapp.database.ProductDatabaseHelper;
import com.example.productviewapp.models.Product;


import java.util.ArrayList;
public class SecondActivity extends AppCompatActivity {
    RecyclerView recyclerView; // RecyclerView to display selected products
    ProductAdapter adapter; // Adapter to bind products to RecyclerView
    Button emailButton; // Button to trigger email sending
    ArrayList<Product> fullProductList = new ArrayList<>(); // List of fully-loaded products with all details from database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Create database helper to retrieve full product details
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);

        // Retrieve the list of selected products from Intent extras
        ArrayList<Product> passedList = getIntent().getParcelableArrayListExtra("selectedProducts");

        if (passedList != null) {
            // For each product in passed list, load full record from database
            for (Product p : passedList) {
                Product full = dbHelper.getProductById(p.getId());
                if (full != null) {
                    fullProductList.add(full);
                } else {
                    Log.e("SecondActivity", "Failed to load product with ID: " + p.getId());
                }
            }
        } else {
            // If nothing was passed, show a message
            Toast.makeText(this, "No products passed to SecondActivity", Toast.LENGTH_SHORT).show();
        }

        // Find RecyclerView in the layout
        recyclerView = findViewById(R.id.recyclerViewSecond);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create adapter using the loaded products
        adapter = new ProductAdapter(fullProductList);
        recyclerView.setAdapter(adapter);

        // Find email button and set its click behavior
        emailButton = findViewById(R.id.emailButton);
        emailButton.setOnClickListener(v -> sendEmailWithProducts());
    }

    // This method prepares and launches an email Intent to send selected product info
    @SuppressLint({"QueryPermissionsNeeded", "NotifyDataSetChanged"})
    private void sendEmailWithProducts() {
        // Check that there are products to email
        if (fullProductList.isEmpty()) {
            Toast.makeText(this, "No products to email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build the body text for the email
        StringBuilder emailBody = new StringBuilder("Selected Products:\n\n");
        for (Product p : fullProductList) {
            emailBody.append(p.getName())
                    .append(" - $").append(p.getPrice())
                    .append("\n").append(p.getDescription())
                    .append("\n\n");
        }

        // Email subject and body
        String subject = "My Selected Products";
        String body = emailBody.toString();

        // Encode for URI
        String uriText = "mailto:sweng888mobileapps@gmail.com" +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(body);

        Uri uri = Uri.parse(uriText);

        // Create an implicit Intent to open the user's email app
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(uri);

        // Make sure there is an app available to handle this Intent
        if (intent.resolveActivity(getPackageManager()) == null) {
            // Launch email app
            startActivity(intent);

            // Show Confirmation
            Toast.makeText(this, "Email launched! Check your mail app to send.", Toast.LENGTH_LONG).show();

            // Clear only the fullProductList (SecondActivity list)
            fullProductList.clear();
            adapter.notifyDataSetChanged();

            // Navigate back to MainActivity
            Intent backIntent = new Intent(this, MainActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish(); // closes SecondActivity

        } else {
            // No email app installed
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}
