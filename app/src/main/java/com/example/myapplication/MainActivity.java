package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

public class MainActivity extends AppCompatActivity {


    private RemindersDbAdapter dbAdapter;
    private RemindersSimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setLogo(R.drawable.icon);


        String[] from = {RemindersDbAdapter.COL_IMPORTANT, RemindersDbAdapter.COL_CONTENT};
        int[] to = {R.id.importantSign, R.id.remindertext};

        dbAdapter = new RemindersDbAdapter(getBaseContext());
        dbAdapter.open();
        Cursor cursor = dbAdapter.fetchAllReminders();
        cursorAdapter = new RemindersSimpleCursorAdapter(
                getBaseContext(),
                R.layout.activity_listview,
                cursor,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        ListView listView = findViewById(R.id.reminders_list);
        listView.setAdapter(cursorAdapter);

        registerForContextMenu(listView);
    }

    //Menu of alert dialog new reminder and exit
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newReminderButton:
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.alertdialoge, null);

                alertLayout.setBackgroundColor(Color.parseColor("#003300"));

                final EditText newReminder = alertLayout.findViewById(R.id.editReminder);
                final CheckBox important = alertLayout.findViewById(R.id.importantCheckBox);
                important.setBackgroundColor(Color.parseColor("#000000"));
                final TextView title = alertLayout.findViewById(R.id.dialogetitle);
                title.setText("New Reminder");
                title.setTextSize(23);

                AlertDialog.Builder alert = new AlertDialog.Builder(this,R.style.MyDialogTheme1);
                alert.setView(alertLayout);
                alert.setCancelable(false);

                alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.setPositiveButton("Commit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reminderText = newReminder.getText().toString();
                        if (reminderText.isEmpty())
                        {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Please enter some text",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        dbAdapter.createReminder(
                                reminderText,
                                important.isChecked()
                        );
                        cursorAdapter.changeCursor(dbAdapter.fetchAllReminders());
                    }
                });

                 AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#FFFFFF"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"));
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.parseColor("#D3D3D3"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#D3D3D3"));

                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
                return true;


            case R.id.ExitButton:
                finish();
                System.exit(0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //When click on reminder text menu of edit reminder and delete reminder
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int reminderID = cursorAdapter.getCursor().getInt(RemindersDbAdapter.INDEX_ID);

        if (item.getItemId() == R.id.editButton) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.alertdialoge, null);
            alertLayout.setBackgroundColor(Color.parseColor("#0000ff"));

            final EditText editReminder = alertLayout.findViewById(R.id.editReminder);
            final CheckBox important = alertLayout.findViewById(R.id.importantCheckBox);
            important.setBackgroundColor(Color.parseColor("#000000"));
            final TextView title = alertLayout.findViewById(R.id.dialogetitle);
            title.setText("Edit Reminder");
            title.setTextSize(23);
            final Reminder reminder = dbAdapter.fetchReminderById(reminderID);
            editReminder.setText(reminder.getContent());


            AlertDialog.Builder alert = new AlertDialog.Builder(this,R.style.MyDialogTheme2);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.setPositiveButton("Commit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String reminderText = editReminder.getText().toString();
                    if (reminderText.isEmpty()) {
                        Toast.makeText(
                                getApplicationContext(),
                                "Please enter some text",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }
                    reminder.setContent(reminderText);
                    reminder.setImportant(important.isChecked());
                    dbAdapter.updateReminder(reminder);
                    cursorAdapter.changeCursor(dbAdapter.fetchAllReminders());
                }
            });


            AlertDialog dialog = alert.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#FFFFFF"));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"));

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.parseColor("#D3D3D3"));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#D3D3D3"));

            Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
            layoutParams.weight = 10;
            btnPositive.setLayoutParams(layoutParams);
            btnNegative.setLayoutParams(layoutParams);

            return true;
        } else if (item.getItemId() == R.id.deleteButton) {
            dbAdapter.deleteReminderById(reminderID);
            cursorAdapter.changeCursor(dbAdapter.fetchAllReminders());
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbAdapter.close();
    }



}

