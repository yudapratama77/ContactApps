package com.yudapratama.contact_apps_jenius_test.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yudapratama.contact_apps_jenius_test.R;
import com.yudapratama.contact_apps_jenius_test.activity.base.BaseActivity;
import com.yudapratama.contact_apps_jenius_test.res.RestApi;
import com.yudapratama.contact_apps_jenius_test.res.RestApiCall;
import com.yudapratama.contact_apps_jenius_test.res.body.Contact;
import com.yudapratama.contact_apps_jenius_test.res.group.ContactGroup;

import butterknife.BindView;
import io.github.hendraanggrian.extras.BindExtra;
import io.github.hendraanggrian.extras.Extras;

public class ContactDetailActivity extends BaseActivity {


    public static final String CONTACT_ID = "CONTACT_ID";
    private Contact contact;

    @BindView(R.id.toolbar_contactdetail)
    Toolbar toolbar;
    @BindView(R.id.edittext_firstname_contactdetail)
    EditText editTextFirstname;
    @BindView(R.id.edittext_lastname_contactdetail)
    EditText editTextLastname;
    @BindView(R.id.edittext_age_contactdetail)
    EditText editTextAge;
    @BindView(R.id.imageview_photo_contactdetail)
    ImageView imageViewPhoto;
    @BindView(R.id.button_update_contactdetail)
    Button buttonUpdate;
    @BindExtra(CONTACT_ID)
    String id;

    @Override
    public int getContentView() {
        return R.layout.activity_contact_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extras.bind(this);

        new RestApiCall<>(RestApi.BASE_URL.create(ContactGroup.class).getContactDetail(id))
                .start(contactDetailResponse -> {
                    contact = contactDetailResponse.getData();
                    initView(contact);
                }, (e, message) -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    public static void start(@NonNull Context context, String id) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.CONTACT_ID, id);
        context.startActivity(intent);
    }

    public void initView(Contact contact) {
        editTextFirstname.setText(contact.getFirstName());
        editTextLastname.setText(contact.getLastName());
        //editTextAge.setText(contact.getAge());
    }

    public void updateContact() {

    }
}
