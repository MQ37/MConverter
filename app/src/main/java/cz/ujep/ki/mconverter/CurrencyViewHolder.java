package cz.ujep.ki.mconverter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class CurrencyViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;
    private TextView codeTextView;
    private TextView amountTextView;
    private TextView rateTextView;

    public CurrencyViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.itemName);
        codeTextView = itemView.findViewById(R.id.itemCode);
        amountTextView = itemView.findViewById(R.id.itemAmount);
        rateTextView = itemView.findViewById(R.id.itemRate);
    }

    public void bind(String name, String code, double amount, double rate) {
        nameTextView.setText(name);
        codeTextView.setText(code);
        amountTextView.setText(String.format(Locale.getDefault(), "%.2f", amount));
        rateTextView.setText(String.format(Locale.getDefault(), "%.2f", rate));
    }
}
