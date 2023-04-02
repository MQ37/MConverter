package cz.ujep.ki.mconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.NumberFormat;
import android.media.Image;
import android.net.ParseException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public class CalculatorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText fcAmount;
    EditText homeAmount;
    TextView fcCode;
    Spinner tax;
    TextView fullName;
    ImageView flagView;
    private double rate;
    private double taxrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        fcAmount = (EditText)findViewById(R.id.fcAmount);
        homeAmount = (EditText)findViewById(R.id.homeAmount);
        fcCode = (TextView)findViewById(R.id.fcCode);
        tax = (Spinner)findViewById(R.id.tax);
        fullName = (TextView)findViewById(R.id.fullName);
        flagView = (ImageView)findViewById(R.id.flag);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        // Get flag
        Resources resources = getResources();
        String countryCode = CurrencyCountryMap.currencyToCountryMap.get(b.getString("CODE"));
        int flagId = 0;
        if (countryCode != null) {
            flagId = resources.getIdentifier(countryCode.toLowerCase(), "drawable", getPackageName());
        }
        if (flagId == 0)
            flagId = R.drawable.ic_launcher_foreground;
        flagView.setImageResource(flagId);

        fullName.setText(b.getString("NAME") + " (" + b.getString("COUNTRY") + ")");
        fcCode.setText(b.getString("CODE") + ": ");
        fcAmount.setText("1");

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.taxes,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tax.setAdapter(adapter);
        tax.setOnItemSelectedListener(this);

        rate = b.getDouble("RATE") / b.getDouble("AMOUNT");
        taxrate = rate * (1.0 + parseTax((String)(tax.getSelectedItem())));

        refresh(fcAmount.getText().toString(), homeAmount, taxrate);

        fcAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(fcAmount.hasFocus()) {
                    refresh(s.toString(), homeAmount, taxrate);
                }
            }});

        homeAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(homeAmount.hasFocus())
                    refresh(s.toString(), fcAmount, 1.0/taxrate);
            }});
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        taxrate = rate * (1.0 + parseTax((String)parent.getItemAtPosition(position)));
        if(fcAmount.hasFocus())
            refresh(fcAmount.getText().toString(), homeAmount, taxrate);
        if(homeAmount.hasFocus())
            refresh(homeAmount.getText().toString(), fcAmount, 1.0 / taxrate);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static double parseTax(String s) {
        return Double.parseDouble(s.substring(0, s.length() - 1)) / 100.0;
    }

    private static void refresh(String amount, EditText target, double rate) {
        double origAmount;
        if (amount.equals(""))
            origAmount = 0.0;
        else {
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number;
            try {
                number = format.parse(amount);
                origAmount = number.doubleValue();
            } catch (ParseException | java.text.ParseException e) {origAmount = Double.parseDouble(amount);}
        }
        double targetAmount = origAmount * rate;
        target.setText(String.format("%.2f", targetAmount));
    }
}