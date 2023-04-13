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
import fpt.edu.foodlyapplication.utils.ServerURLManager;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    public static final String RESPONSE_USER_NOT_EXISTS = "User Not Exists";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String EMPTY_INPUT_MESSAGE = "Please enter full information!";
    public static final String OLD_PASSWORD_WRONG_MESSAGE = "Old password is wrong!";
    public static final String REPLACE_PASSWORD_WRONG_MESSAGE = "Replace new password is wrong!";
    public static final String RESPONSE_SUCCESS = "Successful";
    public static final String UPDATE_SUCCESS_MESSAGE = "Change password successful";
    private ImageView backBtn, passwordOldToggle, passwordNewToggle, passwordNewReplaceToggle;
    private EditText passwordOldEdt, passwordNewEdt, passwordNewReplaceEdt;
    private ConstraintLayout updateBtn, updateLaterBtn;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        getInfoUser();
        setListeners();
    }

    private void setListeners() {
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
                Log.i(TAG, "Info user update passowd: " + user.toString());
                String passwordOld = passwordOldEdt.getText().toString().trim();
                String passwordNew = passwordNewEdt.getText().toString().trim();
                String passwordNewReplace = passwordNewReplaceEdt.getText().toString().trim();
                if (passwordOld.isEmpty() || passwordNew.isEmpty() || passwordNewReplace.isEmpty()) {
                    Toast.makeText(getApplicationContext(), EMPTY_INPUT_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, EMPTY_INPUT_MESSAGE);
                } else if (!passwordOld.equals(user.getPassword())) {
                    Toast.makeText(getApplicationContext(), OLD_PASSWORD_WRONG_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, OLD_PASSWORD_WRONG_MESSAGE);
                } else if (!passwordNew.equals(passwordNewReplace)) {
                    Toast.makeText(getApplicationContext(), REPLACE_PASSWORD_WRONG_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, REPLACE_PASSWORD_WRONG_MESSAGE);
                } else {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURLManager.URL_UPDATE_PASSWORD, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals(RESPONSE_USER_NOT_EXISTS)) {
                                Log.i(TAG, RESPONSE_USER_NOT_EXISTS);
                            } else if (response.equals(RESPONSE_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), UPDATE_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                                Log.i(TAG, UPDATE_SUCCESS_MESSAGE);
                                getInfoUser();
                                // Clearn text
                                passwordOldEdt.setText("");
                                passwordNewEdt.setText("");
                                passwordNewReplaceEdt.setText("");
                            } else {
                                Log.i(TAG, "Error");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, error.toString());
                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put(PARAM_EMAIL, user.getEmail());
                            params.put(PARAM_PASSWORD, passwordNew);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });
    }

    private void getInfoUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest getUserRequest = new StringRequest(Request.Method.POST, ServerURLManager.URL_GET_USER_BY_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get string result from sever
                if (response.equals(RESPONSE_USER_NOT_EXISTS)) {
                    Log.i(TAG, RESPONSE_USER_NOT_EXISTS);
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, getIntent().getStringExtra(ProfileFragment.EXTRA_USER_EMAIL));
                return params;
            }
        };
        requestQueue.add(getUserRequest);
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