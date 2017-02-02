package com.example.phenomenon.newapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.firebase.client.Firebase;

public class OneFragment extends Fragment implements LocationListener {

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        LocationManager locationManager;
        //final SQLiteDatabase db=getActivity().openOrCreateDatabase("CustomerDB", Context.MODE_PRIVATE, null);
        final Firebase ref= new Firebase("https://mdmcustomer.firebaseio.com");

        //db.execSQL("CREATE TABLE IF NOT EXISTS customer(acct VARCHAR PRIMARY KEY NOT NULL,name VARCHAR,location VARCHAR NOT NULL);");

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        final EditText ed= (EditText) getActivity().findViewById(R.id.editText);
        final EditText ed1= (EditText) getActivity().findViewById(R.id.editText1);
        final EditText ed2= (EditText) getActivity().findViewById(R.id.editText2);
        final EditText ed3= (EditText) getActivity().findViewById(R.id.editText3);

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
                    final String txt2=txt.replace("/", "-")+ "-01";
                    //create a dialog box
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Save "+ txt+"-01"+ " at location " + location+"?");

                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            //db.execSQL("INSERT INTO customer VALUES('" + txt2 + "','NONE','" + location + "');");

                            //txt2.replace("/", "-");
                            String name= ed.getText().toString();
                            if (name.equals("")) name= "Unknown";
                            Customer cus= new Customer(txt2, name, location);

                            //ref.child(txt2).child("name").setValue(name);
                            //ref.child(txt2).child("location").setValue(location);
                            ref.child(txt2).setValue(cus);

                            //showMessage("Success", "Record added");
                            String msg = "inserted " + txt2 + "-01" + " at " + location + " into the database";

                            Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            ed.setText("");
                            ed1.setText("");
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           // this.finish();
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

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
    }



    public void onLocationChanged(Location location) {
        //String msg= location.toString();

        /*String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
        */
        // Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

        EditText ed2= (EditText) getActivity().findViewById(R.id.editText2);
        EditText ed3= (EditText) getActivity().findViewById(R.id.editText3);
        ed2.setText("" +location.getLatitude());
        ed3.setText("" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {

        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //startActivity(intent);
        Toast.makeText(getActivity().getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getActivity().getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


}