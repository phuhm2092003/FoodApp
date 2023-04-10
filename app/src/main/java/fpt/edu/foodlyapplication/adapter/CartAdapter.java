package fpt.edu.foodlyapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.interfaces.onItemCartClick;
import fpt.edu.foodlyapplication.model.Cart;
import fpt.edu.foodlyapplication.utils.ServerURLManger;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private ArrayList<Cart> listCart;
    private onItemCartClick onItemCartClick;

    public CartAdapter(ArrayList<Cart> listCart, fpt.edu.foodlyapplication.interfaces.onItemCartClick onItemCartClick) {
        this.listCart = listCart;
        this.onItemCartClick = onItemCartClick;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        if (cart == null) {
            return;
        }
        if (cart.getImage().substring(0, 4).equals("http")) {
            Glide.with(holder.itemView.getContext())
                    .load(cart.getImage())
                    .placeholder(R.drawable.load_image)
                    .into(holder.productImg);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(ServerURLManger.URL_BASE + "/" + cart.getImage())
                    .placeholder(R.drawable.load_image)
                    .into(holder.productImg);
        }

        holder.productName.setText(cart.getName());
        holder.productPrice.setText("$" + cart.getPrice() + ".00");
        holder.productSumPrice.setText("$" + cart.getSumPrice() + ".00");
        holder.productQuantity.setText(String.format("%02d", cart.getQuantity()));
        holder.itemCart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemCartClick.onDeleteCart(cart);
                return true;
            }
        });

        holder.addQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCartClick.onChangeQuantity(view, cart);
            }
        });
        holder.reduceQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCartClick.onChangeQuantity(view, cart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (listCart == null) ? 0 : listCart.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CircleImageView productImg;
        TextView productName, productPrice, productSumPrice, productQuantity;
        ImageView reduceQuantityButton, addQuantityButton;
        ConstraintLayout itemCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCart = (ConstraintLayout) itemView.findViewById(R.id.itemCart);
            productImg = (CircleImageView) itemView.findViewById(R.id.productImg);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            productSumPrice = (TextView) itemView.findViewById(R.id.productSumPrice);
            productQuantity = (TextView) itemView.findViewById(R.id.productQuantity);
            reduceQuantityButton = (ImageView) itemView.findViewById(R.id.reduceQuantityButton);
            addQuantityButton = (ImageView) itemView.findViewById(R.id.addQuantityButton);
        }
    }
}
