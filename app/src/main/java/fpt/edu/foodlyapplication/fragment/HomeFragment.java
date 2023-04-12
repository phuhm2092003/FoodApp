package fpt.edu.foodlyapplication.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import fpt.edu.foodlyapplication.adapter.ProductAdapter;
import fpt.edu.foodlyapplication.interfaces.onItemProductClick;
import fpt.edu.foodlyapplication.model.Product;
import fpt.edu.foodlyapplication.utils.ServerURLManger;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final String RESPONSE_NULL_DATA = "List product null";
    private static final String RESPONSE_ERROR = "Error";
    public static final String RESPONSE_SUCCESS = "Successful";
    public static final String ADD_SUCCESS_MESSAGE = "Add Product to cart successful";
    public static final String GET_LIST_PRODUCT_ERROR_MESSAGE = "Get list product error";
    public static final String ADD_FAILED_MESSAGE = "Add Product to cart failed";
    public static final String PARAM_ID_USER = "idUser";
    public static final String PARAM_ID_PRODUCT = "idProduct";
    public static final String PARAM_QUATITY = "quantity";
    public static final int QUANTITY_DEFAULT = 1;
    private RecyclerView productReycleView;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();

        initView(rootView);
        processGetListProductRequest();

        return rootView;
    }

    private void initView(View view) {
        productReycleView = (RecyclerView) view.findViewById(R.id.productReycleView);
    }

    private void processGetListProductRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest getListProductRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_GET_LIST_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processGetListProductResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server error: " + error.toString());
            }
        });
        requestQueue.add(getListProductRequest);
    }

    private void processGetListProductResponse(String serverResponse) {
        if (serverResponse.equals(RESPONSE_ERROR)) {
            showMessage(GET_LIST_PRODUCT_ERROR_MESSAGE);
            return;
        }

        ArrayList<Product> productList = new ArrayList<>();
        if (serverResponse.equals(RESPONSE_NULL_DATA)) {
            setUpProductRecyclerView(productList);
            return;
        }

        // Parser JSON response form server to product object after add productList
        try {
            JSONArray jsonArray = new JSONArray(serverResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Product product = new Product();
                product.setId(jsonObject.getInt("Id"));
                product.setImage(jsonObject.getString("Image"));
                product.setName(jsonObject.getString("Name"));
                product.setPrice(jsonObject.getInt("Price"));
                Log.d(TAG, "Object Product: " + product);

                productList.add(product);
            }

            setUpProductRecyclerView(productList);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpProductRecyclerView(ArrayList<Product> productList) {
        LinearLayoutManager layoutManagerProduct = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        productReycleView.setLayoutManager(layoutManagerProduct);

        ProductAdapter productAdapter = new ProductAdapter(productList, new onItemProductClick() {
            @Override
            public void onItemAddProductToCartClick(Product product) {
                processAddProductToCartRequest(product);
            }
        });
        productReycleView.setAdapter(productAdapter);
    }

    private void processAddProductToCartRequest(Product product) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest addProductTocartRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_ADD_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processAddProductToCartResponse(response);
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
                // Add idUser(email), idProduct, quantity in the request body to server
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_ID_USER, mainActivity.getKeyUser());
                params.put(PARAM_ID_PRODUCT, String.valueOf(product.getId()));
                params.put(PARAM_QUATITY, String.valueOf(QUANTITY_DEFAULT));
                return params;
            }
        };
        requestQueue.add(addProductTocartRequest);
    }

    private void processAddProductToCartResponse(String serverResponse) {
        String message = serverResponse.equals(RESPONSE_SUCCESS) ? ADD_SUCCESS_MESSAGE : ADD_FAILED_MESSAGE;
        showMessage(message);
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        processGetListProductRequest();
    }

}