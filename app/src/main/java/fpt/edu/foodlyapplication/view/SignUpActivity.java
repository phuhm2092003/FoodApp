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
import fpt.edu.foodlyapplication.utils.Sever;

public class SignUpActivity extends AppCompatActivity {
    private ImageView backBtn, passwordToggle;
    private EditText fullnameEdt, emailEdt, passwordEdt;
    private ConstraintLayout registerBtn;
    private TextView signInText;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });

        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordEdt.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.ic_password_hide);
                } else {
                    passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.ic_password_show);
                }
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAccounts();
            }
        });
    }

    private void initView() {
        backBtn = (ImageView) findViewById(R.id.backBtn);
        passwordToggle = (ImageView) findViewById(R.id.passwordToggle);
        fullnameEdt = (EditText) findViewById(R.id.fullnameEdt);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        passwordEdt = (EditText) findViewById(R.id.passwordEdt);
        registerBtn = (ConstraintLayout) findViewById(R.id.registerBtn);
        signInText = findViewById(R.id.signInText);
    }

    private void registerAccounts() {
        String fullname = fullnameEdt.getText().toString().trim();
        String email = emailEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();

        if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fullname or email or password is empty!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Data is empty");
        } else if (!email.matches("[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+")) {
            Toast.makeText(getApplicationContext(), "Email wrong format!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Email is wrong!");
        } else {
            // Register accounts
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Sever.url_register_account, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Get string return from sever
                    if (response.equals("User Exists")) {
                        Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "User already exists!");
                    } else if (response.equals("Successful")) {
                        Toast.makeText(getApplicationContext(), "Register new account successfull!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Register new account successfull!");
                        fullnameEdt.setText("");
                        emailEdt.setText("");
                        passwordEdt.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Register new account failed!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Register new account failed!");
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
                    // Push data to body-parser sever
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("fullname", fullname);
                    params.put("password", password);

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

}