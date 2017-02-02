package com.example.phenomenon.newapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //intializing views
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText ed1= (EditText) findViewById(R.id.editText1);
        final EditText ed2= (EditText) findViewById(R.id.editText2);
        final EditText ed3= (EditText) findViewById(R.id.editText3);

        //Button tb = (Button) findViewById(R.id.button2);
        //tb.setOnClickListener();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String location= ed2.getText() + ", " + ed3.getText();
                 String txt= ed1.getText().toString();
                // Inserting record
                //regex to the rescue
                if (!txt.isEmpty() & !location.equals(", ") & txt.matches("\\d\\d/\\d\\d/\\d\\d/\\d\\d\\d\\d(-01)?")){

                    if (txt.length()==16){
                        txt=new StringBuilder(txt).delete(13,16).toString();
                    }
                    final String txt2=txt;
                    //create a dialog box
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setMessage("Save "+ txt+"-01"+ " at location " + location+"?");

                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            db.execSQL("INSERT INTO customer VALUES('" + txt2 + "','NONE','" + location + "');");
                            //showMessage("Success", "Record added");
                            String msg = "inserted " + txt2 + "-01" + " at " + location + " into the database";

                            Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    //alertDialog.setIcon();
                    alertDialog.show();

                }else{
                    String msg= "Please ensure all compulsory parameters are included and in the right format";
                    Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }



            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000, 1, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
/**

        ed1.addTextChangedListener(new TextWatcher() {
            String text = ed1.getText().toString();
            int textlength = ed1.getText().length();
            boolean autoformat = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(text.endsWith("/") || text.endsWith("-")){
                    autoformat= false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //if(text.endsWith(" "))
                    //return;

                if((textlength == 2 || textlength == 5 || textlength == 8) && autoformat)
                {
                    ed1.setText(new StringBuilder(text).insert(text.length(), "/").toString());
                    ed1.setSelection(ed1.getText().length());
                }

                else if(textlength == 13 && autoformat)
                {
                    ed1.setText(new StringBuilder(text).insert(text.length(), "-01").toString());
                    ed1.setSelection(ed1.getText().length());
                }

                else {autoformat=true;}


            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
**/
        db=openOrCreateDatabase("CustomerDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS customer(acct VARCHAR PRIMARY KEY NOT NULL,name VARCHAR,location VARCHAR NOT NULL);");
    }

    @Override
    public void onLocationChanged(Location location) {
        //String msg= location.toString();

        /*String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
        */
       // Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

        EditText ed2= (EditText) findViewById(R.id.editText2);
        EditText ed3= (EditText) findViewById(R.id.editText3);
        ed2.setText("" +location.getLatitude());
        ed3.setText("" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {

        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id== R.id.action_search){
            Intent intent= new Intent(this, InputActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void testClicked(View view){
        if (view.getId()== R.id.button2){
            Intent intent= new Intent(this, Main2Activity.class);
            startActivity(intent);
        }

    }
}
