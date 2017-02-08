package com.mss.firebasedummy.FireBaseDatabase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.mss.firebasedummy.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by deepakgupta on 8/2/17.
 */

public class CreateUserActivity extends AppCompatActivity {

    public static String FIREBASEURL = "https://fir-testdemo-7043a.firebaseio.com/";
    @Bind(R.id.edit_email)
    EditText editEmail;
    @Bind(R.id.edit_password)
    EditText editPassword;
    @Bind(R.id.btn_send)
    Button btnSend;
    private Firebase myFirebaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_create_user);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        myFirebaseRef = new Firebase(FIREBASEURL);
    }

    @OnClick(R.id.btn_send)
    public void onClick() {
//        myFirebaseRef.child("users").setValue(editEmail.getText().toString());
        Firebase usersRef = myFirebaseRef.child("android");
        Map<String, String> users = new HashMap<String, String>();
        users.put("Email", editEmail.getText().toString());
        Map<String, Map<String, String>> usersList = new HashMap<String, Map<String, String>>();
        usersList.put("users", users);
        usersRef.setValue(usersList);
    }

}



