package eu.ttbox.nfcproxy;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import eu.ttbox.nfcproxy.domain.db.NfcProxyDbOpenHelper;

public class NfcProxyApplication extends Application {

    private String TAG = "NfcProxyApplication";

    private static NfcProxyApplication APP_INSTANCE;

    // DataBase
    private NfcProxyDbOpenHelper replayDatabase;


    // ===========================================================
    // Constructors
    // ===========================================================

    @Override
    public void onCreate() {
        APP_INSTANCE = this;
        super.onCreate();
    }

    // ===========================================================
    // Database instance
    // ===========================================================

    public NfcProxyDbOpenHelper getNfcProxyDbOpenHelper() {
        if (replayDatabase == null) {
            replayDatabase = new NfcProxyDbOpenHelper(this);
        }
        return replayDatabase;
    }


    // ===========================================================
    // Statistic
    // ===========================================================

    public static NfcProxyApplication getInstance() {
        return APP_INSTANCE;
    }

    // ===========================================================
    // Accessors
    // ===========================================================

    /**
     * Get Application Version
     *
     * @return
     */
    public String version() {
        return String.format("Version : %s/%s", getPackageName(), versionName());
    }

    public String versionPackageName() {
        return String.format("%s/%s", getPackageName(), versionName());
    }

    public String versionName() {
        try {
            final PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } // try
        catch (PackageManager.NameNotFoundException nnfe) {
            return "Unknown";
        }
    }

    public int versionCode() {
        try {
            final PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } // try
        catch (PackageManager.NameNotFoundException nnfe) {
            return 0;
        }
    }
}