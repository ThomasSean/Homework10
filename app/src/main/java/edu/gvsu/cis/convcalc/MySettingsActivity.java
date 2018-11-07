package edu.gvsu.cis.convcalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MySettingsActivity extends AppCompatActivity {

    private String fromUnits = "";
    private String toUnits = "";
    private String mode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // retrieve input params
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        fromUnits = intent.getStringExtra("fromUnits");
        toUnits = intent.getStringExtra("toUnits");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("fromUnits", fromUnits);
                intent.putExtra("toUnits", toUnits);
                setResult(MainActivity.SETTINGS_RESULT,intent);
                finish();
            }
        });


        ArrayList<String> vals = new ArrayList<String>();
        int selection = 0;
        if (mode.intern() == "Length") {
            for (UnitsConverter.LengthUnits unit : UnitsConverter.LengthUnits.values()) {
                vals.add(unit.toString());
            }
        } else {
            for (UnitsConverter.VolumeUnits unit : UnitsConverter.VolumeUnits.values()) {
                vals.add(unit.toString());
            }
        }
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, vals);

        Spinner fromSpinner = findViewById(R.id.from_spinner);
        fromSpinner.setAdapter(unitAdapter);

        fromSpinner.setSelection(0);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromUnits = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner toSpinner = findViewById(R.id.to_spinner);
        toSpinner.setAdapter(unitAdapter);
        toSpinner.setSelection(0);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toUnits = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // set initial val of spinners.
        for(int i=0; i<vals.size(); i++) {
            if (fromUnits.intern() == vals.get(i)) {
                fromSpinner.setSelection(i);
            }
            if (toUnits.intern() == vals.get(i)) {
                toSpinner.setSelection(i);
            }
        }
    }

}
