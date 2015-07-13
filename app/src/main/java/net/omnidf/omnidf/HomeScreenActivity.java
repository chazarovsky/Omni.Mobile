package net.omnidf.omnidf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.omnidf.omnidf.rest.OmniServer;


public class HomeScreenActivity extends AppCompatActivity {

    EditText inputOrigin;
    EditText inputDestination;
    Button buttonGetRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initViews();
        setUpGetRouteButton();
    }

    private void initViews(){
        inputOrigin = (EditText) findViewById(R.id.inputOrigin);
        inputDestination = (EditText) findViewById(R.id.inputDestination);
        buttonGetRoute = (Button) findViewById(R.id.buttonGetRoute);
    }

    private void setUpGetRouteButton(){
        buttonGetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRoutesSearchActivity(inputOrigin.getText().toString(),
                        inputDestination.getText().toString());
            }
        });
    }

    private void startRoutesSearchActivity(String origin, String destination){
        Intent intent = new Intent(this, RoutesSearchActivity.class);
        intent.putExtra(OmniServer.ORIGIN_QUERY, origin)
                .putExtra(OmniServer.DESTINATION_QUERY, destination);
        startActivity(intent);
        finish();
    }
}
