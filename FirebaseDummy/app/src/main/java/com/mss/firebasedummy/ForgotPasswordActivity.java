package com.mss.firebasedummy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mss.firebasedummy.AppController.mAuth;

/**
 * Created by deepakgupta on 8/2/17.
 */

public class ForgotPasswordActivity extends AppCompatActivity {
    @Bind(R.id.edit_email)
    EditText editEmail;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
    }

    public boolean validations() {
        if (editEmail.getText().toString().trim().isEmpty()) {
            AppUtils.showErrorOnTop(viewGroup, ForgotPasswordActivity.this, "Please enter your email.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString().trim()).matches()) {
            AppUtils.showErrorOnTop(viewGroup, ForgotPasswordActivity.this, "Please enter valid email.");
            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.btn_submit)
    public void onClick() {
        if (validations()) {
            AppUtils.dialog(ForgotPasswordActivity.this);
            mAuth.sendPasswordResetEmail(editEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AppUtils.dismissProgressDialog();
                                Toast.makeText(getApplicationContext(), "Email sent.", Toast.LENGTH_SHORT).show();
                                Log.d("forgot", "Email sent.");
                                finish();
                            } else {
                                AppUtils.dismissProgressDialog();
                                Toast.makeText(getApplicationContext(), "Email not exists.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
