package cz.ujep.ki.mconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

//public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
public class MainActivity extends AppCompatActivity {

    private static final int CURRENCY_LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.currencyView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        CurrencyRecyclerViewAdapter adapter = new CurrencyRecyclerViewAdapter(null, this);
        rv.setAdapter(adapter);

        CurrencyLoaderManager clm = new CurrencyLoaderManager(this, adapter);
        LoaderManager.getInstance(this).initLoader(CURRENCY_LOADER_ID, null, clm);
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