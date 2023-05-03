package fpt.edu.foodlyapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import fpt.edu.foodlyapplication.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    public static final int TIME_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> startSignInScreen(), TIME_DELAY);
    }

    private void startSignInScreen() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}