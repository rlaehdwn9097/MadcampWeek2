package com.example.madcampweek2;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.tabs.TabLayout;

public class ServiceActivity extends FragmentActivity {

    TabLayout tabs;

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    //---------------사진추가__위------------------------------------------------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //call super
        super.onActivityResult(requestCode, resultCode, data);
    }

    //--------------사진추가__아래--------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Contacts"));
        tabs.addTab(tabs.newTab().setText("Photos"));
        tabs.addTab(tabs.newTab().setText("TAB3"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0)
                    selected = fragment1;
                else if(position == 1)
                    selected = fragment2;
                else if(position == 2)
                    selected = fragment3;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}