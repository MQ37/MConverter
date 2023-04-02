package cz.ujep.ki.mconverter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

public class CurrencyLoaderManager implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private CurrencyRecyclerViewAdapter adapter;

    public CurrencyLoaderManager(Context context, CurrencyRecyclerViewAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri = Uri.parse(CurrencyContentProvider.CONTENT_URI);
        String[] projection = new String[] {
                CurrencyContentProvider._ID,
                CurrencyContentProvider.CODE,
                CurrencyContentProvider.NAME,
                CurrencyContentProvider.AMOUNT,
                CurrencyContentProvider.RATE,
                CurrencyContentProvider.COUNTRY
        };
        String sortOrder = CurrencyContentProvider.CODE + " ASC";
        return new CursorLoader(context, uri, projection, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}

