package fpt.edu.foodlyapplication.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
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

import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.utils.Server;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    public static final String EMAIL_REGEX_PATTERN = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";

    public static final String EMPTY_INPUT_MESSAGE = "Fullname or email or password is empty!";
    public static final String INVALID_EMAIL_MESSAGE = "Please enter a valid email address!";
    public static final String USER_EXISTS_MESSAGE = "User already exists!";
    public static final String REGISTER_SUCCESS_MESSAGE = "Register new account successfull!";
    public static final String REGISTER_FAILED_MESSAGE = "Register new account failed!";
    public static final String RESPONSE_USER_EXISTS = "User Exists";
    public static final String RESPONSE_SUCCESS = "Successful";

    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_FULLNAME = "fullname";
    public static final String PARAM_PASSWORD = "password";
    public static final String SERVER_URL_REGISTER_ACCOUNT = Server.url_register_account;

    private ImageView backButton, passwordToggleButton;
    private EditText fullnameEditText, emailEditText, passwordEditText;
    private ConstraintLayout registerButton;
    private TextView signInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        setListeners();
    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.backButton);
        passwordToggleButton = (ImageView) findViewById(R.id.passwordToggleButton);
        fullnameEditText = (EditText) findViewById(R.id.fullnameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        registerButton = (ConstraintLayout) findViewById(R.id.registerButton);
        signInTextView = (TextView) findViewById(R.id.signInTextView);
    }

    private void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
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

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDataInput();
            }
        });
    }

    private void validateDataInput() {
        String fullname = fullnameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isEmptyFields(fullname, email, password)) {
            Toast.makeText(getApplicationContext(), EMPTY_INPUT_MESSAGE, Toast.LENGTH_SHORT).show();
            Log.e(TAG, EMPTY_INPUT_MESSAGE);
            return;
        }

        if (isInvalidEmail(email)) {
            Toast.makeText(getApplicationContext(), INVALID_EMAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            Log.e(TAG, INVALID_EMAIL_MESSAGE);
            return;
        }
        registerAccounts(fullname, email, password);
    }

    private void registerAccounts(String fullname, String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest registerRequest = new StringRequest(Request.Method.POST, SERVER_URL_REGISTER_ACCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get response from sever
                if (response.equals(RESPONSE_USER_EXISTS)) {
                    Toast.makeText(getApplicationContext(), USER_EXISTS_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, USER_EXISTS_MESSAGE);
                } else if (response.equals(RESPONSE_SUCCESS)) {
                    Toast.makeText(getApplicationContext(), REGISTER_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, REGISTER_SUCCESS_MESSAGE);
                    // Clear text
                    clearTextFields();
                } else {
                    Toast.makeText(getApplicationContext(), REGISTER_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, REGISTER_FAILED_MESSAGE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Sever error
                Log.e(TAG, "Sever error: " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Push data (email, fullname password) to body-parser sever
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, email);
                params.put(PARAM_FULLNAME, fullname);
                params.put(PARAM_PASSWORD, password);
                return params;
            }
        };
        requestQueue.add(registerRequest);
    }

    private void clearTextFields() {
        fullnameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
    }

    private boolean isInvalidEmail(String email) {
        return !email.matches(EMAIL_REGEX_PATTERN);
    }

    private boolean isEmptyFields(String fullname, String email, String password) {
        return fullname.isEmpty() || email.isEmpty() || password.isEmpty();
    }

}