package edu.gvsu.cis.convcalc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTime;

import edu.gvsu.cis.convcalc.UnitsConverter.LengthUnits;
import edu.gvsu.cis.convcalc.UnitsConverter.VolumeUnits;
import edu.gvsu.cis.convcalc.dummy.HistoryContent;

public class MainActivity extends AppCompatActivity {

    public static int SETTINGS_RESULT = 1;
    public static int HISTORY_RESULT = 2;

    private enum Mode {Length, Volume};

    private Mode mode = Mode.Length;
    private Button calcButton;
    private Button clearButton;
    private Button modeButton;

    private EditText toField;
    private EditText fromField;


    private TextView toUnits;
    private TextView fromUnits;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcButton = findViewById(R.id.calcButton);
        clearButton = findViewById(R.id.clearButton);
        modeButton = findViewById(R.id.modeButton);

        toField = findViewById(R.id.to);
        fromField = findViewById(R.id.from);

        fromUnits = findViewById(R.id.fromUnits);
        toUnits = findViewById(R.id.toUnits);

        title = findViewById(R.id.title);

        calcButton.setOnClickListener(v -> {
            doConversion();
        });

        clearButton.setOnClickListener(v -> {
            toField.setText("");
            fromField.setText("");
            hideKeyboard();
        });

        modeButton.setOnClickListener(v -> {
            toField.setText("");
            fromField.setText("");
            hideKeyboard();
            switch(mode) {
                case Length:
                    mode = Mode.Volume;
                    fromUnits.setText(VolumeUnits.Gallons.toString());
                    toUnits.setText(VolumeUnits.Liters.toString());
                    break;
                case Volume:
                    mode = Mode.Length;
                    fromUnits.setText(LengthUnits.Yards.toString());
                    toUnits.setText(LengthUnits.Meters.toString());
                    break;
            }
            title.setText(mode.toString() + " Converter");
        });

        toField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fromField.getText().clear();
            }
        });

        fromField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                toField.getText().clear();
            }
        });

//
    }

    private void doConversion() {
        EditText dest = null;
        String val = "";
        String fromVal = fromField.getText().toString();
        if (fromVal.intern() != "" ) {
            val = fromVal;
            dest = toField;
        }
        String toVal = toField.getText().toString();
        if (toVal.intern() != "") {
            val = toVal;
            dest = fromField;
        }

        if (dest != null) {
            switch(mode) {
                case Length:
                    LengthUnits tUnits, fUnits;
                    if(dest == toField) {
                        fUnits = LengthUnits.valueOf(fromUnits.getText().toString());
                        tUnits = LengthUnits.valueOf(toUnits.getText().toString());
                    } else {
                        fUnits = LengthUnits.valueOf(toUnits.getText().toString());
                        tUnits = LengthUnits.valueOf(fromUnits.getText().toString());
                    }
                    Double dVal = Double.parseDouble(val);
                    Double cVal = UnitsConverter.convert(dVal, fUnits, tUnits);
                    dest.setText(Double.toString(cVal));
                    HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(dVal, cVal, mode.toString(),
                            fUnits.toString(), tUnits.toString(), DateTime.now());
                    HistoryContent.addItem(item);
                    break;
                case Volume:
                    VolumeUnits vtUnits, vfUnits;
                    if(dest == toField) {
                        vfUnits = VolumeUnits.valueOf(fromUnits.getText().toString());
                        vtUnits = VolumeUnits.valueOf(toUnits.getText().toString());
                    } else {
                        vfUnits = VolumeUnits.valueOf(toUnits.getText().toString());
                        vtUnits = VolumeUnits.valueOf(fromUnits.getText().toString());
                    }
                    Double vdVal = Double.parseDouble(val);
                    Double vcVal = UnitsConverter.convert(vdVal, vfUnits, vtUnits);
                    dest.setText(Double.toString(vcVal));
                    HistoryContent.HistoryItem item2 = new HistoryContent.HistoryItem(vdVal, vcVal, mode.toString(),
                            vfUnits.toString(), vtUnits.toString(), DateTime.now());
                    HistoryContent.addItem(item2);
                    break;
            }
        }
        hideKeyboard();

    }

    private void hideKeyboard()
    {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            //this.getSystemService(Context.INPUT_METHOD_SERVICE);
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, MySettingsActivity.class);
            intent.putExtra("mode", mode.toString());
            intent.putExtra("fromUnits", fromUnits.getText().toString());
            intent.putExtra("toUnits", toUnits.getText().toString());
            startActivityForResult(intent, SETTINGS_RESULT );
            return true;
        }  else if(item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SETTINGS_RESULT) {
            this.fromUnits.setText(data.getStringExtra("fromUnits"));
            this.toUnits.setText(data.getStringExtra("toUnits"));
        } else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");
            this.fromField.setText(vals[0]);
            this.toField.setText(vals[1]);
            this.mode = Mode.valueOf(vals[2]);
            this.fromUnits.setText(vals[3]);
            this.toUnits.setText(vals[4]);
            this.title.setText(mode.toString() + " Converter");
        }

    }

}
