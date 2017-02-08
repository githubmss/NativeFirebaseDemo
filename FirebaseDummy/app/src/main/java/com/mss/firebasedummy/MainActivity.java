package com.mss.firebasedummy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mss.firebasedummy.AppController.mAuth;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_signup)
    Button btnSignup;
    @Bind(R.id.btn_Login)
    Button btnLogin;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Bind(R.id.btn_forgot)
    Button btnForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("pic", user.getPhotoUrl() + "");
                    Toast.makeText(getApplicationContext(), user.getDisplayName() + user.getUid(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick({R.id.btn_signup, R.id.btn_Login, R.id.btn_forgot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.btn_Login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.btn_forgot:
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }

}
