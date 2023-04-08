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
import fpt.edu.foodlyapplication.utils.ServerURLManger;

public class UpdateInfoAccountActivity extends AppCompatActivity {
    private static final String TAG = "UpdateInfoAccountActivity";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_FULLNAME = "fullname";
    public static final String RESPONSE_USER_NOT_EXISTS = "User Not Exists";
    public static final String RESPONSE_SUCCESS = "Successful";
    public static final String EMPTY_INPUT_MESSAGE = "Fullname is empty!";
    public static final String USER_NOT_EXISTS_MESSAGE = "User Not Exists!";
    public static final String UPDATE_SUCCESS_MESSAGE = "Update fullname successful!";
    public static final String UPDATE_FAILED_MESSAGE = "Erorr";
    public static final String GET_USER_BY_EMAIL_ERROR_MESSAGE = "Get user by email error";
    public static final String RESPONSE_ERROR = "Error";
    private ImageView backButton;
    private EditText fullnameEditText;
    private ConstraintLayout updateButton, updateLaterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_account);
        initView();
        processGetUserByEmailRequest();
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
            return;
        }
        processUpdateFullNameRequest(emailUser, fullname);
    }

    private void processUpdateFullNameRequest(String emailUser, String fullname) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest updateRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_UPDATE_FULLNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processUpdateFullNameResponse(response);
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

    private void processUpdateFullNameResponse(String serverResponse) {
        switch (serverResponse) {
            case RESPONSE_SUCCESS:
                Toast.makeText(this, UPDATE_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                break;
            case RESPONSE_USER_NOT_EXISTS:
                Toast.makeText(this, USER_NOT_EXISTS_MESSAGE, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, UPDATE_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void processGetUserByEmailRequest() {
        String emailUser = getIntent().getStringExtra(ProfileFragment.EXTRA_USER_EMAIL);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest getUserRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_GET_USER_BY_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processGetUserByEmailResponse(response);
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

    private void processGetUserByEmailResponse(String serverResponse) {
        if (serverResponse.equals(RESPONSE_USER_NOT_EXISTS)) {
            Log.i(TAG, RESPONSE_USER_NOT_EXISTS);
            return;
        }

        if (serverResponse.equals(RESPONSE_ERROR)) {
            Toast.makeText(this, GET_USER_BY_EMAIL_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(serverResponse);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            User user = new User();
            user.setEmail(jsonObject.getString("Email"));
            user.setFullname(jsonObject.getString("FullName"));
            user.setPassword(jsonObject.getString("Password"));
            Log.i(TAG, user.toString());
            // Set data
            fullnameEditText.setText(user.getFullname());
        } catch (JSONException e) {
            Log.e(TAG, "Sever error: " + e.toString());
            throw new RuntimeException(e);
        }
    }
}