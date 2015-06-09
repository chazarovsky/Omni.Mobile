package net.omnidf.omnidf;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends ActionBarActivity {

    Button buttonLogin;
    LoginButton buttonLoginFb;
    CallbackManager callbackManager;
    Resources res;
    Context ctx;
    //RequestQueue requestQueue = Volley.newRequestQueue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        ctx = getApplicationContext();
        FacebookSdk.sdkInitialize(ctx);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

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
                //Profile profile = Profile.getCurrentProfile();
//                String email;
//                String pass = profile.getId();
//                String name = profile.getName();
//                CharSequence text = String.format(res.getString(R.string.welcome_user), profile.getName());

//                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
//                                Log.wtf("jsonObject Response: ", jsonObject.toString());
//                                Log.wtf("grapResponse Response: ", graphResponse.toString());
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "email");
//                graphRequest.setParameters(parameters);
//                graphRequest.executeAsync();

//                requestQueue.add(registerUserInServer(email, pass, name));
//
//                Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();

                startUtilActivity();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {

            }
        });

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

//        if(requestQueue != null){
//            requestQueue.stop();
//        }
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

//    private JsonObjectRequest registerUserInServer(String email, String pass, String name){
//        JSONObject jsonParams = null;
//        try {
//            jsonParams = new JSONObject()
//                    .put("email", email)
//                    .put("pass", pass)
//                    .put("name", name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return new JsonObjectRequest(Request.Method.POST, Const.URL_NEWUSER, jsonParams, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.wtf("Response", response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.wtf("Server request ERROR: ", error.toString());
//            }
//        });
//    }

}
