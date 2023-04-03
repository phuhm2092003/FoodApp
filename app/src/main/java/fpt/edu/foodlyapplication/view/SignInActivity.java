package fpt.edu.foodlyapplication.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.MainActivity;
import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.utils.ServerURLManger;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String EXTRA_USER_EMAIL = "EmailUser";
    public static final String RESPONSE_SUCCESS = "Successful";
    public static final String LOGIN_FAILED_MESSAGE = "Login failed!";
    public static final String LOGIN_SUCCESS_MESSAGE = "Login successful!";
    private static final String EMPTY_INPUT_MESSAGE = "Email or password is empty!";
    public static final String SERVER_URL_LOGIN = ServerURLManger.url_login;
    private ImageView backButton, passwordToggleButton;
    private EditText emailEditText, passwordEditText;
    private ConstraintLayout loginButton;
    private TextView signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        setListeners();
    }


    private void initView() {
        backButton = (ImageView) findViewById(R.id.backButton);
        passwordToggleButton = (ImageView) findViewById(R.id.passwordToggleButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (ConstraintLayout) findViewById(R.id.loginButton);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
    }

    private void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SplashActivity.class));
            }
        });

        passwordToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordEditText.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordToggleButton.setImageResource(R.drawable.ic_password_hide);
                } else {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordToggleButton.setImageResource(R.drawable.ic_password_show);
                }
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLoginForm();
            }
        });
    }


    private void validateLoginForm() {
        String emailInput = emailEditText.getText().toString().trim();
        String passwordInput = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput)) {
            Toast.makeText(this, EMPTY_INPUT_MESSAGE, Toast.LENGTH_SHORT).show();
            Log.d(TAG, EMPTY_INPUT_MESSAGE);
            return;
        }
        processLoginRequest(emailInput, passwordInput);
    }

    private void processLoginRequest(String userEmail, String userPassword) {
        RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, SERVER_URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processLoginResponse(response, userEmail);
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
                // Add email and password in the request body to sever
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, userEmail);
                params.put(PARAM_PASSWORD, userPassword);
                return params;
            }
        };
        requestQueue.add(loginRequest);
    }

    private void processLoginResponse(String response, String userEmail) {
        // Get response from sever
        if (response.equals(RESPONSE_SUCCESS)) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            intent.putExtra(EXTRA_USER_EMAIL, userEmail);
            startActivity(intent);
            Toast.makeText(SignInActivity.this, LOGIN_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
            Log.d(TAG, LOGIN_SUCCESS_MESSAGE);
        } else {
            Toast.makeText(SignInActivity.this, LOGIN_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
            Log.d(TAG, LOGIN_FAILED_MESSAGE);
        }
    }
}