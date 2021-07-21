package android.termix.ssc.ce.sharif.edu;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

public class LoginSignupActivity extends AppCompatActivity {
    private static WeakReference<LoginSignupActivity> loginSignupActivityWeakReference;
    private static final String[] titles = new String[]{"درون‌شد", "نام‌نویسی"};

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sign_up_page);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        loginSignupActivityWeakReference = new WeakReference<>(this);
        logo = findViewById(R.id.logoImage);

        ConstraintLayout constraintLayout = findViewById(R.id.root_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        ViewPager2 viewPager = findViewById(R.id.login_signup_pager);
        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));
        TabLayout tabLayout = findViewById(R.id.signup_login_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();
    }

    private static class ViewPagerFragmentAdapter extends FragmentStateAdapter {
        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new LoginFragment();
                case 1:
                    return new SignUpFragment();
            }
            return new LoginFragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }

    public static WeakReference<LoginSignupActivity> getLoginSignupActivityWeakReference() {
        return loginSignupActivityWeakReference;
    }

    public ImageView getLogo() {
        return logo;
    }

    public static View.OnFocusChangeListener getEditTextFocusChangeListener() {
        return (v, hasFocus) -> {
            LoginSignupActivity loginSignupActivity = LoginSignupActivity.getLoginSignupActivityWeakReference().get();
            if (loginSignupActivity != null) {
                if (hasFocus) {
                    loginSignupActivity.getLogo().setVisibility(View.GONE);
                } else {
                    loginSignupActivity.getLogo().setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public void goToMainActivity() {
        Intent intent = new Intent(LoginSignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public static boolean isEmailValid(String email) {
        return Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                ")*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01" +
                "-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z" +
                "0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-" +
                "9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0" +
                "b\\x0c\\x0e-\\x7f])+)])").matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() < 33 && password.length() > 7;
    }
}

