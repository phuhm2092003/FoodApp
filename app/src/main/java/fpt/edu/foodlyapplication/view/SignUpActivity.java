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

import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.utils.ServerURLManager;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    public static final String EMAIL_REGEX_PATTERN = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
    public static final String EMPTY_INPUT_MESSAGE = "Please enter both fullname, email and password";
    public static final String INVALID_EMAIL_MESSAGE = "Please enter a valid email address";
    public static final String USER_EXISTS_MESSAGE = "User already exists";
    public static final String REGISTER_SUCCESS_MESSAGE = "Register new account successfull";
    public static final String REGISTER_FAILED_MESSAGE = "Register new account failed";
    public static final String RESPONSE_USER_EXISTS = "User exists";
    public static final String RESPONSE_SUCCESS = "Successfully";
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
                validateRegisterForm();
            }
        });
    }

    private void validateRegisterForm() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest registerRequest = new StringRequest(Request.Method.POST, ServerURLManager.URL_REGISTER_ACCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleRegisterAccountResponse(response);
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
                // Add email, password, name in the request body to server
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, email);
                params.put(PARAM_FULLNAME, fullname);
                params.put(PARAM_PASSWORD, password);
                return params;
            }
        };
        requestQueue.add(registerRequest);
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

    private void showMessage(String userExistsMessage) {
        Toast.makeText(getApplicationContext(), userExistsMessage, Toast.LENGTH_SHORT).show();
    }

    private void clearTextFields() {
        fullnameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
    }
}