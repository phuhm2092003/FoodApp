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
    LinearLayout menuHome, menuCart, menuProfile;
    ImageView imgHome, imgCart, imgProfile;
    TextView txtHome, txtCart, txtProfile;

    ViewPager2 pageMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPageMain();

        // Bottom menu item selected
        menuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuHome.setBackgroundResource(R.drawable.bgr_item_bottom_menu);
                menuCart.setBackgroundResource(R.color.White);
                menuProfile.setBackgroundResource(R.color.White);

                imgHome.setImageResource(R.drawable.ic_home_main_color);
                imgCart.setImageResource(R.drawable.ic_cart_gray);
                imgProfile.setImageResource(R.drawable.ic_profile_gray);

                txtHome.setVisibility(View.VISIBLE);
                txtCart.setVisibility(View.GONE);
                txtProfile.setVisibility(View.GONE);

                pageMain.setCurrentItem(0, false);
            }
        });

        menuCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuHome.setBackgroundResource(R.color.White);
                menuCart.setBackgroundResource(R.drawable.bgr_item_bottom_menu);
                menuProfile.setBackgroundResource(R.color.White);

                imgHome.setImageResource(R.drawable.ic_home_gray);
                imgCart.setImageResource(R.drawable.ic_cart_main_color);
                imgProfile.setImageResource(R.drawable.ic_profile_gray);

                txtHome.setVisibility(View.GONE);
                txtCart.setVisibility(View.VISIBLE);
                txtProfile.setVisibility(View.GONE);

                pageMain.setCurrentItem(1, false);

            }
        });

        menuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuHome.setBackgroundResource(R.color.White);
                menuCart.setBackgroundResource(R.color.White);
                menuProfile.setBackgroundResource(R.drawable.bgr_item_bottom_menu);

                imgHome.setImageResource(R.drawable.ic_home_gray);
                imgCart.setImageResource(R.drawable.ic_cart_gray);
                imgProfile.setImageResource(R.drawable.ic_profile_main_color);

                txtHome.setVisibility(View.GONE);
                txtCart.setVisibility(View.GONE);
                txtProfile.setVisibility(View.VISIBLE);

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
        menuHome = findViewById(R.id.menuHome);
        menuCart = findViewById(R.id.menuCart);
        menuProfile = findViewById(R.id.menuProfile);
        imgHome = findViewById(R.id.imgHome);
        imgCart = findViewById(R.id.imgCart);
        imgProfile = findViewById(R.id.imgProfile);
        txtHome = findViewById(R.id.txtHome);
        txtCart = findViewById(R.id.txtCart);
        txtProfile = findViewById(R.id.txtProfile);
        pageMain = findViewById(R.id.pageMain);
    }
}