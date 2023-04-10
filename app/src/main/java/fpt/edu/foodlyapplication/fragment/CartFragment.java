package fpt.edu.foodlyapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.MainActivity;
import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.adapter.CartAdapter;
import fpt.edu.foodlyapplication.interfaces.onItemCartClick;
import fpt.edu.foodlyapplication.model.Cart;
import fpt.edu.foodlyapplication.model.User;
import fpt.edu.foodlyapplication.utils.ServerURLManger;

public class CartFragment extends Fragment {
    public static final String RESPONSE_ERROR = "Error";
    public static final String RESPONSE_LIST_NULL = "List cart null";
    public static final String PARAM_EMAIL = "email";
    public static final String RESPONSE_SUCCESS = "Successful";
    public static final String DELETE_CART_SUCCESS_MESSAGE = "Delete cart successful";
    public static final String DELETE_CART_FAILED_MESSAGE = "Delete cart failed";
    public static final String PARAM_ID = "id";
    public static final String RESPOSE_CART_NULL = "Cart with specified id not found";
    public static final String CART_NULL_MESSAGE = "Cart with specified id not found!";
    public static final String UPDATE_SUCCESS_MESSAGE = "Update quantity cart successfull";
    public static final String PARAM_QUATITY = "quantityNew";
    public static final String RESPONSE_USER_NOT_EXISTS = "User Not Exists";
    public static final String PAY_BILL_SUCCES_MESSAGE = "Pay bill succesfull";
    public static final String PAY_BILL_FAILED_MESSAGE = "Pay bill failed";
    public static final String PARAM_CUSTOMER_NAME = "customerName";
    public static final String PRAM_DATE_BUY = "dateBuy";
    public static final String PRAM_TOTAL = "total";
    private ImageView backButton, loadListButton;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private LinearLayoutManager cartLayoutManager;
    private ArrayList<Cart> listCart;
    private MainActivity mainActivity;
    private TextView cartNullTextView, subTotalTextView, totalTextView;
    private ConstraintLayout payButton;
    private LinearLayout layoutBill;
    private static final String TAG = "CartFragment";
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        mainActivity = (MainActivity) getActivity();
        listCart = new ArrayList<>();
        user = new User();
        initView(view);
        processGetUserByEmailRequest();
        setListeners();
        getListCart();
        return view;
    }

    private void initView(View view) {
        backButton = (ImageView) view.findViewById(R.id.backButton);
        loadListButton = (ImageView) view.findViewById(R.id.loadDataButton);
        cartRecyclerView = (RecyclerView) view.findViewById(R.id.cartRecyclerView);
        cartNullTextView = (TextView) view.findViewById(R.id.cartNullTextView);
        payButton = (ConstraintLayout) view.findViewById(R.id.payButton);
        layoutBill = (LinearLayout) view.findViewById(R.id.layoutPayBill);
        subTotalTextView = (TextView) view.findViewById(R.id.subTotalTextView);
        totalTextView = (TextView) view.findViewById(R.id.totalTextView);
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

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayBillRequest();
            }
        });
    }

    private void processGetUserByEmailRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getUserByEmail = new StringRequest(Request.Method.POST, ServerURLManger.URL_GET_USER_BY_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processGetUserByEmailResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Sever error:  " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add email to request body in server
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, mainActivity.getKeyUser());
                return params;
            }
        };
        requestQueue.add(getUserByEmail);
    }

    private void processGetUserByEmailResponse(String serverResponse) {

        if (serverResponse.equals(RESPONSE_USER_NOT_EXISTS)) {
            Log.i(TAG, RESPONSE_USER_NOT_EXISTS);
            return;
        }

        if (serverResponse.equals(RESPONSE_ERROR)) {
            Toast.makeText(getActivity().getApplicationContext(), ServerURLManger.URL_GET_USER_BY_EMAIL, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(serverResponse);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            user.setEmail(jsonObject.getString("Email"));
            user.setFullname(jsonObject.getString("FullName"));
            user.setPassword(jsonObject.getString("Password"));
            Log.i(TAG, user.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            throw new RuntimeException(e);
        }
    }
    private void getListCart() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getListCartRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_GET_LIST_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processGetListCartResponse(response);
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

    private void processGetListCartResponse(String response) {
        if (response.equals(RESPONSE_ERROR)) {
            Log.e(TAG, "Get list cart error");
            return;
        }

        if (response.equals(RESPONSE_LIST_NULL)) {
            cartNullTextView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            payButton.setVisibility(View.GONE);
            layoutBill.setVisibility(View.GONE);
            return;
        }

        // Parse JSON response to create Cart objects and add to listCart
        try {
            cartNullTextView.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            payButton.setVisibility(View.VISIBLE);
            layoutBill.setVisibility(View.VISIBLE);
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

            setTotalBill();
            setUpCartRecycleView();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTotalBill() {
        int total = 0;
        for (Cart cart : listCart) {
            total += cart.getSumPrice();
        }
        subTotalTextView.setText(String.format("$%d", total));
        totalTextView.setText(String.format("$%d", total));
    }

    private void setUpCartRecycleView() {
        cartLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
        cartRecyclerView.setLayoutManager(cartLayoutManager);
        cartAdapter = new CartAdapter(listCart, new onItemCartClick() {
            @Override
            public void onDeleteCart(Cart cart) {
                showConfirmDeleteCartDialog(cart);
            }

            @Override
            public void onChangeQuantity(View view, Cart cart) {
                if (view.getId() == R.id.addQuantityButton) {
                    processUpadteQuantityCart(cart.getQuantity() + 1, cart);
                } else {
                    processUpadteQuantityCart((cart.getQuantity() - 1) < 1 ? 1 : cart.getQuantity() - 1, cart);
                }

            }
        });
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void showConfirmDeleteCartDialog(Cart cart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want delete Cart?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                processDeleteCartRequest(cart);
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

    private void processDeleteCartRequest(Cart cart) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest deleteCartRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_DELETE_CART_BY_ID, new Response.Listener<String>() {
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

    private void processUpadteQuantityCart(int numberOfCart, Cart cart1) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest updateQuantityRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_UPDATE_QUANTITY_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals(RESPOSE_CART_NULL) || response.equals(RESPONSE_ERROR)) {
                    Toast.makeText(getActivity().getApplicationContext(), CART_NULL_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, CART_NULL_MESSAGE);
                    return;
                }
                Log.d(TAG, UPDATE_SUCCESS_MESSAGE);
                getListCart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put(PARAM_ID, String.valueOf(cart1.getId()));
                param.put(PARAM_QUATITY, String.valueOf(numberOfCart));
                return param;
            }
        };
        requestQueue.add(updateQuantityRequest);
    }

    private void processPayBillRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest addBillRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_ADD_BILL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals(RESPONSE_SUCCESS)) {
                    Toast.makeText(getActivity().getApplicationContext(), PAY_BILL_SUCCES_MESSAGE, Toast.LENGTH_SHORT).show();
                    processDeleteCartByEmailRequest();
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), PAY_BILL_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server erro: " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add email to request body in server
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_CUSTOMER_NAME, user.getFullname());
                Date currentTime = Calendar.getInstance().getTime();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put(PRAM_DATE_BUY, simpleDateFormat.format(currentTime).toString());
                params.put(PRAM_TOTAL, subTotalTextView.getText().toString().substring(1));
                return params;
            }
        };
        requestQueue.add(addBillRequest);
    }

    private void processDeleteCartByEmailRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest deleteCartRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_DELETE_CART_BY_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals(RESPONSE_SUCCESS)) {
                    getListCart();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), DELETE_CART_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
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
                Log.i(TAG, "getParams: " + user.toString());
                params.put(PARAM_EMAIL, user.getEmail());
                return params;
            }
        };
        requestQueue.add(deleteCartRequest);
    }





    @Override
    public void onResume() {
        super.onResume();
        getListCart();
        processGetUserByEmailRequest();
    }
}