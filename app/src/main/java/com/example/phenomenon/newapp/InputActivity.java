package com.example.phenomenon.newapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

public class InputActivity extends AppCompatActivity {
    private  SQLiteDatabase db;
    private final String TAG = "MapLocation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        final EditText ed= (EditText) findViewById(R.id.editText3);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting record
                String txt = ed.getText().toString();
                //some regex to save d day
                //if account number is valid
                if (txt.matches("\\d\\d/\\d\\d/\\d\\d/\\d\\d\\d\\d(-01)?")){
                    if (txt.length()==16){
                        txt=new StringBuilder(txt).delete(13,16).toString();
                    }
                    //Cursor c=db.rawQuery("SELECT * FROM customer WHERE acct='"+ed.getText()+"'", null);
                    Cursor c=db.rawQuery("SELECT * FROM customer WHERE acct='"+txt+"'", null);
                    if(c.moveToFirst())
                    {
                        //editName.setText(c.getString(1));
                        //editMarks.setText(c.getString(2));
                        String location= c.getString(2);
                        String msg = "found " + txt+"-01" + " at " + location;

                        Toast.makeText(getBaseContext(), msg,
                                Toast.LENGTH_SHORT).show();
                        //Google Maps portion
                        try {

                            // Process text for network transmission

                            // Create Intent object for starting Google Maps application
                            Intent geoIntent = new Intent(
                                    android.content.Intent.ACTION_VIEW, Uri
                                    .parse("geo:0,0?q=loc:" + location));

                            // Use the Intent to start Google Maps application using Activity.startActivity()
                            startActivity(geoIntent);

                        } catch (Exception e) {
                            // Log any error messages to LogCat using Log.e()
                            Log.e(TAG, e.toString());
                        }
                    }else{

                        Snackbar.make(view, "Unable To Find Customer " + txt, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }


                    c.close();


                }else {
                    Snackbar.make(view, "Please enter valid account number format", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

        db=openOrCreateDatabase("CustomerDB", Context.MODE_PRIVATE, null);

    }

    public void editClicked(View view){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
