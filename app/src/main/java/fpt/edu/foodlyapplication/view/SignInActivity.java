package fpt.edu.foodlyapplication.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.MainActivity;
import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.utils.ServerURLManager;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String EXTRA_USER_EMAIL = "EmailUser";
    public static final String RESPONSE_SUCCESS = "Success";
    public static final String LOGIN_FAILED_MESSAGE = "Login failed";
    public static final String LOGIN_SUCCESS_MESSAGE = "Login successfully";
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
        backButton = findViewById(R.id.backButton);
        passwordToggleButton = findViewById(R.id.passwordToggleButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);
    }

    private void setListeners() {
        backButton.setOnClickListener(view -> startSplashScreen());
        passwordToggleButton.setOnClickListener(view -> handlePasswordToggleButtonClicked());
        signUpTextView.setOnClickListener(view -> startSignUpScreen());
        loginButton.setOnClickListener(view -> validateLoginInput());
    }

    private void startSplashScreen() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

    private void handlePasswordToggleButtonClicked() {
        if (passwordEditText.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordToggleButton.setImageResource(R.drawable.ic_password_hide);
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordToggleButton.setImageResource(R.drawable.ic_password_show);
        }
    }

    private void startSignUpScreen() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void validateLoginInput() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isEmptyInput(email, password)) {
            showMessage(EMPTY_INPUT_MESSAGE);
            return;
        }

        handleLoginRequest(email, password);
    }

    private boolean isEmptyInput(String email, String password) {
        return TextUtils.isEmpty(email) || TextUtils.isEmpty(password);
    }

    private void handleLoginRequest(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, ServerURLManager.URL_LOGIN,
                response -> handleLoginResponse(response, email),
                error -> Log.e(TAG, error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add email and password in the request body to server
                Map<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, email);
                params.put(PARAM_PASSWORD, password);
                return params;
            }
        };
        requestQueue.add(loginRequest);
    }

    private void handleLoginResponse(String serverResponse, String email) {
        if (serverResponse.equals(RESPONSE_SUCCESS)) {
            showMessage(LOGIN_SUCCESS_MESSAGE);
            startMainScreen(email);
        } else {
            showMessage(LOGIN_FAILED_MESSAGE);
        }
    }

    private void startMainScreen(String email) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_USER_EMAIL, email);
        startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        startSplashScreen();
    }
}