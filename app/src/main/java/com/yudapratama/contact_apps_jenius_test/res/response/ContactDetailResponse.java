package com.yudapratama.contact_apps_jenius_test.res.response;

import com.yudapratama.contact_apps_jenius_test.res.body.Contact;

public final class ContactDetailResponse extends StatusResponse {

    public Contact data;

    public Contact getData() {
        return data;
    }

    public void setData(Contact data) {
        this.data = data;
    }

    public ContactDetailResponse(){
    }
}
