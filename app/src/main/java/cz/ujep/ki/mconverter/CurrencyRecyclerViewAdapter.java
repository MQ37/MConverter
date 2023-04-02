package cz.ujep.ki.mconverter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class CurrencyRecyclerViewAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {
    private Cursor mc;
    private AppCompatActivity parent;
    private HashMap<String, String> currencyToCountryMap;

    public CurrencyRecyclerViewAdapter(Cursor cursor, AppCompatActivity parent) {
        this.mc = cursor;
        this.parent = parent;
        currencyToCountryMap = new HashMap<>();
        currencyToCountryMap.put("AUD", "AU");
        currencyToCountryMap.put("BRL", "BR");
        currencyToCountryMap.put("BGN", "BG");
        currencyToCountryMap.put("CNY", "CN");
        currencyToCountryMap.put("DKK", "DK");
        currencyToCountryMap.put("EUR", "EU");
        currencyToCountryMap.put("PHP", "PH");
        currencyToCountryMap.put("HKD", "HK");
        currencyToCountryMap.put("INR", "IN");
        currencyToCountryMap.put("IDR", "ID");
        currencyToCountryMap.put("ISK", "IS");
        currencyToCountryMap.put("ILS", "IL");
        currencyToCountryMap.put("JPY", "JP");
        currencyToCountryMap.put("ZAR", "ZA");
        currencyToCountryMap.put("CAD", "CA");
        currencyToCountryMap.put("KRW", "KR");
        currencyToCountryMap.put("HUF", "HU");
        currencyToCountryMap.put("MYR", "MY");
        currencyToCountryMap.put("MXN", "MX");
        //currencyToCountryMap.put("XDR", "MMF");
        currencyToCountryMap.put("NOK", "NO");
        currencyToCountryMap.put("NZD", "NZ");
        currencyToCountryMap.put("PLN", "PL");
        currencyToCountryMap.put("RON", "RO");
        currencyToCountryMap.put("SGD", "SG");
        currencyToCountryMap.put("SEK", "SE");
        currencyToCountryMap.put("CHF", "CH");
        currencyToCountryMap.put("THB", "TH");
        currencyToCountryMap.put("TRY", "TR");
        currencyToCountryMap.put("USD", "US");
        currencyToCountryMap.put("GBP", "GB");
        currencyToCountryMap.put("CZK", "CZ");
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item_layout, parent, false);
        return new CurrencyViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        mc.moveToPosition(position);

        String name = mc.getString(mc.getColumnIndex(CurrencyContentProvider.NAME));
        String code = mc.getString(mc.getColumnIndex(CurrencyContentProvider.CODE));
        String country = mc.getString(mc.getColumnIndex(CurrencyContentProvider.COUNTRY));
        double amount = mc.getDouble(mc.getColumnIndex(CurrencyContentProvider.AMOUNT));
        double rate = mc.getDouble(mc.getColumnIndex(CurrencyContentProvider.RATE));

        Resources resources = parent.getResources();
        String countryCode = currencyToCountryMap.get(code);
        int flagId = 0;
        if (countryCode != null) {
            flagId = resources.getIdentifier(countryCode.toLowerCase(), "drawable", parent.getPackageName());
        }

        if (flagId == 0)
            flagId = R.drawable.ic_launcher_foreground;

        holder.bind(name, code, amount, rate, flagId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "click");
                Bundle bundle = new Bundle();
                bundle.putString("CODE", code);
                bundle.putString("NAME", name);
                bundle.putDouble("AMOUNT", amount);
                bundle.putDouble("RATE", rate);
                bundle.putString("COUNTRY", country);
                Intent intent = new Intent(parent, CalculatorActivity.class);
                intent.putExtras(bundle);
                parent.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        if (mc != null)
            return mc.getCount();
        return -1;
    }

    public void swapCursor(Cursor cursor) {
        this.mc = cursor;
    }
}

