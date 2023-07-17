package com.nqmgaming.assignment_minhnqph31902.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nqmgaming.assignment_minhnqph31902.DAO.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    ImageButton imgBackForgotPassword;
    EditText edtEmailForgotPassword, edtUsernameForgotPassword;
    TextView tvRegisterHere, tvLoginHere;
    Button btnSendForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        imgBackForgotPassword = findViewById(R.id.btnBackForgotPasswordOne);

        edtEmailForgotPassword = findViewById(R.id.edtEmailForgotPassword);
        edtUsernameForgotPassword = findViewById(R.id.edtUsernameForgotPassword);

        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        tvLoginHere = findViewById(R.id.tvBackToLogin);

        btnSendForgotPassword = findViewById(R.id.btnResetPassword);

        imgBackForgotPassword.setOnClickListener(v -> {
            finish();
        });

        tvRegisterHere.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, RegisterActivity.class));
            finish();
        });

        tvLoginHere.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });

        btnSendForgotPassword.setOnClickListener(v -> {
            String email = edtEmailForgotPassword.getText().toString().trim();
            String username = edtUsernameForgotPassword.getText().toString().trim();
            if (username.isEmpty()) {
                edtUsernameForgotPassword.setError("Username is required!");
                edtUsernameForgotPassword.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                edtEmailForgotPassword.setError("Email is required!");
                edtEmailForgotPassword.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmailForgotPassword.setError("Please provide a valid email!");
                edtEmailForgotPassword.requestFocus();
                return;
            }


            UserDAO userDAO = new UserDAO(ForgotPasswordActivity.this);
            if (userDAO.checkUsernameAndEmail(username, email)) {
                UserDTO userDTO = userDAO.checkUsernameAndEmailAfter(username, email);
                int id = userDAO.getIdByUsernameAndEmail(username, email);
                userDTO.setId(id);
                Toast.makeText(this, id+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, SetNewPasswordActivity.class);
                String idString = String.valueOf(id);
                intent.putExtra("idDTO",idString);
                intent.putExtra("userDTO", userDTO.getUsername());
                intent.putExtra("emailDTO", userDTO.getEmail());
                intent.putExtra("passwordDTO", userDTO.getPassword());
                intent.putExtra("firstNameDTO", userDTO.getFirstname());
                intent.putExtra("lastNameDTO", userDTO.getLastname());
                startActivity(intent);
                finish();
            } else {
                edtUsernameForgotPassword.setError("Username or email is incorrect!");
                edtUsernameForgotPassword.requestFocus();
                return;
            }


        });

    }
}