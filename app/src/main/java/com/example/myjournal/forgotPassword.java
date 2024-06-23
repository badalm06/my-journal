package com.example.myjournal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    EditText mforgotpassword;
    Button mpasswordrecoverbutton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mforgotpassword = findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton = findViewById(R.id.password_recover_btn);

        mpasswordrecoverbutton.setOnClickListener(v -> {
            String mail = mforgotpassword.getText().toString().trim();
            if (mail.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter your email first", Toast.LENGTH_SHORT).show();
            } else {
                sendPasswordRecoveryEmail(mail);
            }
        });
    }

    private void sendPasswordRecoveryEmail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(forgotPassword.this, "Recovery email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(forgotPassword.this, "Failed to send recovery email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
