package fpt.edu.foodlyapplication.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.fragment.ProfileFragment;
import fpt.edu.foodlyapplication.model.User;
import fpt.edu.foodlyapplication.utils.Sever;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    private ImageView backBtn, passwordOldToggle, passwordNewToggle, passwordNewReplaceToggle;
    private EditText passwordOldEdt, passwordNewEdt, passwordNewReplaceEdt;
    private ConstraintLayout updateBtn, updateLaterBtn;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        getUser();
        // Event view
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        passwordOldToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePassword(passwordOldEdt, passwordOldToggle);
            }
        });

        passwordNewToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePassword(passwordNewEdt, passwordNewToggle);
            }
        });

        passwordNewReplaceToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePassword(passwordNewReplaceEdt, passwordNewReplaceToggle);
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
                Log.i(TAG, "Info user update passowd: "+ user.toString());
                String passwordOld = passwordOldEdt.getText().toString().trim();
                String passwordNew = passwordNewEdt.getText().toString().trim();
                String passwordNewReplace = passwordNewReplaceEdt.getText().toString().trim();
                if(passwordOld.isEmpty() || passwordNew.isEmpty() || passwordNewReplace.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter full information!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Data is empty!");
                }else if(!passwordOld.equals(user.getPassword())){
                    Toast.makeText(getApplicationContext(), "Old password is wrong!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Old password is wrong!");
                }else if(!passwordNew.equals(passwordNewReplace)){
                    Toast.makeText(getApplicationContext(), "Replace new password is wrong!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Replace new password is wrong!");
                }else {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Sever.url_update_password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("User Not Exists")){
                                Log.i(TAG, "User Not Exists");
                            }else if(response.equals("Successful")){
                                Toast.makeText(getApplicationContext(), "Change password successful", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Change passowrd successful");
                                getUser();
                                // Clearn text
                                passwordOldEdt.setText("");
                                passwordNewEdt.setText("");
                                passwordNewReplaceEdt.setText("");
                            }else {
                                Log.i(TAG, "Error");
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
                            params.put("email", user.getEmail());
                            params.put("password", passwordNew);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });
    }

    private void getUser() {
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
                        user = new User();
                        user.setEmail(jsonObject.getString("Email"));
                        user.setFullname(jsonObject.getString("FullName"));
                        user.setPassword(jsonObject.getString("Password"));
                        Log.i(TAG, user.toString());

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
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", getIntent().getStringExtra(ProfileFragment.EXTRA_USER_EMAIL));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void togglePassword(EditText edt, ImageView imgToggle) {
        if (edt.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
            edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgToggle.setImageResource(R.drawable.ic_password_hide);
        } else {
            edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgToggle.setImageResource(R.drawable.ic_password_show);
        }
    }

    private void initView() {
        backBtn = (ImageView) findViewById(R.id.backBtn);
        passwordOldToggle = (ImageView) findViewById(R.id.passwordOldToggle);
        passwordNewToggle = (ImageView) findViewById(R.id.passwordNewToggle);
        passwordNewReplaceToggle = (ImageView) findViewById(R.id.passwordNewReplaceToggle);
        passwordOldEdt = (EditText) findViewById(R.id.passwordOldEdt);
        passwordNewEdt = (EditText) findViewById(R.id.passwordNewEdt);
        passwordNewReplaceEdt = (EditText) findViewById(R.id.passwordNewReplaceEdt);
        updateBtn = (ConstraintLayout) findViewById(R.id.updateBtn);
        updateLaterBtn = (ConstraintLayout) findViewById(R.id.updateLaterBtn);
    }
}