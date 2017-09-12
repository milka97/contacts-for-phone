package com.example.daria.habits;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SQLController dbcon;
    private Cursor cur;
    private ListView listView;
    private TextView showName, showNameId;
    private FloatingActionButton fab;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private int MY_PERMISSIONS_WAS_GRANTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
                intent.putExtra("permission", 1);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }

        listView = (ListView) findViewById(R.id.main_listview);

        dbcon = new SQLController(this);
        dbcon.open();
        cur = dbcon.readNames();

        setAdapter(cur);
    }


    public void setAdapter(Cursor c) {

        if (c.moveToFirst()) {
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            do {
                String s = c.getString(c.getColumnIndex("name"));
                int i = c.getInt(c.getColumnIndex("_id"));
                HashMap<String, String> m = new HashMap<>();
                m.put("index", String.valueOf(i));
                m.put("name", s);
                list.add(m);
            } while (c.moveToNext());


            String[] from = new String[]{"index", "name"};
            int[] to = new int[]{R.id.lvitem_id, R.id.lvitem_name};

            SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.listview_item, from, to);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    showName = (TextView) view.findViewById(R.id.lvitem_name);
                    showNameId = (TextView) view.findViewById(R.id.lvitem_id);
                    String s = showName.getText().toString();
                    String is = showNameId.getText().toString();
                    Intent show_intent = new Intent(getApplicationContext(), EditScrollingActivity.class);
                    show_intent.putExtra("showName", s);
                    show_intent.putExtra("showNameId", is);
                    show_intent.putExtra("permission", 1);
                    startActivity(show_intent);

                }
            });

        }
    }


    @Override
    protected void onDestroy() {
        dbcon.close();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    MY_PERMISSIONS_WAS_GRANTED = 1;

                } else {
                    MY_PERMISSIONS_WAS_GRANTED = 0;
                }
                return;
            }

        }
    }
}