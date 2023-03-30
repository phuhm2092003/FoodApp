package fpt.edu.foodlyapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.adapter.ProductAdapter;
import fpt.edu.foodlyapplication.model.Product;
import fpt.edu.foodlyapplication.utils.Server;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final String RESPONSE_NO_DATA = "No data";
    private static final String RESPONSE_ERROR = "ERROR";
    private static final String SERVER_URL_GET_LIST_PRODUCT = Server.url_get_list_product;
    private RecyclerView productReycleView;
    private ProductAdapter productAdapter;
    private LinearLayoutManager layoutManagerProduct;
    private ArrayList<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        productList = new ArrayList<>();
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
                        productAdapter = new ProductAdapter(productList);
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

    @Override
    public void onResume() {
        super.onResume();
        getListProduct();
    }

}