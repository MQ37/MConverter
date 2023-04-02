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

    public CurrencyRecyclerViewAdapter(Cursor cursor, AppCompatActivity parent) {
        this.mc = cursor;
        this.parent = parent;
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
        String countryCode = CurrencyCountryMap.currencyToCountryMap.get(code);

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

