package bulksms.tdd.tddbulksms.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import bulksms.tdd.tddbulksms.R;

public class StartActivity extends AppCompatActivity {
    Button btDeliveredList, btUndeliveredList, btSettings;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initview();


    }

    private void initview() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btDeliveredList = (Button) findViewById(R.id.btDeliverdList);
        btUndeliveredList = (Button) findViewById(R.id.btUndeliverdList);
        btSettings = (Button) findViewById(R.id.btSettings);
        initListener();
    }

    private void initListener() {
        btDeliveredList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskFinished.class);
                startActivity(intent);
            }
        });

        btUndeliveredList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UndeliveredList.class);
                startActivity(intent);
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

}
