package com.example.daria.habits;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button add_tel, add_email;
    private LinearLayout llTel, llEmail;
    private List<View> telEds = new ArrayList<>();
    private List<View> emailEds = new ArrayList<>();
    private static final int REQUEST = 1;
    private EditText name, tel, email;
    private SQLController dbcon;
    private int type_id = 0;
    private Long id;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        dbcon = new SQLController(this);
        dbcon.open();

        add_tel = (Button) findViewById(R.id.tel_add);
        add_email = (Button) findViewById(R.id.email_add);
        llTel = (LinearLayout) findViewById(R.id.linearLayoutTel);
        llEmail = (LinearLayout) findViewById(R.id.linearLayoutEmail);
        add_tel.setOnClickListener(this);
        add_email.setOnClickListener(this);
        name = (EditText) findViewById(R.id.editText_name);
        name.requestFocus();
    }

    @Override
    public void onClick(View v) {
        int i = 0;
        switch (v.getId()) {
            case R.id.tel_add:
                i = 1;
                createTelOrEmailView(llTel, i);
                break;
            case R.id.email_add:
                i = 2;
                createTelOrEmailView(llEmail, i);
                break;
        }

    }

    protected void createTelOrEmailView(LinearLayout layout, int i) {

        final View view = getView(i);
        Button deleteField = (Button) view.findViewById(R.id.delete_button);
        final EditText text = (EditText) view.findViewById(R.id.telephone_editText);
        tv = (TextView) view.findViewById(R.id.id_for_tel_or_email);
        layout.addView(view);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioButton9);
        radioButton.setChecked(true);
        type_id = 1;
        tv.setText(String.valueOf(type_id));

        if (i == 1 && getIntent().getIntExtra("permission", 0) == 1) {
            Button call = (Button) view.findViewById(R.id.buttonCall);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + text.getText().toString()));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            });
        }

        if (i == 2) {
            Button writeEmail = (Button) view.findViewById(R.id.buttonMail);
            writeEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{text.getText().toString()});

        /* Отправляем на выбор!*/
                    getApplicationContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton9:
                        type_id = 1;
                        tv.setText(String.valueOf(type_id));
                        break;
                    case R.id.radioButton8:
                        type_id = 2;
                        tv.setText(String.valueOf(type_id));
                        break;
                    case R.id.radioButton7:
                        type_id = 3;
                        tv.setText(String.valueOf(type_id));
                        break;
                }
            }
        });

        deleteField.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                try {
                    ((LinearLayout) view.getParent()).removeView(view);
                    if (telEds.contains(view))
                        telEds.remove(view);
                    else emailEds.remove(view);
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }
        });

        if (i == 1) telEds.add(view);
        if (i == 2) emailEds.add(view);
    }

    protected View getView(int i) {
        switch (i) {
            case 1:
                return getLayoutInflater().inflate(R.layout.add_tel_layout, null);
            case 2:
                return getLayoutInflater().inflate(R.layout.add_email_layout, null);
            default:
                return new View(this);
        }
    }

    protected void save() {

        boolean isExist = false;

        Cursor cur = dbcon.readNames();
        if (cur.moveToFirst()) {

            do {
                String s = cur.getString(cur.getColumnIndex(DbHelper.COL_NAME));
                if (name.getText().toString().equals(s)) {
                    isExist = true;
                    Toast.makeText(this, R.string.already_exists, Toast.LENGTH_LONG).show();
                    break;
                }
            } while (cur.moveToNext());

            if (isExist == false) {
                saveContact();
            }
        } else {
            saveContact();
        }
    }

    protected void saveContact() {
        id = dbcon.insertName(name.getText().toString());

        if (!telEds.isEmpty()) {
            for (View view : telEds) {
                EditText tel = (EditText) view.findViewById(R.id.telephone_editText);
                TextView v = (TextView) view.findViewById(R.id.id_for_tel_or_email);
                long iid = dbcon.insertTel(tel.getText().toString(), id, Integer.parseInt(v.getText().toString()));
            }
        }

        if (!emailEds.isEmpty()) {
            for (View view : emailEds) {
                EditText email = (EditText) view.findViewById(R.id.telephone_editText);
                TextView v = (TextView) view.findViewById(R.id.id_for_tel_or_email);
                long iid = dbcon.insertEmail(email.getText().toString(), id, Integer.parseInt(v.getText().toString()));
            }
        }

        Intent main = new Intent(
                ScrollingActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
    }

    @Override
    protected void onDestroy() {
        dbcon.close();
        super.onDestroy();
    }
}
