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

import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.fragment.ProfileFragment;
import fpt.edu.foodlyapplication.model.User;
import fpt.edu.foodlyapplication.utils.Server;

public class UpdateInfoAccountActivity extends AppCompatActivity {
    private static final String TAG = "UpdateInfoAccountActivity";
    public static final String SEVER_URL_GET_USER = Server.url_get_user_by_email;
    public static final String SERVER_URL_UPDATE_FULLNAME = Server.url_update_fullname_user;
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_FULLNAME = "fullname";
    public static final String RESPONSE_USER_NOT_EXISTS = "User Not Exists";
    public static final String RESPONSE_SUCCESS = "Successful";
    public static final String EMPTY_INPUT_MESSAGE = "Fullname is empty!";
    public static final String USER_NOT_EXISTS_MESSAGE = "User Not Exists!";
    public static final String UPDATE_SUCCESS_MESSAGE = "Update fullname successful!";
    public static final String UPDATE_FAILED_MESSAGE = "Erorr";
    private ImageView backButton;
    private EditText fullnameEditText;
    private ConstraintLayout updateButton, updateLaterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_account);
        initView();
        setUserDetails();
        setListeners();
    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.backButton);
        fullnameEditText = (EditText) findViewById(R.id.fullnameEditText);
        updateButton = (ConstraintLayout) findViewById(R.id.updateButton);
        updateLaterButton = (ConstraintLayout) findViewById(R.id.updateLaterButton);
    }

    private void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDataInput();
            }
        });
    }

    private void validateDataInput() {
        String emailUser = getIntent().getStringExtra(ProfileFragment.EXTRA_USER_EMAIL);
        String fullname = fullnameEditText.getText().toString().trim();
        if (fullname.isEmpty()) {
            Toast.makeText(getApplicationContext(), EMPTY_INPUT_MESSAGE, Toast.LENGTH_SHORT).show();
            Log.i(TAG, EMPTY_INPUT_MESSAGE);
            return;
        }
        updateFullName(emailUser, fullname);
    }

    private void updateFullName(String emailUser, String fullname) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest updateRequest = new StringRequest(Request.Method.POST, SERVER_URL_UPDATE_FULLNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals(RESPONSE_USER_NOT_EXISTS)) {
                    Toast.makeText(getApplicationContext(), USER_NOT_EXISTS_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, USER_NOT_EXISTS_MESSAGE);
                } else if (response.equals(RESPONSE_SUCCESS)) {
                    Toast.makeText(getApplicationContext(), UPDATE_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, UPDATE_SUCCESS_MESSAGE);
                } else {
                    Log.i(TAG, UPDATE_FAILED_MESSAGE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Sever error
                Log.e(TAG, "Sever error: " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, emailUser);
                params.put(PARAM_FULLNAME, fullname);
                return params;
            }
        };

        requestQueue.add(updateRequest);
    }

    private void setUserDetails() {
        String emailUser = getIntent().getStringExtra(ProfileFragment.EXTRA_USER_EMAIL);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest getUserRequest = new StringRequest(Request.Method.POST, SEVER_URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get response from sever
                if (response.equals(RESPONSE_USER_NOT_EXISTS)) {
                    Log.i(TAG, RESPONSE_USER_NOT_EXISTS);
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        User user = new User();
                        user.setEmail(jsonObject.getString("Email"));
                        user.setFullname(jsonObject.getString("FullName"));
                        user.setPassword(jsonObject.getString("Password"));
                        Log.i(TAG, user.toString());
                        fullnameEditText.setText(user.getFullname());

                    } catch (JSONException e) {
                        Log.e(TAG, "Sever error: " + e.toString());
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Sever error: " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, emailUser);
                return params;
            }
        };
        requestQueue.add(getUserRequest);
    }
}