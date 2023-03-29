package cz.ujep.ki.mconverter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CurrencyContentProvider extends ContentProvider {

    private final static String DB_NAME = "currencies.db";
    private final static int DB_VERSION = 1;
    private final static String TABLE_NAME = "currencies";
    public final static String AUTHORITY
            = "cz.ujep.ki.mconverter.providers.currencies";
    public final static String CONTENT_URI
            = "content://" + AUTHORITY + "/" + TABLE_NAME;
    public final static String CONTENT_TYPE
            = "vnd.android.cursor.dir/vnd.cz.ujep.ki.android.fiser.currencies";

    public final static String _ID = "_id";
    public final static String CODE = "code";
    public final static String NAME = "name";
    public final static String AMOUNT = "amount";
    public final static String RATE = "rate";
    public final static String COUNTRY = "country";

    private static final int CURRENCY_MATCH = 1;

    private DatabaseHelper dbHelper;
    private UriMatcher matcher;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, TABLE_NAME, CURRENCY_MATCH);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (matcher.match(uri)) {
            case CURRENCY_MATCH:
                qb.setTables(TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(matcher.match(uri)) {
            case CURRENCY_MATCH:
                return CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (matcher.match(uri)) {
            case CURRENCY_MATCH:
                id = db.insert(TABLE_NAME, CODE, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(uri, null);
            return itemUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (matcher.match(uri)) {
            case CURRENCY_MATCH:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (matcher.match(uri)) {
            case CURRENCY_MATCH:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String command =
                    "CREATE TABLE " + TABLE_NAME + " (" +
                            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            CODE + " VARCHAR(3), " +
                            NAME + " VARCHAR(32), " +
                            COUNTRY + " VARCHAR(32), " +
                            AMOUNT + " REAL, " +
                            RATE + " REAL " + ");";
            db.execSQL(command);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
