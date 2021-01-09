package com.example.madcampweek2;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


public class Fragment1 extends Fragment implements View.OnClickListener {

    private boolean isOpen = false;
    private View rootView;
    private FloatingActionButton fab;
    private FloatingActionButton itemFab;
    private ListView listView;
    private ArrayList<String> names = new ArrayList<String>();
    private ContactList contactList = ContactList.getInstance();
    private String name;
    private String userid;
    private String number;
    private String email;
    private FirebaseUser firebaseUser;
    private ArrayAdapter<String> listViewAdapter;



    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            String uid = getArguments().getString("uid");
            Toast.makeText(getContext(), "uid = " + uid, Toast.LENGTH_SHORT).show();
        }

        rootView = inflater.inflate(R.layout.fragment_1, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.mainFab);
        itemFab = (FloatingActionButton) rootView.findViewById(R.id.insertfab);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = firebaseUser.getUid();

        fab.setOnClickListener(this);
        itemFab.setOnClickListener(this);

        getNames(names);

        listView = (ListView) rootView.findViewById(R.id.f1_listview);

        listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                names
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name = contactList.get(position).getName();
                number = contactList.get(position).getNumber();
                email = contactList.get(position).getEmail();
                show(position, false);
            }
        });

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
                //Toast.makeText(getContext(),"메뉴 버튼 클릭", Toast.LENGTH_SHORT).show();
                show(0, true);
                break;
        }
    }


    public void show(int position, boolean addOrNot) {
        if (!addOrNot) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_contact, null);
            builder.setView(view);

            final EditText nameText = view.findViewById(R.id.name_text);
            final EditText numberText = view.findViewById(R.id.number_text);
            final EditText emailText = view.findViewById(R.id.email_text);

            final Button confirm = view.findViewById(R.id.confirm_button);
            final Button delete = view.findViewById(R.id.delete_button);
            final Button call = view.findViewById(R.id.call_button);

            nameText.setText(name);
            numberText.setText(number);
            emailText.setText(email);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = ContactList.get(position).getId();

                    name = nameText.getText().toString();
                    number = numberText.getText().toString();
                    email = emailText.getText().toString();

                    Contact obj = new Contact(0, userid, name, number, email);
                    PUD pud = new PUD();
                    pud.updateContact(id, obj, listViewAdapter);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = ContactList.get(position).getId();
                    PUD pud = new PUD();
                    pud.deleteContact(id, listViewAdapter);
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            AlertDialog dialog = builder.create();

            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_contact, null);
            builder.setView(view);

            final EditText nameText = view.findViewById(R.id.name_text);
            final EditText numberText = view.findViewById(R.id.number_text);
            final EditText emailText = view.findViewById(R.id.email_text);

            final Button confirm = view.findViewById(R.id.confirm_button);
            final Button delete = view.findViewById(R.id.delete_button);
            final Button call = view.findViewById(R.id.call_button);

            name = "";
            number = "";
            email = "";

            nameText.setText(name);
            numberText.setText(number);
            emailText.setText(email);

            AlertDialog dialog = builder.create();
            dialog.show();

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int id = ContactList.get(position).getId();

                    Toast.makeText(getContext(), userid, Toast.LENGTH_SHORT).show();

                    name = nameText.getText().toString();
                    number = numberText.getText().toString();
                    email = emailText.getText().toString();

                    Contact obj = new Contact(0, userid, name, number, email);
                    PUD pud = new PUD();
                    pud.postContact(obj, listViewAdapter);

                    getFragmentManager()
                            .beginTransaction()
                            .detach(Fragment1.this)
                            .attach(Fragment1.this)
                            .commit();

                    dialog.dismiss();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



        }
    }

    public void getNames(ArrayList<String> names) {
        JsonPlaceHolderAPI mAPI = NetRetrofit.getInstance().getService();

        Call<List<Contact>> getCall = mAPI.getContacts();
        getCall.enqueue(new Callback<List<Contact>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.isSuccessful()){
                    names.clear();
                    contactList.clear();
                    List<Contact> mList = response.body();
                    for (Contact contact: mList) {
                        if (contact.getUserid().equals(userid)) {
                            names.add(contact.getName());
                            contactList.add(contact);
                            contactList.sort1();
                        }
                    }
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Status Code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.d(TAG, "Fail msg: " + t.getMessage());
            }
        });
    }
}