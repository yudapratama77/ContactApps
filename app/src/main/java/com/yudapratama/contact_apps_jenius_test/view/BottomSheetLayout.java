package com.yudapratama.contact_apps_jenius_test.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;
import com.hendraanggrian.dispatcher.Dispatcher;
import com.hendraanggrian.dispatcher.Source;
import com.squareup.picasso.Picasso;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public final class BottomSheetLayout extends com.flipboard.bottomsheet.BottomSheetLayout {

    private static final String DEFAULT_TITLE = "Choose an image...";
    private static final int MAX_ITEMS = 60;

    public BottomSheetLayout(Context context) {
        super(context);
        init();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setPeekSheetTranslation(Resources.getSystem().getDisplayMetrics().heightPixels / 2);
    }

    public void showImagePicker(@NonNull Fragment fragment, @NonNull OnCameraSelectedListener cameraListener, @NonNull OnPickerSelectedListener pickerListener, @NonNull OnImageSelectedListener imageListener) {
        showImagePicker(Source.valueOf(fragment), null, cameraListener, pickerListener, imageListener);
    }

    public void showImagePicker(@NonNull Fragment fragment, @NonNull OnSelectedListener listener) {
        showImagePicker(Source.valueOf(fragment), null, listener);
    }

    public void showImagePicker(@NonNull Fragment fragment, @NonNull String title, @NonNull OnCameraSelectedListener cameraListener, @NonNull OnPickerSelectedListener pickerListener, @NonNull OnImageSelectedListener imageListener) {
        showImagePicker(Source.valueOf(fragment), title, cameraListener, pickerListener, imageListener);
    }

    public void showImagePicker(@NonNull Fragment fragment, @NonNull String title, @NonNull OnSelectedListener listener) {
        showImagePicker(Source.valueOf(fragment), title, listener);
    }

    public void showImagePicker(@NonNull Activity activity, @NonNull OnCameraSelectedListener cameraListener, @NonNull OnPickerSelectedListener pickerListener, @NonNull OnImageSelectedListener imageListener) {
        showImagePicker(Source.valueOf(activity), null, cameraListener, pickerListener, imageListener);
    }

    public void showImagePicker(@NonNull Activity activity, @NonNull OnSelectedListener listener) {
        showImagePicker(Source.valueOf(activity), null, listener);
    }

    public void showImagePicker(@NonNull Activity activity, @NonNull String title, @NonNull OnCameraSelectedListener cameraListener, @NonNull OnPickerSelectedListener pickerListener, @NonNull OnImageSelectedListener imageListener) {
        showImagePicker(Source.valueOf(activity), title, cameraListener, pickerListener, imageListener);
    }

    public void showImagePicker(@NonNull Activity activity, @NonNull String title, @NonNull OnSelectedListener listener) {
        showImagePicker(Source.valueOf(activity), title, listener);
    }

    private void showImagePicker(@NonNull Source source, @Nullable String title, @NonNull OnCameraSelectedListener cameraListener, @NonNull OnPickerSelectedListener pickerListener, @NonNull OnImageSelectedListener imageListener) {
        showImagePicker(source, title, new OnSelectedListener() {
            @Override
            public void onCameraSelected() {
                cameraListener.onCameraSelected();
            }

            @Override
            public void onImageSelected(@NonNull Uri uri) {
                imageListener.onImageSelected(uri);
            }

            @Override
            public void onPickerSelected() {
                pickerListener.onPickerSelected();
            }
        });
    }

    private void showImagePicker(@NonNull Source source, @Nullable String title, @NonNull OnSelectedListener listener) {
        Dispatcher.with(source)
                .requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onGranted(requested -> showWithSheetView(new ImagePickerSheetView.Builder(source.getContext())
                        .setTitle(title != null ? title : DEFAULT_TITLE)
                        .setMaxItems(MAX_ITEMS)
                        .setShowCameraOption(true)
                        .setShowPickerOption(true)
                        .setImageProvider((imageView, imageUri, size) -> Picasso.with(source.getContext())
                                .load(imageUri)
                                .fit()
                                .centerCrop()
                                .into(imageView))
                        .setOnTileSelectedListener(selectedTile -> {
                            dismissSheet();
                            if (selectedTile.isCameraTile())
                                Dispatcher.with(source)
                                        .requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                                        .onGranted(requested1 -> listener.onCameraSelected())
                                        .onDenied(permissions -> Toast.makeText(source.getContext(), "Camera permission denied.", Toast.LENGTH_SHORT).show())
                                        .dispatch();
                            else if (selectedTile.isPickerTile())
                                listener.onPickerSelected();
                            else if (selectedTile.isImageTile() && selectedTile.getImageUri() != null)
                                listener.onImageSelected(selectedTile.getImageUri());
                        })
                        .create()))
                .onDenied(permissions -> Toast.makeText(source.getContext(), "Permission denied to showProgress image picker.", Toast.LENGTH_SHORT).show())
                .onShouldShowRationale((dispatcher, permissions) -> new MaterialDialog.Builder(source.getContext())
                        .title("Permission is required")
                        .content("To show a picker, please enable reading your external storage.")
                        .negativeText(android.R.string.cancel)
                        .positiveText(android.R.string.ok)
                        .onPositive((dialog, which) -> dispatcher.dispatch())
                        .show())
                .dispatch();
    }

    public interface OnCameraSelectedListener {

        void onCameraSelected();
    }

    public interface OnPickerSelectedListener {

        void onPickerSelected();
    }

    public interface OnImageSelectedListener {

        void onImageSelected(@NonNull Uri uri);
    }

    public interface OnSelectedListener extends OnCameraSelectedListener, OnPickerSelectedListener, OnImageSelectedListener {

    }
}
