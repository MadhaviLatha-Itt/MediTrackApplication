package com.example.madhavilatha.meditrack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout profile;
    private Button Logout_id;
    private TextView email, name;
    private GoogleApiClient googleApiClient;
    private SignInButton SignIn;
    private ImageView profile_pic;
    private static final int REQ_CODE=9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profile=(LinearLayout) findViewById(R.id.profile);
        profile_pic=(ImageView) findViewById(R.id.profile_pic);
        Logout_id=(Button) findViewById(R.id.Logout_id);
        email=(TextView) findViewById(R.id.email);
        name=(TextView) findViewById(R.id.name);
        SignIn=(SignInButton) findViewById(R.id.SignIn);
        SignIn.setOnClickListener(this);
        Logout_id.setOnClickListener(this);
        profile.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SignIn:
                SignIn();
                break;
            case R.id.Logout_id:
                Logout_id();
                break;
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void SignIn(){
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }
    private void Logout_id(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
             }
        ); }
    private void handleResults(GoogleSignInResult result){
        if (result.isSuccess()) {
            GoogleSignInAccount account=result.getSignInAccount();
            String Name=account.getDisplayName();
            String Email=account.getEmail();
            String img_url=account.getPhotoUrl().toString();
            name.setText(Name);
            email.setText(Email);
            Glide.with(this).load(img_url).into(profile_pic);
            updateUI(true);
        }
        else {updateUI(false);}
    }
    private void updateUI(boolean IsLogin){
        if (IsLogin){
            profile.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        }
        else {
            profile.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResults(result);
        }
    }
}
