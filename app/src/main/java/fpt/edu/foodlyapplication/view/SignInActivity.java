package fpt.edu.foodlyapplication.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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

import fpt.edu.foodlyapplication.MainActivity;
import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.utils.Sever;

public class SignInActivity extends AppCompatActivity {
    private ImageView backBtn, passowrdToggle;
    private EditText emailEdt, passwordEdt;
    private ConstraintLayout loginBtn;
    private TextView signUpText;
    private static final String TAG = "SignInActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            }
        });

        passowrdToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordEdt.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passowrdToggle.setImageResource(R.drawable.ic_password_hide);
                } else {
                    passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passowrdToggle.setImageResource(R.drawable.ic_password_show);
                }
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void initView() {
        backBtn = (ImageView) findViewById(R.id.backBtn);
        passowrdToggle = (ImageView) findViewById(R.id.passwordToggle);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        passwordEdt = (EditText) findViewById(R.id.passwordEdt);
        loginBtn = (ConstraintLayout) findViewById(R.id.loginBtn);
        signUpText = (TextView) findViewById(R.id.signUpText);
    }

    private void login() {
        String email = emailEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email or password is empty!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Email or password is empty");
        } else {
            // Login system
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Sever.url_login, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Get string return from sever
                    if (response.equals("Successful")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Login successful");
                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Login failed");
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
                    params.put("password", password);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}