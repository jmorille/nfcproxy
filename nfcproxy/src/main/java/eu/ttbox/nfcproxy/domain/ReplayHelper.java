package eu.ttbox.nfcproxy.domain;


import android.database.Cursor;
import android.widget.TextView;

import eu.ttbox.nfcproxy.domain.db.NfcProxyDbOpenHelper.ReplayColumns;

public class ReplayHelper {


    boolean isNotInit = true;


    public int idIdx = -1;
    public int categoryIdx = -1;
    public int tagIdIdx = -1;
    public int nameIdx = -1;
    public int recordTimeIdx = -1;

    // ===========================================================
    // Constructor
    // ===========================================================


    public ReplayHelper() {
        super();
    }

    public ReplayHelper(Cursor cursor) {
        super();
        initWrapper(cursor);
    }

    public ReplayHelper initWrapper(Cursor cursor) {
        idIdx = cursor.getColumnIndex(ReplayColumns.COL_ID);
        categoryIdx = cursor.getColumnIndex(ReplayColumns.COL_CATEGORY);
        nameIdx = cursor.getColumnIndex(ReplayColumns.COL_NAME);
        tagIdIdx = cursor.getColumnIndex(ReplayColumns.COL_TAG_ID);
        recordTimeIdx = cursor.getColumnIndex(ReplayColumns.COL_RECORD_TIME);
         isNotInit = false;
        return this;
    }


    // ===========================================================
    // Accessor
    // ===========================================================

    public long getEntityId(Cursor cursor) {
        return cursor.getLong(idIdx);
    }

    public String getEntityIdAsString(Cursor cursor) {
        return cursor.getString(idIdx);
    }

    public String getTagId(Cursor cursor) {
        return cursor.getString(tagIdIdx);
    }

    public String getName(Cursor cursor) {
        return cursor.getString(nameIdx);
    }

    public long getRecordTime(Cursor cursor) {
        return cursor.getLong(recordTimeIdx);
    }

    public long getCategory(Cursor cursor) {
        return cursor.getInt(categoryIdx);
    }

    // ===========================================================
    // TextView
    // ===========================================================


    private ReplayHelper setTextWithIdx(TextView view, Cursor cursor, int idx) {
        view.setText(cursor.getString(idx));
        return this;
    }

    public ReplayHelper setTextViewWithEntityId(TextView view, Cursor cursor) {
        return setTextWithIdx(view, cursor, idIdx);
    }

    public ReplayHelper setTextViewWithTagId(TextView view, Cursor cursor) {
        return setTextWithIdx(view, cursor, tagIdIdx);
    }

    public ReplayHelper setTextViewWithNameId(TextView view, Cursor cursor) {
        return setTextWithIdx(view, cursor, nameIdx);
    }

    public ReplayHelper setTextViewWithRecordTime(TextView view, Cursor cursor) {
        return setTextWithIdx(view, cursor, recordTimeIdx);
    }


    // ===========================================================
    // Other
    // ===========================================================




}
