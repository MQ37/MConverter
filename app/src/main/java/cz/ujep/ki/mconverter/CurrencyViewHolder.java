package cz.ujep.ki.mconverter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Locale;

public class CurrencyViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;
    private TextView codeTextView;
    private TextView amountTextView;
    private TextView rateTextView;
    private ImageView flagView;


    public CurrencyViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.itemName);
        codeTextView = itemView.findViewById(R.id.itemCode);
        amountTextView = itemView.findViewById(R.id.itemAmount);
        rateTextView = itemView.findViewById(R.id.itemRate);
        flagView = itemView.findViewById(R.id.itemFlag);


    }

    public void bind(String name, String code, double amount, double rate, int flagId) {
        nameTextView.setText(name);
        codeTextView.setText(code);
        amountTextView.setText(String.format(Locale.getDefault(), "%.2f", amount));
        rateTextView.setText(String.format(Locale.getDefault(), "%.2f", rate));
        flagView.setImageResource(flagId);
    }
}
