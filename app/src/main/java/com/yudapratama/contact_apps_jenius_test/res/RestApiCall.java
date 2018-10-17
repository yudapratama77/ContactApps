package com.yudapratama.contact_apps_jenius_test.res;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.yudapratama.contact_apps_jenius_test.res.response.StatusResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tr.xip.errorview.ErrorView;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public final class RestApiCall<Response> {

    private static final String ERROR_EMPTY = "There was no response.";
    private static final String ERROR_PARSING = "There was an error parsing the message.";
    private static final String ERROR_NETWORK = "Please connect to internet to proceed.";
    private static final String ERROR_UNKNOWN = "Unknown error.";

    @NonNull
    private final Observable<Response> call;
    @Nullable
    private MaterialDialog dialog;
    @Nullable private ProgressBar progressBar;
    @Nullable private Snackbar snackbar;
    @Nullable private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable private ErrorView errorView;
    @Nullable private List<View> disableViews;

    public RestApiCall(@NonNull Observable<Response> call) {
        this.call = call;
    }

    public RestApiCall<Response> withProgressDialog(@NonNull Context context, @Nullable String title, @NonNull String content) {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .content(content)
                .progress(true, 0)
                .cancelable(false);
        this.dialog = title != null ? builder.title(title).build() : builder.build();
        return this;
    }

    public RestApiCall<Response> withProgressBar(@NonNull ProgressBar progressBar) {
        this.progressBar = progressBar;
        return this;
    }

    public RestApiCall<Response> withSnackbar(@NonNull View view, @NonNull String content) {
        this.snackbar = Snackbar.make(view, content, Snackbar.LENGTH_INDEFINITE);
        return this;
    }

    public RestApiCall<Response> withSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        return this;
    }

    public RestApiCall<Response> withErrorView(@NonNull ErrorView errorView) {
        this.errorView = errorView;
        return this;
    }

    public RestApiCall<Response> disableView(@NonNull View... disableViews) {
        this.disableViews = Arrays.asList(disableViews);
        return this;
    }

    public void start(@NonNull final OnSuccessListener<Response> success, @NonNull final OnErrorListener error) {
        start(null, success, error);
    }

    public void start(@Nullable final Class<? extends StatusResponse> responseClass, @NonNull final OnSuccessListener<Response> success, @NonNull final OnErrorListener error) {
        showProgress();
        dismissError();

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        dismissProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgress();
                        showError();

                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            retrofit2.Response response = httpException.response();

                            if (response.errorBody() == null) {
                                error.onError(e, ERROR_EMPTY);
                            } else if (responseClass != null) {
                                try {
                                    error.onError(e, new Gson().getAdapter(responseClass).fromJson(response.errorBody().string()).status);
                                } catch (IOException exc) {
                                    error.onError(e, ERROR_PARSING);
                                }
                            }
                        } else if (e instanceof IOException) {
                            error.onError(e, ERROR_NETWORK);
                        } else {
                            error.onError(e, ERROR_UNKNOWN);
                        }
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response response) {
                        dismissProgress();
                        success.onSuccess(response);
                    }
                });
    }

    private void showProgress() {
        if (dialog != null)
            dialog.show();
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        if (snackbar != null)
            snackbar.show();
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
        if (disableViews != null)
            for (View view : disableViews)
                view.setEnabled(false);
    }

    private void dismissProgress() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        if (snackbar != null && snackbar.isShown())
            snackbar.dismiss();
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        if (disableViews != null)
            for (View view : disableViews)
                view.setEnabled(true);
    }

    private void showError() {
        if (errorView != null)
            errorView.setVisibility(View.VISIBLE);
    }

    private void dismissError() {
        if (errorView != null)
            errorView.setVisibility(View.GONE);
    }

    public interface OnErrorListener {
        void onError(@NonNull Throwable e, @NonNull String message);
    }

    public interface OnSuccessListener<Response> {
        void onSuccess(@NonNull Response response);
    }
}
