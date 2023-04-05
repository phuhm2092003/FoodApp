package fpt.edu.foodlyapplication.interfaces;

import android.view.View;

import fpt.edu.foodlyapplication.model.Cart;

public interface onItemCartClick {
    void onDeleteCart(Cart cart);
    void onChangeQuantity(View view, Cart cart);
}
