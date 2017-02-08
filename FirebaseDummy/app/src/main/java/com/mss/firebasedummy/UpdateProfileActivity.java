package com.mss.firebasedummy;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mss.firebasedummy.AppController.mAuth;

/**
 * Created by deepakgupta on 7/2/17.
 */

public class UpdateProfileActivity extends AppCompatActivity {

    Uri filepath, profilePic = null;
    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.img_pic)
    ImageView imgPic;
    @Bind(R.id.btn_save)
    Button btnSave;
    private StorageReference mStorageRef;
    FirebaseUser mUser;
    private static final String TAG = "Update";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        ButterKnife.bind(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        initUi();
    }

    private void initUi() {
        mUser = mAuth.getCurrentUser();
    }

    private void userReauthenticated() {
        AuthCredential credential = EmailAuthProvider
                .getCredential(mUser.getEmail(), "123456");
        mUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateProfile() {
        AppUtils.dialog(UpdateProfileActivity.this);
        AuthCredential credential = EmailAuthProvider
                .getCredential(mUser.getEmail(), new AppPreferences(UpdateProfileActivity.this).getPrefrenceString("pass"));
        // Prompt the user to re-provide their sign-in credentials
        mUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUserInfo();
                        Log.d(TAG, "User re-authenticated.");
                    }
                });
    }

    private void updateUserInfo() {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(editName.getText().toString())
                .setPhotoUri(profilePic)
                .build();
        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AppUtils.dismissProgressDialog();
                            Log.d(TAG, "User profile updated.");
                        } else {
                            AppUtils.dismissProgressDialog();
                        }
                    }
                });
    }

    private void selectProfile() {
        final Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_picture);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        LinearLayout llTakePic = (LinearLayout) dialog.findViewById(R.id.ll_take_pic);
        LinearLayout llChoosePic = (LinearLayout) dialog.findViewById(R.id.ll_choose_pic);
        llTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        llChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryImageIntent, 3);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                try {
                    filepath = data.getData();
                    Glide.with(UpdateProfileActivity.this).load(filepath).into(imgPic);
                    uploadFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 3) {
                try {
                    filepath = data.getData();
                    Glide.with(UpdateProfileActivity.this).load(filepath).into(imgPic);
                    uploadFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadFile() {
        //if there is a file to upload
        if (filepath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference riversRef = mStorageRef.child("images/pic.jpg");
            riversRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            profilePic = downloadUrl;
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }


    @OnClick({R.id.img_pic, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_pic:
                selectProfile();
                break;
            case R.id.btn_save:
                if (editName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter user name", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile();
                }
                break;
        }
    }
}
