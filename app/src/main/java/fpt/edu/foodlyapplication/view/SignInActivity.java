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
    public static final String RESPONSE_SUCCESS = "Successfull";
    public static final String LOGIN_FAILED_MESSAGE = "Login failed";
    public static final String LOGIN_SUCCESS_MESSAGE = "Login successful!";
    public static final String EMPTY_INPUT_MESSAGE = "Please enter both email and password";

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
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isEmptyInput(email, password)) {
            showMessage(EMPTY_INPUT_MESSAGE);
        } else {
            processLoginRequest(email, password);
        }
    }

    private boolean isEmptyInput(String email, String password) {
        return TextUtils.isEmpty(email) || TextUtils.isEmpty(password);
    }

    private void processLoginRequest(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, ServerURLManger.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processLoginResponse(response, email);
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
                Map<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, email);
                params.put(PARAM_PASSWORD, password);
                return params;
            }
        };
        requestQueue.add(loginRequest);
    }

    private void processLoginResponse(String serverResponse, String email) {
        if (serverResponse.equals(RESPONSE_SUCCESS)) {
            showMessage(LOGIN_SUCCESS_MESSAGE);
            launchMainActivity(email);
        } else {
            showMessage(LOGIN_FAILED_MESSAGE);
        }
    }

    private void launchMainActivity(String email) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_USER_EMAIL, email);
        startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}