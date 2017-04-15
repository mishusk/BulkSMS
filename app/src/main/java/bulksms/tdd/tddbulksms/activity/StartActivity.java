package bulksms.tdd.tddbulksms.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.helper.HelperMethods;


public class StartActivity extends AppCompatActivity {
    Button btDeliveredList, btUndeliveredList, btSettings,btClearDatabase,btAllUndeliveredList;
    FloatingActionButton fab;
    DbManager dbManager;

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
        btClearDatabase  = (Button) findViewById(R.id.btClearDatabase);
        btSettings = (Button) findViewById(R.id.btSettings);
        btAllUndeliveredList = (Button) findViewById(R.id.btAllUndeliverdList);
        dbManager = new DbManager(getApplicationContext());
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


        btClearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearDatabase().execute();
            }
        });

        btAllUndeliveredList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllUndeliveredList.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ClearDatabase extends AsyncTask<String, String, Boolean> {
        ProgressDialog progressDialog = HelperMethods.getProgressBar(getDialogContext());

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            HelperMethods.startProgressBar(progressDialog,"Please Wait...");
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            dbManager.cleanSmSinfoTable();
            dbManager.cleanPhoneinfoTable();
            dbManager.cleanRecentUndeliveredTable();
            dbManager.cleanDeliveredTable();
            dbManager.cleanUndeliveredTable();
            return  true;
        }

        @Override
        protected void onPostExecute(Boolean isTaskFinished) {
            HelperMethods.stopProgressBar(progressDialog);
            Toast.makeText(StartActivity.this, "Database Deleted", Toast.LENGTH_SHORT).show();

        }
    }

    private Context getDialogContext(){
        Context context;
        if(getParent() != null)
            context = getParent();
        else
            context = this;

        return context;
    }

}
