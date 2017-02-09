package com.mss.firebasedummy.FireBaseDatabase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mss.firebasedummy.FireBaseDatabase.model.UserModel;
import com.mss.firebasedummy.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by deepakgupta on 8/2/17.
 */

public class UpdateUserActivity extends AppCompatActivity {
    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.edit_desc)
    EditText editDesc;
    @Bind(R.id.btn_send)
    Button btnSend;
    private UserModel user;
    DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_firebase_user);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        database = FirebaseDatabase.getInstance().getReference();
        user = (UserModel) getIntent().getSerializableExtra("update");
        if (user != null) {
            editName.setText(user.getName());
            editDesc.setText(user.getDesc());
        }
    }

    @OnClick(R.id.btn_send)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                addUser();
                break;
        }
    }

    private void addUser() {
        if (user != null) {
            user.setName(editName.getText().toString());
            user.setDesc(editDesc.getText().toString());
            database.child("users").child(user.getUid()).setValue(user);
            editName.setText("");
            editDesc.setText("");
            finish();
        }
    }
}



