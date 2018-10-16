package com.example.nathan.automaticalcohol.Activities;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.nathan.automaticalcohol.Fragments.TabCookbookFragment;
import com.example.nathan.automaticalcohol.Fragments.TabHomeFragment;
import com.example.nathan.automaticalcohol.Fragments.TabInventoryFragment;
import com.example.nathan.automaticalcohol.Fragments.TabReportsFragment;
import com.example.nathan.automaticalcohol.Fragments.TabTabsFragment;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.SectionsPageAdapter;

public class BartenderActivity extends AppCompatActivity {

    private final String TAG = "BartenderActivity";

//    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;
    private String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bartender);
        Log.d(TAG, "onCreate: Starting");

//        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Setup the ViewPager with the sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        pin = getIntent().getStringExtra("PinActivity_passPin");

        Bundle bundle = new Bundle();
        bundle.putString("bartenderPin", pin);
        Log.e(TAG, "setupViewPager... pin is: "+ pin);

        // set MyFragment Arguments
        TabHomeFragment homeFragment = new TabHomeFragment();
        homeFragment.setArguments(bundle);
        adapter.addFragment(homeFragment, "Home");

        adapter.addFragment(new TabCookbookFragment(), "Cookbook");
        adapter.addFragment(new TabInventoryFragment(), "Inventory");
        adapter.addFragment(new TabReportsFragment(), "Reports");
        adapter.addFragment(new TabTabsFragment(), "Tabs");
        viewPager.setAdapter(adapter);
    }




}