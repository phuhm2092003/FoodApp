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
import fpt.edu.foodlyapplication.utils.ServerURLManager;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    public static final String EMAIL_REGEX_PATTERN = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
    public static final String EMPTY_INPUT_MESSAGE = "Please enter both fullname, email and password";
    public static final String INVALID_EMAIL_MESSAGE = "Please enter a valid email address";
    public static final String USER_EXISTS_MESSAGE = "User already exists";
    public static final String REGISTER_SUCCESS_MESSAGE = "Register new account successfully";
    public static final String REGISTER_FAILED_MESSAGE = "Register new account failed";
    public static final String RESPONSE_USER_EXISTS = "User exists";
    public static final String RESPONSE_SUCCESS = "Success";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_FULLNAME = "fullname";
    public static final String PARAM_PASSWORD = "password";
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
        backButton = findViewById(R.id.backButton);
        passwordToggleButton = findViewById(R.id.passwordToggleButton);
        fullnameEditText = findViewById(R.id.fullnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        signInTextView = findViewById(R.id.signInTextView);
    }

    private void setListeners() {
        backButton.setOnClickListener(view -> startSignInScreen());
        passwordToggleButton.setOnClickListener(view -> handlePasswordToggleButtonClicked());
        signInTextView.setOnClickListener(view -> startSignInScreen());
        registerButton.setOnClickListener(view -> validateRegisterAccountInput());
    }

    private void startSignInScreen() {
        Intent intent = new Intent(this, SignInActivity.class);
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

    private void validateRegisterAccountInput() {
        String email = emailEditText.getText().toString().trim();
        String fullname = fullnameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isEmptyInput(fullname, email, password)) {
            showMessage(EMPTY_INPUT_MESSAGE);
            return;
        }

        if (isInvalidEmail(email)) {
            showMessage(INVALID_EMAIL_MESSAGE);
            return;
        }

        handleRegisterAccountRequest(fullname, email, password);
    }

    private boolean isEmptyInput(String fullname, String email, String password) {
        return TextUtils.isEmpty(fullname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password);
    }

    private boolean isInvalidEmail(String email) {
        return !email.matches(EMAIL_REGEX_PATTERN);
    }

    private void handleRegisterAccountRequest(String fullname, String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest registerAccountRequest = new StringRequest(Request.Method.POST, ServerURLManager.URL_REGISTER_ACCOUNT,
                response -> handleRegisterAccountResponse(response),
                error -> Log.e(TAG, error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add email, password, name in the request body to server
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, email);
                params.put(PARAM_FULLNAME, fullname);
                params.put(PARAM_PASSWORD, password);
                return params;
            }
        };
        requestQueue.add(registerAccountRequest);
    }

    private void handleRegisterAccountResponse(String serverResponse) {
        switch (serverResponse) {
            case RESPONSE_USER_EXISTS:
                showMessage(USER_EXISTS_MESSAGE);
                break;
            case RESPONSE_SUCCESS:
                showMessage(REGISTER_SUCCESS_MESSAGE);
                clearTextFields();
                break;
            default:
                showMessage(REGISTER_FAILED_MESSAGE);
                break;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearTextFields() {
        fullnameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
    }
}