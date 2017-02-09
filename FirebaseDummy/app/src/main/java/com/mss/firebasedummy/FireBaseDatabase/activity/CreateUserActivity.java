package com.mss.firebasedummy.FireBaseDatabase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mss.firebasedummy.AppUtils;
import com.mss.firebasedummy.FireBaseDatabase.model.UserModel;
import com.mss.firebasedummy.LoginActivity;
import com.mss.firebasedummy.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by deepakgupta on 8/2/17.
 */

public class CreateUserActivity extends AppCompatActivity {
    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.edit_desc)
    EditText editDesc;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.btn_get_list)
    Button btnGetList;

//    public static String FIREBASEURL = "https://fir-testdemo-7043a.firebaseio.com/";

    private Firebase myFirebaseRef;
    private UserModel user;
    DatabaseReference database;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_create_user);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        database = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick({R.id.btn_send, R.id.btn_get_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if (validations()) {
                    addUser();
                }
                break;

            case R.id.btn_get_list:
                startActivity(new Intent(CreateUserActivity.this, UsersListActivity.class));
                break;
        }
    }

    private void addUser() {
        user = new UserModel();
        user.setUid(database.child("users").push().getKey());
        user.setName(editName.getText().toString());
        user.setDesc(editDesc.getText().toString());
        database.child("users").child(user.getUid()).setValue(user);
        editName.setText("");
        editDesc.setText("");
    }

    public boolean validations() {
        if (editName.getText().toString().trim().isEmpty()) {
            AppUtils.showErrorOnTop(viewGroup, CreateUserActivity.this, "Please enter your name.");
            return false;
        } else {
            return true;
        }
    }

/*
    @OnClick(R.id.btn_send)
    public void onClick() {
       *//* myFirebaseRef.child("users").setValue(editEmail.getText().toString());
        Firebase usersRef = myFirebaseRef.child("android");
        Map<String, String> users = new HashMap<String, String>();
        users.put("Email", editEmail.getText().toString());
        Map<String, Map<String, String>> usersList = new HashMap<String, Map<String, String>>();
        usersList.put("users", users);
        usersRef.setValue(usersList);*//*

        Firebase ref = new Firebase("https://fir-testdemo-7043a.firebaseio.com/android/users");
        // Attach an listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }*/

}



