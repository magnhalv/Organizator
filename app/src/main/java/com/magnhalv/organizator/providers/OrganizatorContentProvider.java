package com.magnhalv.organizator.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.GetChars;

/**
 * Created by nomaghal on 31.08.2017.
 */

public class OrganizatorContentProvider extends ContentProvider {

    private OrganizatorDatabase mDatabaseHelper;

    private static final String AUTHORITY = OrganizatorContract.CONTENT_AUTHORITY;

    /**
     * URI IDs
     */

    /** URI ID for route: /tasks **/
    public static final int ROUTE_TASKS = 1;
    /** URI ID for route: /tasks/{ID} **/
    public static final int ROUTE_TASKS_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "tasks", ROUTE_TASKS);
        sUriMatcher.addURI(AUTHORITY, "tasks/*", ROUTE_TASKS_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new OrganizatorDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_TASKS:
                return OrganizatorContract.Task.CONTENT_TYPE;
            case ROUTE_TASKS_ID:
                return OrganizatorContract.Task.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
    *  Supports:
     *    - /tasks
     *    - /tasks/{ID}
     **/
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_TASKS:
                return db.query(OrganizatorContract.Task.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            case ROUTE_TASKS_ID:
                String task_id = uri.getLastPathSegment();
                String lSelection = OrganizatorContract.Task._ID + "=?";
                String[] lSelectionArgs = new String[]{ task_id };

                Cursor c = db.query(
                        OrganizatorContract.Task.TABLE_NAME,
                        projection,
                        lSelection,
                        lSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                Context ctx = getContext();
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_TASKS:
                long id = db.insert(OrganizatorContract.Task.TABLE_NAME, null, contentValues);
                result = Uri.parse(OrganizatorContract.Task.CONTENT_URI + "/" + id);
                break;
            case ROUTE_TASKS_ID:
                throw new UnsupportedOperationException("Insert is not supported on uri: " + uri);
            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }
        Context ctx = getContext();
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    /**
     * SQLite backend for @{link FeedProvider}.
     *
     * Provides access to an disk-backed, SQLite datastore which is utilized by FeedProvider. This
     * database should never be accessed by other parts of the application directly.
     */
    static class OrganizatorDatabase extends SQLiteOpenHelper {
        /** Schema version. */
        public static final int DATABASE_VERSION = 1;
        /** Filename for SQLite file. */
        public static final String DATABASE_NAME = "organizator.db";

        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INTEGER = " INTEGER";
        private static final String COMMA_SEP = ",";

        /** SQL statement to create "entry" table. */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + OrganizatorContract.Task.TABLE_NAME + " (" +
                        OrganizatorContract.Task._ID + " INTEGER PRIMARY KEY," +
                        OrganizatorContract.Task.COLUMN_NAME_TITLE    + TYPE_TEXT + COMMA_SEP +
                        OrganizatorContract.Task.COLUMN_NAME_DESCRIPTION + TYPE_INTEGER + ")";

        /** SQL statement to drop "entry" table. */
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + OrganizatorContract.Task.TABLE_NAME;

        public OrganizatorDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
}
