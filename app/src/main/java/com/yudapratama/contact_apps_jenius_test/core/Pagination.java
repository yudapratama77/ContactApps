package com.yudapratama.contact_apps_jenius_test.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.paginate.Paginate;
import com.yudapratama.contact_apps_jenius_test.recycler.adapter.BaseAdapter;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public final class Pagination<Item> {

    private int page;
    private boolean loading;
    private boolean pageEnd;

    private BaseAdapter<Item> baseAdapter;

    @SuppressWarnings("unchecked")
    private Pagination(@NonNull RecyclerView recyclerView, @NonNull final OnShouldPaginateListener<Item> listener) {
        baseAdapter = (BaseAdapter<Item>) recyclerView.getAdapter();
        reset(false);
        Paginate.with(recyclerView, new Paginate.Callbacks() {

            @Override
            public void onLoadMore() {
                loading = true;
                listener.onShouldPaginate(Pagination.this, baseAdapter);
            }

            @Override
            public boolean isLoading() {
                return loading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return pageEnd;
            }
        }).build();
    }

    public void reset() {
        reset(true);
    }

    public int getPage() {
        return page;
    }

    public void loadingError() {
        this.loading = false;
    }

    public void loadingSuccess(boolean pageEnd) {
        this.loading = false;
        this.page++;
        this.pageEnd = pageEnd;
    }

    private void reset(boolean alsoResetItems) {
        loading = false;
        page = 1;
        pageEnd = false;
        if (alsoResetItems)
            baseAdapter.clear();
    }

    public interface OnShouldPaginateListener<Item> {

        void onShouldPaginate(@NonNull Pagination pagination, @NonNull BaseAdapter<Item> adapter);
    }

    public static <Item> Pagination attach(@NonNull RecyclerView recyclerView, @NonNull OnShouldPaginateListener<Item> listener) {
        return new Pagination<>(recyclerView, listener);
    }
}
