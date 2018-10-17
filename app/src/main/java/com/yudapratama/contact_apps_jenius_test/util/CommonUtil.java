package com.yudapratama.contact_apps_jenius_test.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;

import java.io.File;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public class CommonUtil {

    /**
     * load color resource from theme
     * @param context context to used
     * @param colorIntId color id resource for android API >= 21 (e.g. android.R.colorPrimary)
     * @param colorStringId color id resource for android API < 21 (e.g. colorPrimary)
     * @return The color value as integer
     */
    public static int getThemeColor(@NonNull Context context, int colorIntId, @NonNull String colorStringId) {
        return getThemeProperty(context, colorIntId, colorStringId).data;
    }

    /**
     * load color resource from theme"
     * @param context context to used
     * @param resourceIdInt resource id as integer for android API >= 21 (e.g. android.R.colorPrimary)
     * @param resourceIdString resource id as string for android API < 21 (e.g. colorPrimary)
     * @return Theme property value holder
     */
    private static TypedValue getThemeProperty(@NonNull Context context, int resourceIdInt, @NonNull String resourceIdString) {
        TypedValue outValue = new TypedValue();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            resourceIdInt = context.getResources().getIdentifier(resourceIdString, "attr", context.getPackageName());
        }

        context.getTheme().resolveAttribute(resourceIdInt, outValue, true);

        return outValue;
    }

    /**
     * Generates the file multipart body.
     * @param context Android context object
     * @param key part name
     * @param uri file uri
     * @return The request body
     */
    public static MultipartBody.Part generateMultiPart(@NonNull Context context,
                                                       @NonNull String key,
                                                       @NonNull Uri uri) {
        /** Get the actual file by uri */
        File file = new File(uri.getPath());

        /** Get the mime type */
        String mimeType = context.getContentResolver().getType(uri);
        /** Sometimes it returns as a null */
        if (mimeType == null) {
            mimeType = URLConnection.guessContentTypeFromName(uri.getPath());
        }

        /** Create RequestBody instance from file */
        if (mimeType != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        } else {
            Log.w(CommonUtil.class.getSimpleName(), "Incorrect file type, unable to determine the mimetype");
            return null;
        }
    }
}
