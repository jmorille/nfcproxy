package eu.ttbox.nfcproxy.domain.core;

import android.text.TextUtils;


public class DbSelection {

    public final String selection;
    public final String[] selectionArgs;

    public DbSelection(String selection, String[] selectionArgs) {
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }


    public static DbSelection mergeCriteria(String oneSelection, String[] oneSelectionArgs
            , String otherSelection, String[] otherSlectionArgs) {
        String selection = oneSelection;
        String[] selectionArgs = oneSelectionArgs;
        if (!TextUtils.isEmpty(otherSelection)) {
            // Merge Selection
            if (TextUtils.isEmpty(oneSelection)) {
                selection = otherSelection;
            } else {
                selection = String.format("(%s) and (%s)", oneSelection, otherSelection);
            }
            // Merge Selection Args
            int oneSelectionArgSize = oneSelectionArgs != null ? oneSelectionArgs.length : 0;
            int otherSelectionArgSize = otherSlectionArgs != null ? otherSlectionArgs.length : 0;
            selectionArgs = new String[oneSelectionArgSize + otherSelectionArgSize];
            // Copy
            if (oneSelectionArgSize > 0) {
                System.arraycopy(oneSelectionArgs, 0, selectionArgs, 0, oneSelectionArgSize);
            }
            if (otherSelectionArgSize > 0) {
                System.arraycopy(otherSlectionArgs, 0, selectionArgs, oneSelectionArgSize, otherSelectionArgSize);
            }
        }

        return new DbSelection(selection, selectionArgs);
    }


}
