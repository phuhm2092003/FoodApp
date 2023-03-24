package fpt.edu.foodlyapplication.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.fragment.ProfileFragment;
import fpt.edu.foodlyapplication.model.User;
import fpt.edu.foodlyapplication.utils.Sever;

public class UpdateInfoAccountActivity extends AppCompatActivity {
    private ImageView backBtn;
    private EditText fullnameEdt;
    private ConstraintLayout updateBtn, updateLaterBtn;
    private static final String TAG = "UpdateInfoAccountActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_account);
        initView();
        setText();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFullname();
            }
        });
    }

    private void updateFullname() {
        String emailUser = getIntent().getStringExtra(ProfileFragment.KEY_USER);
        String fullname = fullnameEdt.getText().toString().trim();
        if(fullname.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fullname is empty!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Fullname is empty");
        }else {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Sever.url_update_fullname_user, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("User Not Exists")){
                        Toast.makeText(getApplicationContext(), "User Not Exists", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "User Not Exists");
                    }else if(response.equals("Successful")){
                        Toast.makeText(getApplicationContext(), "Update fullname successful", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Update full name successful");
                    }else {
                        Log.i(TAG, "Erorr");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, error.toString());
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email",emailUser);
                    params.put("fullname", fullname);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void setText() {
        String emailUser = getIntent().getStringExtra(ProfileFragment.KEY_USER);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Sever.url_get_user_by_email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get string result from sever
                if (response.equals("User Not Exists")) {
                    Log.i(TAG, "User Not Exists");
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        User user = new User();
                        user.setEmail(jsonObject.getString("Email"));
                        user.setFullname(jsonObject.getString("FullName"));
                        user.setPassword(jsonObject.getString("Password"));
                        Log.i(TAG, user.toString());
                        // Set text
                        fullnameEdt.setText(user.getFullname());

                    } catch (JSONException e) {
                        Log.i(TAG, e.toString());
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", emailUser);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void initView() {
        backBtn = (ImageView) findViewById(R.id.backBtn);
        fullnameEdt = (EditText) findViewById(R.id.fullnameEdt);
        updateBtn = (ConstraintLayout) findViewById(R.id.updateBtn);
        updateLaterBtn = (ConstraintLayout) findViewById(R.id.updateLaterBtn);
    }
}