package com.mss.firebasedummy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mss.firebasedummy.AppController.mAuth;

/**
 * Created by deepakgupta on 7/2/17.
 */

public class HomeActivity extends AppCompatActivity {

    FirebaseUser mUser;
    private static final String TAG = "Update";

    Uri filepath, profilePic = null;
    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.btn_signout)
    Button btnSignout;
    @Bind(R.id.btn_change_pass)
    Button btnChangePass;
    @Bind(R.id.btn_update_user)
    Button btnUpdateUser;
    private StorageReference mStorageRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        initUi();
    }

    private void initUi() {

        mUser = mAuth.getCurrentUser();
    }

    private void updatePassword() {
        mUser.updatePassword(editName.getText().toString()).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if (task.isSuccessful()) {
                                                  AppUtils.dismissProgressDialog();
                                                  mAuth.signOut();
                                                  Intent loginIntent = new Intent(HomeActivity.this, MainActivity.class);
                                                  loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                  startActivity(loginIntent);
                                                  finish();
                                                  Log.d(TAG, "User password updated.");
                                              } else {
                                                  AppUtils.dismissProgressDialog();
                                                  Log.d(TAG, "failed .");
                                              }
                                          }
                                      }
                );
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.btn_change_pass, R.id.btn_signout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_pass:
                if (editName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter new password.", Toast.LENGTH_SHORT).show();
                } else {
                    AppUtils.dialog(HomeActivity.this);
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(mUser.getEmail(), new AppPreferences(HomeActivity.this).getPrefrenceString("pass"));
                    // Prompt the user to re-provide their sign-in credentials
                    mUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    updatePassword();
                                    Log.d(TAG, "User re-authenticated.");
                                }
                            });
                }
                break;
            case R.id.btn_signout:
                mAuth.signOut();
                new AppPreferences(getApplicationContext()).setPrefrenceString("pass", "");
                Intent loginIntent = new Intent(HomeActivity.this, MainActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
                break;
        }
    }

    @OnClick(R.id.btn_update_user)
    public void onClick() {
        startActivity(new Intent(HomeActivity.this, UpdateProfileActivity.class));
    }
}
