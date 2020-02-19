package mkasperski.com.gamerdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1 ;
    GoogleSignInClient mGoogleSignInClient;




    /// views

    Button mRegisterButton, mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////init views
        mRegisterButton = findViewById(R.id.register_button);
        mLoginButton = findViewById(R.id.login_button);


        //handle register button click
        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /// start RegisterActivity
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));


            }

        });
        //login activity
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class
                ));
            }
        });
    }
}
