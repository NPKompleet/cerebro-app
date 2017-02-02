package com.example.phenomenon.newapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class TwoFragment extends Fragment{

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*

*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

        //final SQLiteDatabase db= getActivity().openOrCreateDatabase("CustomerDB", Context.MODE_PRIVATE, null);

        final String TAG = "MapLocation";

        FloatingActionButton fab1 = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        final EditText ed= (EditText) getActivity().findViewById(R.id.editText31);

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
                    txt += "-01";
                    final String txt2= txt;
                    final String txt3= txt.replace("/","-");
                    Firebase ref= new Firebase("https://mdmcustomer.firebaseio.com").child(txt3);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String location= (String) dataSnapshot.child("location").getValue();

                            if(location != null){
                                String msg = "located " + txt2 + " at " + location;

                                Toast.makeText(getActivity().getBaseContext(), msg,
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
                                Toast.makeText(getActivity().getBaseContext(), "Couldn't find "+txt2+" in database",
                                        Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });




                    /*
                    //Cursor c=db.rawQuery("SELECT * FROM customer WHERE acct='"+ed.getText()+"'", null);
                    Cursor c=db.rawQuery("SELECT * FROM customer WHERE acct='"+txt+"'", null);
                    if(c.moveToFirst())
                    {
                        //editName.setText(c.getString(1));
                        //editMarks.setText(c.getString(2));
                        String location= c.getString(2);
                        String msg = "found " + txt+"-01" + " at " + location;

                        Toast.makeText(getActivity().getBaseContext(), msg,
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

                        Snackbar.make(view, "Unable To Find Customer " + txt+"-01", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }


                    c.close();
                    */




                }else {
                    Snackbar.make(view, "Please enter valid account number format", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

    }

}