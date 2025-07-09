package com.example.productviewapp.adapters;


import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.view.*;
import android.widget.*;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.productviewapp.models.Product;
import com.example.productviewapp.R;


import java.util.*;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> productList; // List of all products to display

    private final List<Product> selectedList = new ArrayList<>();  // List of products that the user has selected (via checkbox)

    /**
     * Constructor to create the adapter.
     * @param productList The products to display
     */
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    /**
     * Public method used to get products selected by the user.
     * @return List of selected products
     */
    public List<Product> getSelectedProducts() {
        return selectedList;
    }

    /**
     * Inner static ViewHolder class that holds references to each view in an item.
     * This improves performance by avoiding repeated findViewById() calls.
     */
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, description, seller;   // UI components for text
        ImageView image;                     // Product image
        CheckBox selectBox;                  // Checkbox to select the product

        /**
         * Constructor binds the views to the fields.
         * @param view The inflated item view.
         */
        public ProductViewHolder(View view) {
            super(view);
            // Locate views from item_product.xml
            name = view.findViewById(R.id.productName);
            price = view.findViewById(R.id.productPrice);
            description = view.findViewById(R.id.productDescription);
            seller = view.findViewById(R.id.productSeller);
            image = view.findViewById(R.id.productImage);
            selectBox = view.findViewById(R.id.checkBox);
        }
    }

    /**
     * Called when RecyclerView needs a new view to display an item.
     * Inflates the item_product.xml layout and wraps it in a ViewHolder.
     * @param parent Parent view (RecyclerView)
     * @param viewType Type of the new view (not used here)
     * @return A new ProductViewHolder instance
     */
    @NonNull
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the XML layout for a single product row
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(item);
    }

    /**
     * Called to populate the views in a ViewHolder with data for a specific position.
     * This is where you set texts, images, and checkbox state.
     * @param holder ViewHolder containing references to the item's views
     * @param position Index of the item in the list
     */
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // Get the product at this position
        Product product = productList.get(position);

        // Set name, price, and description fields
        holder.name.setText(product.getName());
        holder.price.setText("$" + product.getPrice());
        holder.description.setText(product.getDescription());
        holder.seller.setText("Sold by: " + product.getSeller());


        // Set image if available; otherwise use fallback/default image
        if (product.getImage() != null) {
            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length));
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background); // neutral fallback
        }

        // Set the checkbox based on whether this product is selected
        holder.selectBox.setChecked(selectedList.contains(product));

        // Handle user checking/unchecking the box
        holder.selectBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedList.add(product);
            else selectedList.remove(product);
        });
    }

    /**
     * Tells RecyclerView how many items are in the list.
     * @return Number of products in the data set
     */
    public int getItemCount() {
        return productList.size();
    }
}