package com.example.madcampweek2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Fragment1 extends Fragment implements View.OnClickListener {

    private boolean isOpen = false;
    private FloatingActionButton fab;
    private FloatingActionButton itemFab;


    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.mainFab);
        itemFab = (FloatingActionButton) rootView.findViewById(R.id.insertfab);

        fab.setOnClickListener(this);
        itemFab.setOnClickListener(this);

        return rootView;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainFab:

                if(!isOpen){
                    //ObjectAnimator.ofFloat(itemFab, "translationY", -400f).start();
                    ObjectAnimator.ofFloat(itemFab, "translationY", -200f).start();
                    isOpen = true;
                }
                else{
                    //ObjectAnimator.ofFloat(itemFab, "translationY", 0f).start();
                    ObjectAnimator.ofFloat(itemFab, "translationY", 0f).start();
                    isOpen = false;
                }
                break;
            case R.id.insertfab:
                Toast.makeText(getContext(),"메뉴 버튼 클릭", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}