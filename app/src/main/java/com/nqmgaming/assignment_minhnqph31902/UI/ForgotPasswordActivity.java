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

    //declare variables
    ImageButton imgBackForgotPassword;
    EditText edtEmailForgotPassword, edtUsernameForgotPassword;
    TextView tvRegisterHere, tvLoginHere;
    Button btnSendForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //mapping variables with view
        imgBackForgotPassword = findViewById(R.id.btnBackForgotPasswordOne);

        edtEmailForgotPassword = findViewById(R.id.edtEmailForgotPassword);
        edtUsernameForgotPassword = findViewById(R.id.edtUsernameForgotPassword);

        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        tvLoginHere = findViewById(R.id.tvBackToLogin);

        btnSendForgotPassword = findViewById(R.id.btnResetPassword);

        //set event click for buttons and textview

        //setOnCLickListener for imgBackForgotPassword
        imgBackForgotPassword.setOnClickListener(v -> {

            finish();

        });

        //setOnCLickListener for tvRegisterHere
        tvRegisterHere.setOnClickListener(v -> {

            startActivity(new Intent(ForgotPasswordActivity.this, RegisterActivity.class));
            finish();

        });

        //setOnCLickListener for tvLoginHere
        tvLoginHere.setOnClickListener(v -> {

            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();

        });

        btnSendForgotPassword.setOnClickListener(v -> {

            //get data from edittext
            String email = edtEmailForgotPassword.getText().toString().trim();
            String username = edtUsernameForgotPassword.getText().toString().trim();

            //validate data

            //check if username is empty
            if (username.isEmpty()) {
                edtUsernameForgotPassword.setError("Username is required!");
                edtUsernameForgotPassword.requestFocus();
                return;
            }

            //check if email is empty
            if (email.isEmpty()) {
                edtEmailForgotPassword.setError("Email is required!");
                edtEmailForgotPassword.requestFocus();
                return;
            }

            //check if email is valid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmailForgotPassword.setError("Please provide a valid email!");
                edtEmailForgotPassword.requestFocus();
                return;
            }

            //check if username and email is correct

            //create userDAO
            UserDAO userDAO = new UserDAO(ForgotPasswordActivity.this);


            if (userDAO.checkUsernameAndEmail(username, email)) {

                //get userDTO
                UserDTO userDTO = userDAO.checkUsernameAndEmailAfter(username, email);

                //get id
                int id = userDAO.getIdByUsernameAndEmail(username, email);

                //set id for userDTO
                userDTO.setId(id);

                //Intent to SetNewPasswordActivity
                Intent intent = new Intent(ForgotPasswordActivity.this, SetNewPasswordActivity.class);

                String idString = String.valueOf(id);
                intent.putExtra("idDTO", idString);
                intent.putExtra("userDTO", userDTO.getUsername());
                intent.putExtra("emailDTO", userDTO.getEmail());
                intent.putExtra("passwordDTO", userDTO.getPassword());
                intent.putExtra("firstNameDTO", userDTO.getFirstname());
                intent.putExtra("lastNameDTO", userDTO.getLastname());

                //start activity
                startActivity(intent);
                finish();

            } else {

                //show error message
                edtUsernameForgotPassword.setError("Username or email is incorrect!");
                edtUsernameForgotPassword.requestFocus();
                return;

            }
        });

    }
}