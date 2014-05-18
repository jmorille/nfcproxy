package eu.ttbox.nfcproxy.domain;


import android.app.backup.BackupManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

import eu.ttbox.nfcproxy.NfcProxyApplication;
import eu.ttbox.nfcproxy.domain.db.NfcProxyDbOpenHelper;
import eu.ttbox.nfcproxy.domain.db.NfcProxyDbOpenHelper.ReplayColumns;
import eu.ttbox.nfcproxy.domain.core.DbSelection;


public class ReplayProvider extends ContentProvider {

    private static final String TAG = "ReplayProvider";

    // ===========================================================
    // Constante
    // ===========================================================

    private static final String TABLE_REPLAYS = NfcProxyDbOpenHelper.TABLE_REPLAYS;

    public static final String REPLAY_LIST_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/eu.ttbox.nfcproxy.replay";
    public static final String REPLAY_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/eu.ttbox.nfcproxy.replay";

    public static class Constants {

        public static final String AUTHORITY = "eu.ttbox.nfcproxy.ReplayProvider";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/replay");


        public static final Uri getUriEntityId(long entityId) {
            return getUriEntityId(String.valueOf(entityId));
        }

        public static final Uri getUriEntityId(String entityId) {
            Uri uri = Uri.withAppendedPath(CONTENT_URI, entityId);
            Log.d(TAG, "ReplayProvider Entity Uri : " + uri);
            return uri;
        }

    }
    // ===========================================================
    // Instance
    // ===========================================================

    private NfcProxyDbOpenHelper mDbHelper;

    // UriMatcher stuff
    private static final int REPLAYS = 0;
    private static final int REPLAY_ID = 1;

    private static final UriMatcher sURIMatcher = buildUriMatcher();
    private static final HashMap<String, String> mAliasColumnMap = buildAliasColumnMap();


    // ===========================================================
    // Constructor
    // ===========================================================

    @Override
    public boolean onCreate() {
        NfcProxyApplication app = (NfcProxyApplication)getContext().getApplicationContext();
        mDbHelper = app.getNfcProxyDbOpenHelper();
        return true;
    }


    // ===========================================================
    // Uri Type
    // ===========================================================


    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // to get definitions...
        matcher.addURI(Constants.AUTHORITY, "replay", REPLAYS);
        matcher.addURI(Constants.AUTHORITY, "replay/#", REPLAY_ID);
        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case REPLAYS:
                return REPLAY_LIST_MIME_TYPE;
            case REPLAY_ID:
                return REPLAY_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }


    // ===========================================================
    // Uri selection merge
    // ===========================================================

    private DbSelection computeSelection(Uri uri, String selection, String[] selectionArgs) {
        DbSelection mergeSelection = null;
        switch (sURIMatcher.match(uri)) {
            case REPLAYS:
                mergeSelection = new DbSelection(selection, selectionArgs);
                break;
            case REPLAY_ID:
                // Merge
                String entityId = uri.getLastPathSegment();
                mergeSelection = DbSelection.mergeCriteria(ReplayColumns.SELECT_BY_ENTITY_ID, new String[]{entityId}, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        return mergeSelection;
    }


    // ===========================================================
    // Alias
    // ===========================================================

    private static HashMap<String, String> buildAliasColumnMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        // Add Identity Column
        for (String col : ReplayColumns.ALL_COLS) {
            map.put(col, col);
        }
        // Add Other Aliases
        return map;
    }


    // ===========================================================
    // Query
    // ===========================================================

    @Override
    public Cursor query(Uri uri, String[] _projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query for uri : " + uri);

        // --- Merge Selection
        // --- -------------------
        String[] projection = _projection == null ? ReplayColumns.ALL_COLS : _projection;
        DbSelection mergeSelection = computeSelection(uri, selection, selectionArgs);

        // --- Open Transaction
        // --- -------------------
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_REPLAYS);
        builder.setProjectionMap(mAliasColumnMap);
        Cursor cursor = builder.query(mDbHelper.getReadableDatabase(), projection
                , mergeSelection.selection, mergeSelection.selectionArgs, null, null, sortOrder);
        return cursor;
    }


    // ===========================================================
    // Update
    // ===========================================================

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        // --- Merge Selection
        // --- -------------------
        DbSelection mergeSelection = computeSelection(uri, selection, selectionArgs);

        // --- Open Transaction
        // --- -------------------
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            // Update
            count = db.update(TABLE_REPLAYS, values, mergeSelection.selection, mergeSelection.selectionArgs);
            db.setTransactionSuccessful();
            // Commit
            db.endTransaction();
        } finally {
            db.close();
        }
        // --- Notify Changes
        // --- -------------------
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            // Backup
            BackupManager.dataChanged(getContext().getPackageName());
        }
        return count;
    }


    // ===========================================================
    // Delete
    // ===========================================================

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        // --- Merge Selection
        // --- -------------------
        DbSelection mergeSelection = computeSelection(uri, selection, selectionArgs);
        // --- Open Transaction
        // --- -------------------
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            try {
                count = db.delete(TABLE_REPLAYS, mergeSelection.selection, mergeSelection.selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } finally {
            db.close();
        }

        // --- Notify Changes
        // --- -------------------
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            // Backup
            BackupManager.dataChanged(getContext().getPackageName());
        }
        return count;
    }


    // ===========================================================
    // Insert
    // ===========================================================

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sURIMatcher.match(uri)) {
            case REPLAYS:
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        // --- Open Transaction
        // --- -------------------
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long entityId = Long.MIN_VALUE;
        try {
            // fillNormalizedNumber(values);
            db.beginTransaction();
            try {
                entityId = db.insertOrThrow(TABLE_REPLAYS, null, values);
                // commit
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } finally {
            db.close();
        }
        // --- Notify Changes
        // --- -------------------
        Uri personUri = null;
        if (entityId > -1) {
            personUri = Uri.withAppendedPath(Constants.CONTENT_URI, String.valueOf(entityId));
            getContext().getContentResolver().notifyChange(uri, null);
            // Backup
            BackupManager.dataChanged(getContext().getPackageName());
        }
        return personUri;
    }


    // ===========================================================
    // Other
    // ===========================================================

}
