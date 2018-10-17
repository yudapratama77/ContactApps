package com.yudapratama.contact_apps_jenius_test.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public abstract class BaseIntent extends Intent {

    BaseIntent(String action) {
        super(action);
    }

    BaseIntent(String action, Uri uri) {
        super(action, uri);
    }

    public boolean isAvailable(@NonNull Context context) {
        return resolveActivity(context.getPackageManager()) != null;
    }
}
