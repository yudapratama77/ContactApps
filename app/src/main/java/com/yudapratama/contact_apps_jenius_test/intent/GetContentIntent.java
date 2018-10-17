package com.yudapratama.contact_apps_jenius_test.intent;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public class GetContentIntent extends BaseIntent {

    public GetContentIntent(@NonNull String... mimeTypes) {
        this(false, mimeTypes);
    }

    public GetContentIntent(boolean pickMultiple, @NonNull String... mimeTypes) {
        super(Intent.ACTION_GET_CONTENT);
        // set multiple if supported
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, pickMultiple);
        // set type
        setType(TextUtils.join(" ", mimeTypes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
    }

    @NonNull
    public static Uri[] extract(@NonNull Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && data.getClipData() != null) {
            final ClipData clipData = data.getClipData();
            final Uri[] results = new Uri[clipData.getItemCount()];
            for (int i = 0; i < clipData.getItemCount(); i++)
                results[i] = clipData.getItemAt(i).getUri();
            return results;
        } else {
            return new Uri[]{data.getData()};
        }
    }
}
