package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.ContextMenu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity<StableArrayAdapter> extends AppCompatActivity {


    String[] remindersTexts = new String[] {
            "India",
            "Pakistan",
            "Sri Lanka",
            "China",
            "Bangladesh",
            "Nepal",
            "Afghanistan",
            "North Korea",
            "South Korea",
            "Japan"
    };

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] importants = new int[]{
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red
    };

    final Context context = this;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            // Each row in the list stores country name, currency and flag
            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

            for(int i=0;i<10;i++){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("remindertext", remindersTexts[i]);
                hm.put("important", Integer.toString(importants[i]) );
                aList.add(hm);
            }

            // Keys used in Hashmap
            String[] from = { "important","remindertext" };

            // Ids of views in listview_layout
            int[] to = { R.id.important,R.id.remindertext};

              SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.activity_listview, from, to);

            // Getting a reference to listview of main.xml layout file
            ListView listView = ( ListView ) findViewById(R.id.reminders_list);
            listView.setAdapter(adapter);

        registerForContextMenu(listView);

    }



    //Menu of alert dialoge new reminder and exit
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.alertdialoge, null);
                final EditText newreminder = alertLayout.findViewById(R.id.newreminder);
                final CheckBox important = alertLayout.findViewById(R.id.important);

                important.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ImageView img=(ImageView)findViewById(R.id.important);
                            img.setVisibility(View.VISIBLE);
                             }
                    }
                });

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("New Reminder");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.setPositiveButton("Commit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = newreminder.getText().toString();
                        Toast.makeText(getBaseContext(), "Username: " + user, Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                return true;
            case R.id.item2:
                finish();
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //When click on reminder text meu of edit reminder and delete reminder
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem itemm){
        if(itemm.getItemId()==R.id.edit){
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.alertdialoge, null);
            final EditText editreminder = alertLayout.findViewById(R.id.newreminder);
            final CheckBox important = alertLayout.findViewById(R.id.important);



            TextView txt_hello = (TextView) findViewById(R.id.remindertext);
            String reminderr = txt_hello.getText().toString();
            editreminder.setText(reminderr);


            important.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ImageView img=(ImageView)findViewById(R.id.important);
                        img.setVisibility(View.VISIBLE);
                    }
                }
            });

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Edit Reminder");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alert.setPositiveButton("Commit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String user = editreminder.getText().toString();
                    Toast.makeText(getBaseContext(), "Username: " + user, Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
            return true;
        }
        else if(itemm.getItemId()==R.id.delete){
            Toast.makeText(getApplicationContext(),"sending sms code",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }

}

