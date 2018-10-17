package com.yudapratama.contact_apps_jenius_test.intent;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.yudapratama.contact_apps_jenius_test.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public class CaptureImageIntent extends BaseIntent {

    private static final String TAG = "Intent";

    private static Uri result;

    public CaptureImageIntent(@NonNull Context context) {
        super(MediaStore.ACTION_IMAGE_CAPTURE);
        final File tempFile = createTempFile(context);
        if (tempFile == null) {
            throw new RuntimeException("invalid configuration!");
        } else {
            result = Uri.fromFile(tempFile);
            putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", tempFile));
        }
    }

    public static Uri getResult() {
        return result;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File createTempFile(@NonNull Context context) {
        try {
            final File parent = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.fileprovider_parent));
            if (parent.exists()) {
                Log.d(TAG + " [1/3]", "parent folder already loadOrCreate, skipping...");
            } else {
                Log.d(TAG + " [1/3]", "parent folder not found, creating...");
                parent.mkdirs();
            }

            final File file = new File(parent, context.getString(R.string.fileprovider_child));
            if (file.exists()) {
                file.delete();
                Log.d(TAG + " [2/3]", "temp file found, deleting...");
            } else {
                Log.d(TAG + " [2/3]", "temp file does not exist, skipping...");
            }

            final boolean isFileCreated = file.createNewFile();
            Log.d(TAG + " [3/3]", isFileCreated ? "temp file created!" : "unable to create file!");
            return file;
        } catch (IOException ignored) {
            return null;
        }
    }
}
