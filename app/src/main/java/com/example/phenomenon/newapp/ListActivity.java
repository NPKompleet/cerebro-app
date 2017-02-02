package com.example.phenomenon.newapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

public class ListActivity extends AppCompatActivity {
    FirebaseListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String TAG = "MapLocation";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Firebase.setAndroidContext(this);
        Firebase ref= new Firebase("https://mdmcustomer.firebaseio.com");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot msgSnapshot : snapshot.getChildren()) {
                    Customer cus = msgSnapshot.getValue(Customer.class);
                    Log.i("Customer", cus.getAcct() + ": " + cus.getLocation());

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Chat", "The read failed: " + firebaseError.getMessage());
            }
        });

        ListView cusView = (ListView) findViewById(R.id.customer_listView);

        mAdapter = new FirebaseListAdapter<Customer>(this, Customer.class, R.layout.customer_list, ref) {
            @Override
            protected void populateView(View view, Customer cus) {
                ((TextView)view.findViewById(R.id.customer_acct)).setText(cus.getAcct());
                ((TextView)view.findViewById(R.id.customer_name)).setText(cus.getName());
                ((TextView)view.findViewById(R.id.customer_location)).setText(cus.getLocation());

            }
        };
        cusView.setAdapter(mAdapter);
        //cusView.setAdapter();


        cusView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //Get elected customer
            Customer cus= (Customer) mAdapter.getItem(position);
               if (cus != null){
                   //String location= cus.getLocation();
                   //String location= mAdapter.getRef(position).getKey();
                   String msg = "located " + cus.getName() + " at " + cus.getLocation();

                   Toast.makeText(getBaseContext(), msg,
                           Toast.LENGTH_SHORT).show();
                   //Google Maps portion
                   try {

                       // Process text for network transmission

                       // Create Intent object for starting Google Maps application
                       Intent geoIntent = new Intent(
                               android.content.Intent.ACTION_VIEW, Uri
                               .parse("geo:0,0?q=loc:" + cus.getLocation()));

                       // Use the Intent to start Google Maps application using Activity.startActivity()
                       startActivity(geoIntent);

                   } catch (Exception e) {
                       // Log any error messages to LogCat using Log.e()
                       Log.e(TAG, e.toString());
                   }
               }
           }
        });


/*
        final EditText ed1= (EditText) findViewById(R.id.toolsearch);
        ed1.addTextChangedListener(new TextWatcher() {
            String text = ed1.getText().toString();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text = ed1.getText().toString();
                if (text.matches("\\d\\d/\\d\\d/\\d\\d/\\d\\d\\d\\d(-01)?")) {
                    Toast.makeText(getBaseContext(), "text change",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                text = ed1.getText().toString();
                if (text.matches("\\d\\d/\\d\\d/\\d\\d/\\d\\d\\d\\d(-01)?")){

                    search(text.replace("/","-"));
                    Toast.makeText(getBaseContext(), text,
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void search(String t){

        Firebase ref= new Firebase("https://mdmcustomer.firebaseio.com").child(t);
        if (ref != null) {
            Toast.makeText(getBaseContext(), "not null",
                    Toast.LENGTH_SHORT).show();

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Customer cus= snapshot.getValue(Customer.class);
                    Log.i("Customer", cus.getAcct() + ": " + cus.getLocation());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("Chat", "The read failed: " + firebaseError.getMessage());
                }
            });
        }
        else{Toast.makeText(getBaseContext(), "hmmmmmm",
                Toast.LENGTH_SHORT).show();}

        ListView cusView = (ListView) findViewById(R.id.customer_listView);
        cusView.removeAllViews();

        mAdapter = new FirebaseListAdapter<Customer>(this, Customer.class, R.layout.customer_list, ref) {
            @Override
            protected void populateView(View view, Customer cus) {
                ((TextView)view.findViewById(R.id.customer_acct)).setText(cus.getAcct());
                ((TextView)view.findViewById(R.id.customer_name)).setText(cus.getName());
                ((TextView)view.findViewById(R.id.customer_location)).setText(cus.getLocation());

            }
        };
        cusView.setAdapter(mAdapter);
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
               finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

}
