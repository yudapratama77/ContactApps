package com.yudapratama.contact_apps_jenius_test.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public final class RecyclerView<E> extends android.support.v7.widget.RecyclerView implements List<E> {

    /**
     * Items for adapter. By default, this is an ArrayList, call {@link RecyclerView#setItems(List)}
     * to use custom items.
     */
    @NonNull
    private List<E> items;

    private Pagination pagination;

    public RecyclerView(Context context) {
        super(context);
        items = new ArrayList<>();
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        items = new ArrayList<>();
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        items = new ArrayList<>();
    }

    public void setItems(@NonNull List<E> items) {
        this.items = items;
    }

    public void setAdapter(@NonNull Adapter adapter, @NonNull ErrorView errorView) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (adapter.getItemCount() == 0)
                    errorView.showEmpty();
                else
                    errorView.hide();
            }
        });
    }

    public void setPagination(@NonNull Pagination pagination) {
        this.pagination = pagination;
    }

    public Pagination getPagination() {
        return pagination;
    }

    //region Collection
    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return items.contains(o);
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return items.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return items.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] a) {
        return items.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return items.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return items.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return items.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return items.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return items.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return items.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return items.retainAll(c);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public E get(int index) {
        return items.get(index);
    }

    @Override
    public E set(int index, E element) {
        return items.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        items.add(index, element);
    }

    @Override
    public E remove(int index) {
        return items.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return items.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return items.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return items.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return items.listIterator(index);
    }

    @NonNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return items.subList(fromIndex, toIndex);
    }
    //endregion

    public static final class Pagination<E> implements Paginate.Callbacks {
        @NonNull private Builder<E> builder;
        @NonNull private Paginate paginate;

        private int page = 1;
        private boolean isLoading = false;
        private boolean isEnd = false;

        private Pagination(@NonNull Builder<E> builder) {
            this.builder = builder;
            this.paginate = Paginate.with(builder.recyclerView, this).build();
            if (builder.swipeRefreshLayout != null)
                builder.swipeRefreshLayout.setOnRefreshListener(() -> {
                    builder.swipeRefreshLayout.setRefreshing(false);
                    reset();
                    int size = builder.recyclerView.size();
                    builder.recyclerView.clear();
                    builder.recyclerView.getAdapter().notifyItemRangeRemoved(0, size);
                });
        }

        @Override
        public void onLoadMore() {
            if (builder.errorView != null)
                builder.errorView.hide();
            isLoading = true;
            if (builder.listener != null)
                builder.listener.onShouldPaginate(this, builder.recyclerView);
        }

        @Override
        public boolean isLoading() {
            if (builder.errorView != null && isLoading)
                builder.errorView.hide();
            return isLoading;
        }

        @Override
        public boolean hasLoadedAllItems() {
            if (builder.errorView != null && isEnd && builder.recyclerView.getAdapter().getItemCount() == 0)
                builder.errorView.showEmpty();
            return isEnd;
        }

        public int getPage() {
            return page;
        }

        public void loadingSuccess(boolean pageEnd) {
            isLoading = false;
            page++;
            isEnd = pageEnd;
            paginate.setHasMoreDataToLoad(!pageEnd);
            hasLoadedAllItems();
        }

        public void loadingError() {
            isLoading = false;
            paginate.setHasMoreDataToLoad(false);
        }

        public void reset() {
            isLoading = false;
            page = 1;
            isEnd = false;
            paginate.setHasMoreDataToLoad(true);
        }

        public static final class Builder<E> {
            @NonNull private RecyclerView<E> recyclerView;
            @Nullable private SwipeRefreshLayout swipeRefreshLayout;
            @Nullable private ErrorView errorView;
            @Nullable private OnShouldPaginateListener<E> listener;

            public Builder(@NonNull RecyclerView<E> recyclerView) {
                this.recyclerView = recyclerView;
            }

            public Builder<E> withSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
                this.swipeRefreshLayout = swipeRefreshLayout;
                return this;
            }

            public Builder<E> withErrorView(@NonNull ErrorView errorView) {
                this.errorView = errorView;
                return this;
            }

            public Builder<E> onShouldPaginate(@Nullable OnShouldPaginateListener<E> listener) {
                this.listener = listener;
                return this;
            }

            public Pagination<E> build() {
                return new Pagination<>(this);
            }
        }

        public interface OnShouldPaginateListener<E> {
            void onShouldPaginate(@Nullable Pagination pagination, @NonNull RecyclerView<E> recyclerView);
        }
    }
}
