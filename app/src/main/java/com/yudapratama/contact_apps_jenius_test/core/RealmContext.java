package com.yudapratama.contact_apps_jenius_test.core;

import android.content.Context;
import android.support.annotation.NonNull;

import io.realm.Realm;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public interface RealmContext {
    @NonNull
    Context getContext();

    @NonNull
    Realm getRealm();

    interface Holder {
        RealmContext getRealmContext();
    }

    interface ManualLifecycle {

        void getInstance();

        void closeInstance();
    }
}
