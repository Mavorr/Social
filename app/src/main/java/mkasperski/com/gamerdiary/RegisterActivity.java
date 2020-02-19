package mkasperski.com.gamerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

        ///views
    EditText mEmailEt, mPasswordEt;
    Button mRegisterButton;
    TextView mHaveAccountTv;

    //progressbar to display
ProgressDialog ProgressDialog;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Actionbar its title
        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Create an Account");
        /// enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ///init
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordET);
        mRegisterButton = findViewById(R.id.register_button);
        mHaveAccountTv = findViewById(R.id.have_accountTv);
       // In the onCreate() method, initialize the FirebaseAuth instance.
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ProgressDialog = new ProgressDialog(this);
        ProgressDialog.setMessage("Registring User...");

        //handle register button click
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///input email, password
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                ///validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    // set error and focus to email edtitext
                    mEmailEt.setError("Invaid Email");
                    mEmailEt.setFocusable(true);
                }
                else if (password.length()<6){
                    /// set error and focus to password editext
                    mPasswordEt.setError("Password length at least 6 characters");
                    mPasswordEt.setFocusable(true);
                }
                else {
                    registerUser(email, password); // register the user
                }
            }
        });
        //handle login textview click listener

        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        }


    private void registerUser(String email, String password) {
        /// email and password pattern is valid, show progressm diaglo and start registering user
        ProgressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start registration activity
                            ProgressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Get user email and uid from auth
                            String email = user.getEmail();
                            String uid = user.getUid();
                            //When user is registered store user info in firebase reatime database too
                            //using HashMap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            // put info in hashmap
                            hashMap.put("emial", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "");  //will add later (e.g. edit profiles)
                            hashMap.put("phone", ""); //will add later (e.g. edit profiles)
                            hashMap.put("image", ""); //will add later (e.g. edit profiles)
                            //firebaseDatabese database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            // path to store user data named "Users"
                            DatabaseReference reference = database.getReference("Users");
                            // put data with hashmap in database
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                                   finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            ProgressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            /// error, dismiss progress fialog and get and show the error message
                    ProgressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


            }

                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); /// go previous activity
        return super.onSupportNavigateUp();
    }
}

