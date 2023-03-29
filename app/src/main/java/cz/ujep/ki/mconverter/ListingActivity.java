package cz.ujep.ki.mconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Cursor mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mc = getDataFromProvider();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.item,
                mc,
                new String[]{ CurrencyContentProvider.CODE,
                        CurrencyContentProvider.NAME,
                        CurrencyContentProvider.AMOUNT,
                        CurrencyContentProvider.RATE},
                new int[]{R.id.itemCode,R.id.itemName,R.id.itemAmount, R.id.itemRate});
        ListView v = (ListView)findViewById(R.id.currencyView);
        v.setAdapter(adapter);
        v.setOnItemClickListener(this);
    }

    @SuppressLint("Range")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mc.moveToPosition(position);
        Bundle bundle = new Bundle();
        bundle.putString("CODE",
                mc.getString(mc.getColumnIndex(CurrencyContentProvider.CODE)));
        bundle.putString("NAME",
                mc.getString(mc.getColumnIndex(CurrencyContentProvider.NAME)));
        bundle.putDouble("AMOUNT",
                mc.getDouble(mc.getColumnIndex(CurrencyContentProvider.AMOUNT)));
        bundle.putDouble("RATE",
                mc.getDouble(mc.getColumnIndex(CurrencyContentProvider.RATE)));
        bundle.putString("COUNTRY",
                mc.getString(mc.getColumnIndex(CurrencyContentProvider.COUNTRY)));
        Intent intent = new Intent(this, CalculatorActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Cursor getDataFromProvider() {
        Uri uri = Uri.parse(CurrencyContentProvider.CONTENT_URI);
        String[] projection = new String[] {
                CurrencyContentProvider._ID,
                CurrencyContentProvider.CODE,
                CurrencyContentProvider.NAME,
                CurrencyContentProvider.AMOUNT,
                CurrencyContentProvider.RATE,
                CurrencyContentProvider.COUNTRY
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = CurrencyContentProvider.CODE + " ASC";
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_update:
                Intent intent = new Intent(this, UpdateService.class);
                startService(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}