package com.nqmgaming.assignment_minhnqph31902.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nqmgaming.assignment_minhnqph31902.DAO.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.R;

public class SetNewPasswordActivity extends AppCompatActivity {

    //declare variables
    ImageButton imgBackSetNewPassword;
    Button btnSetNewPassword;
    TextView tvLoginHere, tvRegisterHere;
    EditText edtNewPassword, edtConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        //mapping variables with view
        imgBackSetNewPassword = findViewById(R.id.btnBackForgotPasswordTow);

        btnSetNewPassword = findViewById(R.id.btnResetPasswordFinal);

        tvLoginHere = findViewById(R.id.tvBackToLogin);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);

        edtNewPassword = findViewById(R.id.edtEmailForgotPassword);
        edtConfirmNewPassword = findViewById(R.id.edtEmailForgotPasswordConfirm);

        //set event click for buttons and textview

        //setOnCLickListener for imgBackSetNewPassword
        imgBackSetNewPassword.setOnClickListener(v -> {
            startActivity(new Intent(SetNewPasswordActivity.this, LoginActivity.class));
            finish();
        });

        //setOnCLickListener for tvLoginHere
        tvLoginHere.setOnClickListener(v -> {
            startActivity(new Intent(SetNewPasswordActivity.this, LoginActivity.class));
            finish();
        });

        //setOnCLickListener for tvRegisterHere
        tvRegisterHere.setOnClickListener(v -> {
            startActivity(new Intent(SetNewPasswordActivity.this, RegisterActivity.class));
            finish();
        });

        //Get data from ForgotPasswordActivity
        Intent intent = getIntent();

        String idDTOString = intent.getStringExtra("idDTO");
        String username = intent.getStringExtra("userDTO");
        String email = intent.getStringExtra("emailDTO");
        String password = intent.getStringExtra("passwordDTO");
        String firstName = intent.getStringExtra("firstNameDTO");
        String lastName = intent.getStringExtra("lastNameDTO");

        //setOnCLickListener for btnSetNewPassword
        btnSetNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from edtNewPassword and edtConfirmNewPassword
                String newPassword = edtNewPassword.getText().toString().trim();
                String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();

                //check data

                //check if newPassword is empty
                if (newPassword.isEmpty()) {
                    edtNewPassword.setError("New password is required!");
                    edtNewPassword.requestFocus();
                    return;
                }

                if (confirmNewPassword.isEmpty()) {
                    edtConfirmNewPassword.setError("Confirm new password is required!");
                    edtConfirmNewPassword.requestFocus();
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    edtConfirmNewPassword.setError("Confirm new password does not match!");
                    edtConfirmNewPassword.requestFocus();
                    return;
                }

                UserDAO userDAO = new UserDAO(SetNewPasswordActivity.this);

                //check if newPassword is the same as old password
                if (userDAO.checkPassword(newPassword)) {
                    edtNewPassword.setError("Please enter a different password!");
                    edtNewPassword.requestFocus();
                    return;
                }

                //update new password to database
                int idDTO = Integer.parseInt(idDTOString);
                UserDTO userDTO = new UserDTO(idDTO, username, email, newPassword, firstName, lastName);

                //check if update successfully
                int result = userDAO.updateUser(userDTO);
                if (result > 0) {
                    Toast.makeText(SetNewPasswordActivity.this, "Reset password successfully!", Toast.LENGTH_SHORT).show();

                    //Alert Dialog to notify successful password reset
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPasswordActivity.this);
                    builder.setTitle("Reset password successfully!");
                    builder.setMessage("Please log in again!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", (dialog, which) -> {

                        //go to LoginActivity
                        startActivity(new Intent(SetNewPasswordActivity.this, LoginActivity.class));
                        finish();

                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(SetNewPasswordActivity.this, "Reset password failed!" + result, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}