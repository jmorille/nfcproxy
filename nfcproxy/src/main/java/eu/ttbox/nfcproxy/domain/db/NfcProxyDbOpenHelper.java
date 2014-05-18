package eu.ttbox.nfcproxy.domain.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import eu.ttbox.nfcproxy.domain.core.UpgradeDbHelper;

public class NfcProxyDbOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbOpenHelper";

    public static final String DATABASE_NAME = "nfcProxy.db";
    public static final int DATABASE_VERSION = 1;

    // ===========================================================
    // Columns
    // ===========================================================

    public static class ReplayColumns {

        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_CATEGORY = "CATEGORY";
        public static final String COL_NAME = "NAME";
        public static final String COL_TAG_ID = "TAG_ID";
        public static final String COL_RECORD_TIME = "RECORD_TIME";

        // All Cols
        public static final String[] ALL_COLS = new String[]{ //
                COL_ID, COL_NAME //
                , COL_CATEGORY
                , COL_TAG_ID //
                , COL_RECORD_TIME //
        };

        // Where Clause
        public static final String SELECT_BY_ENTITY_ID = String.format("%s = ?", COL_ID);
        public static final String SELECT_BY_TAG_ID = String.format("%s = ?", COL_TAG_ID);
        public static final String SELECT_BY_CATEGORY = String.format("%s = ?", COL_CATEGORY);
        // Order Clause
        public static final String ORDER_NAME_ASC = String.format("%s ASC", COL_RECORD_TIME);

    }

    public static class CardResponseColumns {

        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_REPLAY_FK = "FK_REPLAY_ID";
        public static final String COL_TAG = "TAG";
        public static final String COL_PCD = "PCD";

        // All Cols
        public static final String[] ALL_COLS = new String[]{ //
                COL_ID, COL_REPLAY_FK //
                , COL_TAG //
                , COL_PCD //
        };
        // Where Clause
        public static final String SELECT_BY_ENTITY_ID = String.format("%s = ?", COL_ID);
        public static final String SELECT_BY_REPLAY_FK = String.format("%s = ?", COL_REPLAY_FK);
        // Order Clause
        public static final String ORDER_NATURAL_ASC = String.format("%s ASC, %s ASC",  COL_REPLAY_FK, COL_ID);

    }

    // ===========================================================
    // Table
    // ===========================================================
    public static final String TABLE_REPLAYS = "REPLAYS";
    public static final String TABLE_CARD_RESPONSE = "TRANSCEIVE";

    private static final String[] TO_DROP_TABLE_NAME = new String[]{ //
            TABLE_CARD_RESPONSE //
            , TABLE_REPLAYS //
    };

    // Doc sqlite : http://www.sqlite.org/foreignkeys.html
    private static final String SQL_CREATE_TABLE_REPLAYS = "CREATE TABLE " + TABLE_REPLAYS //
            + "( " + ReplayColumns.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"//
            + ", " + ReplayColumns.COL_CATEGORY + " INTEGER"//
            + ", " + ReplayColumns.COL_RECORD_TIME + " INTEGER"//
            + ", " + ReplayColumns.COL_TAG_ID + " TEXT" //
            + ", " + ReplayColumns.COL_NAME + " TEXT" //
            + ");";

    private static final String SQL_CREATE_TABLE_CRAD_RESPONSE = "CREATE TABLE " + TABLE_CARD_RESPONSE //
            + "( " + CardResponseColumns.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"//
            + ", " + CardResponseColumns.COL_REPLAY_FK + " INTEGER" //
            + ", " + CardResponseColumns.COL_TAG + " TEXT" //
            + ", " + CardResponseColumns.COL_PCD + " TEXT"//
            + String.format(Locale.US, ", FOREIGN KEY(%s) REFERENCES %s(%s)", CardResponseColumns.COL_REPLAY_FK, TABLE_REPLAYS, ReplayColumns.COL_ID)
            + ");";

    // ===========================================================
    // Index
    // ===========================================================

    private static final String INDEX_REPLAYS_TAG_AK = "IDX_TAG_AK";
    private static final String INDEX_CARD_RESPONSE_TAG_AK = "IDX_TRANSCEIVE_REPLAY_FK";

    private static final String[] TO_DROP_INDEX_NAME = new String[]{ //
            INDEX_CARD_RESPONSE_TAG_AK //
            , INDEX_REPLAYS_TAG_AK //
    };

    private static final String SQL_CREATE_INDEX_REPLAY_TAG_AK = //
            String.format(Locale.US, "CREATE INDEX IF NOT EXISTS %s on %s(%s);" //
                    , INDEX_REPLAYS_TAG_AK, TABLE_REPLAYS, ReplayColumns.COL_TAG_ID);

    private static final String SQL_CREATE_INDEX_TRANSCEIVE_TAG_AK = //
            String.format(Locale.US, "CREATE INDEX IF NOT EXISTS %s on %s(%s);" //
                    , INDEX_CARD_RESPONSE_TAG_AK, TABLE_CARD_RESPONSE, CardResponseColumns.COL_REPLAY_FK);


    // ===========================================================
    // Constructors
    // ===========================================================

    private SQLiteDatabase mDatabase;

    public NfcProxyDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mDatabase = db;
        // Create
        mDatabase.execSQL(SQL_CREATE_TABLE_REPLAYS);
        mDatabase.execSQL(SQL_CREATE_TABLE_CRAD_RESPONSE);
        // Index
        db.execSQL(SQL_CREATE_INDEX_REPLAY_TAG_AK);
        db.execSQL(SQL_CREATE_INDEX_TRANSCEIVE_TAG_AK);

    }

    private void onLocalDrop(SQLiteDatabase db) {
        // Drop  Index
        // ---------------
        for (String toDrop : TO_DROP_INDEX_NAME) {
            db.execSQL("DROP INDEX IF EXISTS " + toDrop);
        }

        // Drop  Tables
        // ---------------
        for (String toDrop : TO_DROP_TABLE_NAME) {
            db.execSQL("DROP TABLE IF EXISTS " + toDrop);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        // Read previous values
        // ----------------------
        ArrayList<ContentValues> oldRows = null;
        oldRows = UpgradeDbHelper.copyTable(db, NfcProxyDbOpenHelper.TABLE_REPLAYS);


        // Create the new Table
        // ----------------------
        onLocalDrop(db);
        onCreate(db);
        Log.i(TAG, "Upgrading database : Create TABLE  : " + NfcProxyDbOpenHelper.TABLE_REPLAYS);

        // Insert data in new table
        // ----------------------
        if (oldRows != null && !oldRows.isEmpty()) {
            List<String> validColumns = Arrays.asList(ReplayColumns.ALL_COLS);
            UpgradeDbHelper.insertOldRowInNewTable(db, oldRows, NfcProxyDbOpenHelper.TABLE_REPLAYS, validColumns);
        }
    }


}
