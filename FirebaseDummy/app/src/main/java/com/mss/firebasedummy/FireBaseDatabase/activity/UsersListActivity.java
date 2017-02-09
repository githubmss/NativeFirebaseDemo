package com.mss.firebasedummy.FireBaseDatabase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mss.firebasedummy.FireBaseDatabase.adapter.UserListAdapter;
import com.mss.firebasedummy.FireBaseDatabase.model.UserModel;
import com.mss.firebasedummy.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by deepakgupta on 9/2/17.
 */

public class UsersListActivity extends AppCompatActivity {

    @Bind(R.id.rv_userList)
    RecyclerView rvUserList;
    UserListAdapter adapter;
    DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        database = FirebaseDatabase.getInstance().getReference();
        rvUserList.setLayoutManager(new LinearLayoutManager(UsersListActivity.this));
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UserModel> userModelsList = new ArrayList<UserModel>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    UserModel user = noteDataSnapshot.getValue(UserModel.class);
                    userModelsList.add(user);
                }
                adapter = new UserListAdapter(UsersListActivity.this, userModelsList);
                rvUserList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
