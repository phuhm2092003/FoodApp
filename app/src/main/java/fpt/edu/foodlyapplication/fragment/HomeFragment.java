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
import fpt.edu.foodlyapplication.interfaces.IItemAddCartCallBack;
import fpt.edu.foodlyapplication.model.Product;
import fpt.edu.foodlyapplication.utils.ServerURLManger;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final String RESPONSE_NO_DATA = "List product null";
    private static final String RESPONSE_ERROR = "Error";
    private static final String SERVER_URL_GET_LIST_PRODUCT = ServerURLManger.url_get_list_product;
    public static final String RESPONSE_SUCCESS = "Successful";
    public static final String ADD_SUCCESS_MESSAGE = "Add Product successful";
    public static final String ADD_FAILED_MESSAGE = "Add Product failed";
    public static final String PARAM_ID_USER = "idUser";
    public static final String PARAM_ID_PRODUCT = "idProduct";
    public static final String PARAM_QUATITY = "quantity";
    public static final String QUANTITY_DEFAULT = "1";
    public static final String SERVER_URL_ADD_CART = ServerURLManger.url_add_cart;
    private RecyclerView productReycleView;
    private ProductAdapter productAdapter;
    private LinearLayoutManager layoutManagerProduct;
    private ArrayList<Product> productList;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        productList = new ArrayList<>();
        mainActivity = (MainActivity) getActivity();
        getListProduct();

        return view;
    }

    private void initView(View view) {
        productReycleView = (RecyclerView) view.findViewById(R.id.productReycleView);
    }
    private void getListProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getListProductRequest = new StringRequest(Request.Method.POST, SERVER_URL_GET_LIST_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals(RESPONSE_NO_DATA) && !response.equals(RESPONSE_ERROR)){
                    try {
                        productList.clear();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product product = new Product();
                            product.setId(jsonObject.getInt("Id"));
                            product.setImage(jsonObject.getString("Image"));
                            product.setName(jsonObject.getString("Name"));
                            product.setPrice(jsonObject.getInt("Price"));
                            Log.i(TAG, "Object Product: "+product.toString());
                            productList.add(product);
                        }

                        layoutManagerProduct = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
                        productReycleView.setLayoutManager(layoutManagerProduct);
                        productAdapter = new ProductAdapter(productList, new IItemAddCartCallBack() {
                            @Override
                            public void onCallBack(Product product) {
                                addProductToCart(product);
                            }
                        });
                        productReycleView.setAdapter(productAdapter);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server error: " + error.toString());
            }
        });
        requestQueue.add(getListProductRequest);
    }

    private void addProductToCart(Product product) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest addCartRequest = new StringRequest(Request.Method.POST, SERVER_URL_ADD_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get response form server
                if(response.equals(RESPONSE_SUCCESS)){
                    Toast.makeText(mainActivity, ADD_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, ADD_SUCCESS_MESSAGE);
                }else {
                    Toast.makeText(mainActivity, ADD_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, ADD_FAILED_MESSAGE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server error: " + error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add idUser(email), idProduct, quantity
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_ID_USER, mainActivity.getKeyUser());
                params.put(PARAM_ID_PRODUCT, String.valueOf(product.getId()));
                params.put(PARAM_QUATITY, QUANTITY_DEFAULT);
                return params;
            }
        };
        requestQueue.add(addCartRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListProduct();
    }

}