package eu.ttbox.nfcproxy.domain;


import android.database.Cursor;
import android.widget.TextView;

import eu.ttbox.nfcproxy.domain.db.NfcProxyDbOpenHelper.CardResponseColumns;

public class CardResponseHelper {


    boolean isNotInit = true;

    public int idIdx = -1;
    public int replayFkIdx = -1;
    public int tagIdx = -1;
    public int pcdIdx = -1;


    // ===========================================================
    // Constructor
    // ===========================================================


    public CardResponseHelper() {
        super();
    }

    public CardResponseHelper(Cursor cursor) {
        super();
        initWrapper(cursor);
    }

    public CardResponseHelper initWrapper(Cursor cursor) {
        idIdx = cursor.getColumnIndex(CardResponseColumns.COL_ID);
        replayFkIdx = cursor.getColumnIndex(CardResponseColumns.COL_REPLAY_FK);
        tagIdx = cursor.getColumnIndex(CardResponseColumns.COL_TAG);
        pcdIdx = cursor.getColumnIndex(CardResponseColumns.COL_PCD);
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

    public long getReplayFk(Cursor cursor) {
        return cursor.getLong(replayFkIdx);
    }

    public String getTagAsString(Cursor cursor) {
        return cursor.getString(tagIdx);
    }

    public String getPcdAsString(Cursor cursor) {
        return cursor.getString(pcdIdx);
    }


    // ===========================================================
    // TextView
    // ===========================================================


    private CardResponseHelper setTextWithIdx(TextView view, Cursor cursor, int idx) {
        view.setText(cursor.getString(idx));
        return this;
    }

    public CardResponseHelper setTextViewWithEntityId(TextView view, Cursor cursor) {
        return setTextWithIdx(view, cursor, idIdx);
    }

    public CardResponseHelper setTextViewWithTag(TextView view, Cursor cursor) {
        return setTextWithIdx(view, cursor, tagIdx);
    }

    public CardResponseHelper setTextViewWithPcd(TextView view, Cursor cursor) {
        return setTextWithIdx(view, cursor, pcdIdx);
    }

    // ===========================================================
    // Other
    // ===========================================================



}
