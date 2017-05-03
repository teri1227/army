package com.hch.qewqs.project_army;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{


    android.app.FragmentManager manager = getFragmentManager();
    Fragment fragments;
    static Specialist specialist;
    //
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    CategorySpecialFragement  categorySepcial;
    CategoryMajorFragement categoryMajor;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    //
    private Firebase_info finfo;
    Bundle arg = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar action = getSupportActionBar();
        action.setDisplayHomeAsUpEnabled(true);
        action.setHomeButtonEnabled(true);
        specialist = new Specialist();

        manager.beginTransaction().replace(R.id.content_main, new MainViewActivity()).commit();

        categoryMajor = new CategoryMajorFragement();
        categorySepcial = new CategorySpecialFragement();
        finfo = new Firebase_info(ANONYMOUS);

        if(!isLogin()){
            mUsername = ANONYMOUS;
            Toast.makeText(MainActivity.this, "ANOY", Toast.LENGTH_SHORT).show();
        }else{
            mUsername = mFirebaseUser.getDisplayName().toString();
            Toast.makeText(MainActivity.this, "GOOGLE", Toast.LENGTH_SHORT).show();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        if(savedInstanceState != null){

        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 데이터 저장
        outState.putString("saved",finfo.getmUsername().toString());
    }

    @Override
    public boolean onSupportNavigateUp(){
        Bundle arg = null;
        fragments = new MainViewActivity();
        arg = new Bundle();
        arg.putSerializable("acc", finfo);
        fragments.setArguments(arg);
        manager.beginTransaction().replace(R.id.content_main, fragments).commit();
        return super.onSupportNavigateUp();
    }

    public String getUserName(){
        return mUsername;
    }

    public boolean isLogin(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseUser == null){
            return false;
        }else{
            return true;
        }
    }

    public String whoLogin(){
        if(!isLogin()){
            Toast.makeText(this, "로그인되지 않았습니다.", Toast.LENGTH_LONG).show();
            return ANONYMOUS;
        }else{
            Toast.makeText(this, "로그인이 되어 있습니다.", Toast.LENGTH_LONG).show();
            return mFirebaseUser.toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_in_menu:
                if(!isLogin()) {
                    Toast.makeText(this, "로그인.", Toast.LENGTH_SHORT).show();
                    run_sign_in_menu();
                    item.setTitle("로그아웃");
                }else if(isLogin()){
                    Toast.makeText(this, "로그아웃.", Toast.LENGTH_SHORT).show();
                    mFirebaseAuth.signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mUsername = ANONYMOUS;
                    finfo.setMfirebaseAuth(null);
                    finfo.setMfirebaseUser(null);
                    finfo.setmUsername(mUsername);
                    item.setTitle("로그인");
                }
                return true;
            case R.id.qa_menu:
                fragments = new QA();
                arg = new Bundle();
                arg.putSerializable("acc", finfo);
                fragments.setArguments(arg);
                manager.beginTransaction().replace(R.id.content_main, fragments).commit();
                return true;
            case R.id.settings_menu:
                fragments = new CategoryFourFragement();
                arg = new Bundle();
                arg.putSerializable("acc", finfo);
                fragments.setArguments(arg);
                manager.beginTransaction().replace(R.id.content_main, fragments).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void run_sign_in_menu(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Bad.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Good.",Toast.LENGTH_SHORT).show();
                            mFirebaseAuth = FirebaseAuth.getInstance();
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            mUsername = mFirebaseUser.getDisplayName().toString();

                            finfo.setMfirebaseAuth(mFirebaseAuth);
                            finfo.setMfirebaseUser(mFirebaseUser);
                            finfo.setmUsername(mUsername);

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void onClick_Category_One(){
        Toast.makeText(MainActivity.this, "Category_One.", Toast.LENGTH_SHORT).show();
        fragments = new CategoryOneFragement();
        manager.beginTransaction().replace(R.id.content_main, fragments).commit();
    }

    public void onClick_Category_Two(){
        Toast.makeText(MainActivity.this, "Category_Two.", Toast.LENGTH_SHORT).show();
    }

    public void onClick_Category_Three(){
        Toast.makeText(MainActivity.this, "Category_Three.", Toast.LENGTH_SHORT).show();
    }

    public void onClick_Category_Four(){
        Toast.makeText(MainActivity.this, "Category_Four.", Toast.LENGTH_SHORT).show();
        fragments = new CategoryFourFragement();
        arg = new Bundle();
        arg.putSerializable("acc", finfo);
        fragments.setArguments(arg);
        manager.beginTransaction().replace(R.id.content_main, fragments).commit();
    }

    public void category_one_change(int index){
        if(index == 1){
            manager.beginTransaction().replace(R.id.content_main,categorySepcial).commit();
        }
        if(index==2){
            manager.beginTransaction().replace(R.id.content_main,categoryMajor).commit();
        }

    }

}