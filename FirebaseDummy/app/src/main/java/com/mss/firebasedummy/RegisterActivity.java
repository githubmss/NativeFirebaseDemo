package com.mss.firebasedummy;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mss.firebasedummy.AppController.mAuth;

/**
 * Created by deepakgupta on 7/2/17.
 */

public class RegisterActivity extends AppCompatActivity {
    @Bind(R.id.edit_email)
    EditText editEmail;
    @Bind(R.id.edit_password)
    EditText editPassword;
    @Bind(R.id.btn_send)
    Button btnSend;
    private static final String TAG = "Register";
    FirebaseAuth.AuthStateListener mAuthListener;
    private ViewGroup viewGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regster);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    updateUI(user);
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void createAccount(String email, String password) {
        AppUtils.dialog(RegisterActivity.this);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            AppUtils.dismissProgressDialog();
                            editEmail.setText("");
                            editPassword.setText("");
                            Toast.makeText(getApplicationContext(), "user registered successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            AppUtils.dismissProgressDialog();
                            Toast.makeText(RegisterActivity.this, "failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(getApplicationContext(), user.getUid() + user.getEmail(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validations() {
        if (editEmail.getText().toString().trim().isEmpty()) {
            AppUtils.showErrorOnTop(viewGroup, RegisterActivity.this, "Please enter your email.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString().trim()).matches()) {
            AppUtils.showErrorOnTop(viewGroup, RegisterActivity.this, "Please enter valid email.");
            return false;
        } else if (editPassword.getText().toString().trim().isEmpty()) {
            AppUtils.showErrorOnTop(viewGroup, RegisterActivity.this, "Please enter your password");
            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.btn_send)
    public void onClick() {
        if (validations()) {
            createAccount(editEmail.getText().toString(), editPassword.getText().toString());
        }
    }
}
