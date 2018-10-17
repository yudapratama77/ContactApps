package com.yudapratama.contact_apps_jenius_test.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hendraanggrian.dispatcher.Dispatcher;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.yudapratama.contact_apps_jenius_test.R;
import com.yudapratama.contact_apps_jenius_test.activity.base.BaseActivity;
import com.yudapratama.contact_apps_jenius_test.intent.CaptureImageIntent;
import com.yudapratama.contact_apps_jenius_test.intent.GetContentIntent;
import com.yudapratama.contact_apps_jenius_test.util.CommonUtil;
import com.yudapratama.contact_apps_jenius_test.view.BottomSheetLayout;

import java.io.File;

import butterknife.BindView;
import okhttp3.MultipartBody;

/**
 * Created by yudapratama on 10/16/18.
 * yudaapratamaa77@gmail.com
 */

public class AddContactActivity extends BaseActivity {

    @BindView(R.id.bottomsheetlayout_addcontact)
    BottomSheetLayout bottomSheetLayout;
    @BindView(R.id.toolbar_addcontact)
    Toolbar toolbar;
    @BindView(R.id.edittext_firstname_addcontact)
    EditText editTextFirstname;
    @BindView(R.id.edittext_lastname_addcontact)
    EditText editTextLastname;
    @BindView(R.id.edittext_age_addcontact)
    EditText editTextAge;
    @BindView(R.id.imageview_photo_addcontact)
    ImageView imageViewPhoto;
    @BindView(R.id.imagebutton_photo_addcontact)
    ImageButton imageButtonPhoto;
    @BindView(R.id.button_add_addcontact)
    Button buttonAddContact;

    private Uri photoUri;

    public MultipartBody.Part imagePartPhoto;

    @Override
    public int getContentView() {
        return R.layout.activity_add_contact;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        imageButtonPhoto.setOnClickListener(v -> {
            openImagePicker();
        });

        buttonAddContact.setOnClickListener(v -> {
            addNewContact();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Dispatcher.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            Uri uriProfile = photoUri;
            Picasso.with(this).load(uriProfile).into(imageViewPhoto);
            imagePartPhoto = CommonUtil.generateMultiPart(getContext(), "photo", photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Dispatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void openImagePicker() {

        bottomSheetLayout.showImagePicker(this,
                () -> Dispatcher.with(this)
                        .requestPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(requested -> Dispatcher.with(this)
                                .startActivityForResult(new CaptureImageIntent(getContext()))
                                .onOK((requestCode, resultCode, data) -> {
                                    photoUri = Uri.fromFile(new File(getContext().getCacheDir(), "contact_photo_" + System.currentTimeMillis() + ".jpg"));
                                    Crop.of(CaptureImageIntent.getResult(), photoUri).start(this);
                                })
                                .dispatch())
                        .onDenied(permissions -> Snackbar.make(bottomSheetLayout, "Permission denied to instance.", Snackbar.LENGTH_SHORT).show())
                        .onShouldShowRationale((dispatcher, permissions) -> new MaterialDialog.Builder(this)
                                .title("Permission is required")
                                .content("To capture your photo, please grant Siwasdar with instance permission.")
                                .positiveText(android.R.string.ok)
                                .onPositive((dialog, which) -> dispatcher.dispatch())
                                .show())
                        .dispatch(),
                () -> Dispatcher.with(this)
                        .startActivityForResult(new GetContentIntent("image/*"))
                        .onOK((requestCode, resultCode, data) -> {
                            photoUri = Uri.fromFile(new File(getContext().getCacheDir(), "contact_photo_" + System.currentTimeMillis() + ".jpg"));
                            Crop.of(GetContentIntent.extract(data)[0], photoUri).start(this);
                        })
                        .dispatch(),
                uri -> {
                    photoUri = Uri.fromFile(new File(getContext().getCacheDir(), "contact_photo_" + System.currentTimeMillis() + ".jpg"));
                    Crop.of(uri, photoUri).start(this);
                });
    }

    public void addNewContact() {

//        Contact contact = new Contact();
//
//        String firstname = editTextFirstname.getText().toString();
//        String lastname = editTextLastname.getText().toString();
//        int age = 25;
//
//        contact.setFirstName(firstname);
//        contact.setLastName(lastname);
//        contact.setAge(age);

//        new RestApiCall<>(RestApi.BASE_URL.create(ContactGroup.class).addContact(editTextFirstname.getText().toString(),
//                editTextLastname.getText().toString(), editTextAge.getText(),
//                imagePartPhoto))
//                .withProgressDialog(getContext(), "Please wait...", "Please wait...")
//                .start(statusResponse -> {
//                    Toast.makeText(AddContactActivity.this, "Contact added", Toast.LENGTH_LONG).show();
//                }, (e, message) -> Toast.makeText(AddContactActivity.this, message, Toast.LENGTH_LONG).show());
    }
}
