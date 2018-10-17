package com.yudapratama.contact_apps_jenius_test.recycler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.yudapratama.contact_apps_jenius_test.core.RealmContext;

import java.util.Collection;
import java.util.List;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public interface BaseAdapter<Item> extends RealmContext {

    @NonNull
    List<Item> getItems();

    @NonNull
    Item getItem(int position);

    void add(@NonNull Item item);

    void addAll(@NonNull Collection<Item> c);

    void remove(@NonNull Item item);

    void clear();

    RecyclerView.Adapter setOnEmptyListener(@NonNull OnEmptyListener listener);

    interface OnEmptyListener {
        void onEmpty(boolean isEmpty);
    }
}
