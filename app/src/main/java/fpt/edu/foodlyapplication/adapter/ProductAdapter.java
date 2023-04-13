package fpt.edu.foodlyapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.interfaces.onItemProductClick;
import fpt.edu.foodlyapplication.model.Product;
import fpt.edu.foodlyapplication.utils.ServerURLManager;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ArrayList<Product> list;
    private onItemProductClick onItemProductClick;

    public ProductAdapter(ArrayList<Product> list, onItemProductClick onItemProductClick) {
        this.list = list;
        this.onItemProductClick = onItemProductClick;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = list.get(position);
        if (product == null) {
            return;
        }
        if (product.getImage().substring(0, 4).equals("http")) {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.load_image)
                    .into(holder.productImg);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(ServerURLManager.URL_BASE + "/" + product.getImage())
                    .placeholder(R.drawable.load_image)
                    .into(holder.productImg);
        }

        holder.productName.setText(product.getName());
        holder.productID.setText("PRO0" + product.getId());
        holder.productPrice.setText("$" + product.getPrice() + ".");
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemProductClick.onItemAddProductToCartClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImg, addToCartButton;
        TextView productName, productID, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImg = (ImageView) itemView.findViewById(R.id.productImg);
            addToCartButton = (ImageView) itemView.findViewById(R.id.addToCartButton);
            productID = (TextView) itemView.findViewById(R.id.productId);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
        }
    }
}
