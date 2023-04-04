package fpt.edu.foodlyapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fpt.edu.foodlyapplication.adapter.PageMainAdapter;
import fpt.edu.foodlyapplication.view.SignInActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private LinearLayout homeItem, cartItem, profileItem;
    private ImageView homeImg, cartImg, profileImg;
    private TextView homeText, cartText, profileText;
    private ViewPager2 pageMain;
    private String userEmailKey = "";

    public String getKeyUser() {
        return userEmailKey;
    }

    public void setKeyUser(String userEmailKey) {
        this.userEmailKey = userEmailKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPageMain();
        // Get email user from SignInActivity set keyUser
        setKeyUser(getIntent().getStringExtra(SignInActivity.EXTRA_USER_EMAIL));
        Log.i(TAG, "KEY_USER: " + getKeyUser());
        // Bottom menu item selected
        homeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeItem.setBackgroundResource(R.drawable.bgr_item_bottom_menu);
                cartItem.setBackgroundResource(R.color.White);
                profileItem.setBackgroundResource(R.color.White);

                homeImg.setImageResource(R.drawable.ic_home_main_color);
                cartImg.setImageResource(R.drawable.ic_cart_gray);
                profileImg.setImageResource(R.drawable.ic_profile_gray);

                homeText.setVisibility(View.VISIBLE);
                cartText.setVisibility(View.GONE);
                profileText.setVisibility(View.GONE);

                pageMain.setCurrentItem(0, false);
            }
        });

        cartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeItem.setBackgroundResource(R.color.White);
                cartItem.setBackgroundResource(R.drawable.bgr_item_bottom_menu);
                profileItem.setBackgroundResource(R.color.White);

                homeImg.setImageResource(R.drawable.ic_home_gray);
                cartImg.setImageResource(R.drawable.ic_cart_main_color);
                profileImg.setImageResource(R.drawable.ic_profile_gray);

                homeText.setVisibility(View.GONE);
                cartText.setVisibility(View.VISIBLE);
                profileText.setVisibility(View.GONE);

                pageMain.setCurrentItem(1, false);

            }
        });

        profileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeItem.setBackgroundResource(R.color.White);
                cartItem.setBackgroundResource(R.color.White);
                profileItem.setBackgroundResource(R.drawable.bgr_item_bottom_menu);

                homeImg.setImageResource(R.drawable.ic_home_gray);
                cartImg.setImageResource(R.drawable.ic_cart_gray);
                profileImg.setImageResource(R.drawable.ic_profile_main_color);

                homeText.setVisibility(View.GONE);
                cartText.setVisibility(View.GONE);
                profileText.setVisibility(View.VISIBLE);

                pageMain.setCurrentItem(2, false);
            }
        });
    }

    private void initPageMain() {
        PageMainAdapter pageMainAdapter = new PageMainAdapter(MainActivity.this);
        pageMain.setAdapter(pageMainAdapter);
        pageMain.setUserInputEnabled(false);
    }

    private void initView() {
        homeItem = (LinearLayout) findViewById(R.id.homeItem);
        cartItem = (LinearLayout) findViewById(R.id.cartItem);
        profileItem = (LinearLayout) findViewById(R.id.profileItem);
        homeImg = (ImageView) findViewById(R.id.homeImg);
        cartImg = (ImageView) findViewById(R.id.cartImg);
        profileImg = (ImageView) findViewById(R.id.profileImg);
        homeText = (TextView) findViewById(R.id.homeText);
        cartText = (TextView) findViewById(R.id.cartText);
        profileText = (TextView) findViewById(R.id.profileText);
        pageMain = (ViewPager2) findViewById(R.id.pageMain);
    }

    @Override
    public void onBackPressed() {
        homeItem.setBackgroundResource(R.drawable.bgr_item_bottom_menu);
        cartItem.setBackgroundResource(R.color.White);
        profileItem.setBackgroundResource(R.color.White);

        homeImg.setImageResource(R.drawable.ic_home_main_color);
        cartImg.setImageResource(R.drawable.ic_cart_gray);
        profileImg.setImageResource(R.drawable.ic_profile_gray);

        homeText.setVisibility(View.VISIBLE);
        cartText.setVisibility(View.GONE);
        profileText.setVisibility(View.GONE);

        pageMain.setCurrentItem(0, false);
    }
}