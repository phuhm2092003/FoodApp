package fpt.edu.foodlyapplication.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.MainActivity;
import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.adapter.CartAdapter;
import fpt.edu.foodlyapplication.interfaces.IItemCartDeleteCallBack;
import fpt.edu.foodlyapplication.model.Cart;
import fpt.edu.foodlyapplication.utils.Server;

public class CartFragment extends Fragment {
    public static final String RESPONSE_ERROR = "Error";
    public static final String RESPONSE_LIST_NULL = "List cart null";
    public static final String PARAM_EMAIL = "email";
    public static final String SERVER_URL_GET_LIST_CART = Server.url_get_list_cart;
    public static final String RESPONSE_SUCCESS = "Successful";
    public static final String DELETE_CART_SUCCESS_MESSAGE = "Delete cart successful";
    public static final String DELETE_CART_FAILED_MESSAGE = "Delete cart failed";
    public static final String SERVER_URL_DELETE_CART = Server.url_delete_cart;
    public static final String PARAM_ID = "id";
    private ImageView backButton, loadListButton;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private LinearLayoutManager cartLayoutManager;
    private ArrayList<Cart> listCart;
    private MainActivity mainActivity;

    private static final String TAG = "CartFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        mainActivity = (MainActivity) getActivity();
        listCart = new ArrayList<>();
        setListeners();
        getListCart();
        return view;
    }

    private void initView(View view) {
        backButton = (ImageView) view.findViewById(R.id.backButton);
        loadListButton = (ImageView) view.findViewById(R.id.loadDataButton);
        cartRecyclerView = (RecyclerView) view.findViewById(R.id.cartRecyclerView);
    }

    private void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        loadListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListCart();
            }
        });
    }

    private void getListCart() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getListCartRequest = new StringRequest(Request.Method.POST, SERVER_URL_GET_LIST_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals(RESPONSE_ERROR)) {
                    Log.e(TAG, "Get list cart error");
                    return;
                }

                if (response.equals(RESPONSE_LIST_NULL)) {
                    listCart.clear();
                    cartLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
                    cartRecyclerView.setLayoutManager(cartLayoutManager);
                    cartAdapter = new CartAdapter(listCart, new IItemCartDeleteCallBack() {
                        @Override
                        public void onCallBack(Cart cart) {

                        }
                    });
                    cartRecyclerView.setAdapter(cartAdapter);
                    Toast.makeText(getActivity().getApplicationContext(), RESPONSE_LIST_NULL, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    // Get list cart fill recycleview
                    listCart.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Cart cart = new Cart();
                        cart.setId(jsonObject.getInt("Id"));
                        cart.setName(jsonObject.getString("Name"));
                        cart.setImage(jsonObject.getString("Image"));
                        cart.setQuantity(jsonObject.getInt("Quantity"));
                        cart.setPrice(jsonObject.getInt("Price"));
                        cart.setSumPrice(jsonObject.getInt("SumPrice"));
                        Log.i(TAG, cart.toString());
                        listCart.add(cart);
                    }
                    cartLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
                    cartRecyclerView.setLayoutManager(cartLayoutManager);
                    cartAdapter = new CartAdapter(listCart, new IItemCartDeleteCallBack() {
                        @Override
                        public void onCallBack(Cart cart) {
                            showConfirmDeleteCartDialog(cart);
                        }
                    });
                    cartRecyclerView.setAdapter(cartAdapter);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server error: " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, mainActivity.getKeyUser());
                return params;
            }
        };
        requestQueue.add(getListCartRequest);
    }

    private void showConfirmDeleteCartDialog(Cart cart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want delete Cart?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteCart(cart);
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCart(Cart cart) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest deleteCartRequest = new StringRequest(Request.Method.POST, SERVER_URL_DELETE_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals(RESPONSE_SUCCESS)) {
                    Toast.makeText(getActivity().getApplicationContext(), DELETE_CART_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, DELETE_CART_SUCCESS_MESSAGE);
                    getListCart();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), DELETE_CART_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, DELETE_CART_SUCCESS_MESSAGE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server error: " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_ID, String.valueOf(cart.getId()));
                return params;
            }
        };
        requestQueue.add(deleteCartRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListCart();
    }
}