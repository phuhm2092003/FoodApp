package fpt.edu.foodlyapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fpt.edu.foodlyapplication.adapter.PageMainAdapter;

public class MainActivity extends AppCompatActivity {
    LinearLayout homeItem, cartItem, profileItem;
    ImageView homeImg, cartImg, profileImg;
    TextView homeText, cartText, profileText;

    ViewPager2 pageMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPageMain();

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
}