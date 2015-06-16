package net.omnidf.omnidf;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import net.omnidf.omnidf.activities.UtilActivity;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    Button buttonLogin;
    LoginButton buttonLoginFb;
    CallbackManager callbackManager;
    Resources appResources;
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        appResources = getResources();
        appContext = getApplicationContext();
        FacebookSdk.sdkInitialize(appContext);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        if(AccessToken.getCurrentAccessToken() != null) startUtilActivity();

        buttonLoginFb = (LoginButton) findViewById(R.id.buttonLoginFb);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUtilActivity();
            }
        });

        buttonLoginFb.setReadPermissions(Arrays.asList("public_profile, email"));
        buttonLoginFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(appContext, appResources.getString(R.string.welcome_user) , Toast.LENGTH_SHORT).show();
                startUtilActivity();
            }

            @Override
            public void onCancel() {
                Toast.makeText(appContext, appResources.getString(R.string.login_cancel), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(appContext, appResources.getString(R.string.login_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    // Helper Methods

    private void startUtilActivity(){
        Intent intent = new Intent(getBaseContext(), UtilActivity.class);
        startActivity(intent);
        finish();
    }


}
