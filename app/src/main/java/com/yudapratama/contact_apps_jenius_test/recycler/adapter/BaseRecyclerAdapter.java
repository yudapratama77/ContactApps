package com.yudapratama.contact_apps_jenius_test.recycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.yudapratama.contact_apps_jenius_test.core.RealmContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public abstract class BaseRecyclerAdapter<Item, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements BaseAdapter<Item> {

    @NonNull
    private final RealmContext context;
    @NonNull
    private final List<Item> items;

    @NonNull
    private OnEmptyListener listener;

    public BaseRecyclerAdapter(@NonNull RealmContext context) {
        this(context, new ArrayList<>());
    }

    protected BaseRecyclerAdapter(@NonNull RealmContext context, @NonNull List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    @NonNull
    public List<Item> getItems() {
        return items;
    }

    @NonNull
    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public void add(@NonNull Item item) {
        boolean success = items.add(item);
        if (listener != null && success)
            listener.onEmpty(items.isEmpty());
    }

    @Override
    public void addAll(@NonNull Collection<Item> c) {
        boolean success = items.addAll(c);
        if (listener != null && success)
            listener.onEmpty(items.isEmpty());
    }

    @Override
    public void remove(@NonNull Item item) {
        boolean success = items.remove(item);
        if (listener != null && success)
            listener.onEmpty(items.isEmpty());
    }

    @Override
    public void clear() {
        boolean alreadyEmpty = items.isEmpty();
        items.clear();
        if (listener != null && !alreadyEmpty)
            listener.onEmpty(items.isEmpty());
    }

    @NonNull
    @Override
    public RecyclerView.Adapter setOnEmptyListener(@NonNull OnEmptyListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    @NonNull
    public Context getContext() {
        return context.getContext();
    }

    @NonNull
    @Override
    public Realm getRealm() {
        return context.getRealm();
    }
}
