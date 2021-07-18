package android.termix.ssc.ce.sharif.edu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginSignupActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabLayoutMediator tabLayoutMediator;
    ViewPager2 viewPager;

    private String[] titles = new String[]{"Login", "SignUp"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sign_up_page);
        // removing toolbar elevation
        getSupportActionBar().setElevation(0);

        viewPager = findViewById(R.id.login_signup_pager);
        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));
        tabLayout = findViewById(R.id.signup_login_tablayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();
    }

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SignUpFragment();
                case 1:
                    return new LoginFragment();
            }
            return new SignUpFragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
}

