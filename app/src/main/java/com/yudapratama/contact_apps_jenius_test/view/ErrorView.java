package com.yudapratama.contact_apps_jenius_test.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public class ErrorView extends tr.xip.errorview.ErrorView {

    @Nullable
    private String emptyTitle;
    @Nullable
    private String emptySubtitle;

    public ErrorView(Context context) {
        super(context);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
    }

    public void setContent(@Nullable String title, @Nullable String subtitle) {
        setTitle(title != null ? title : "");
        setSubtitle(subtitle != null ? subtitle : "");
    }

    public void showEmpty() {
        show(emptyTitle, emptySubtitle);
    }

    public void show(@Nullable String title, @Nullable String subtitle) {
        setVisibility(VISIBLE);
        setTitle(title);
        setSubtitle(subtitle);
    }

    public void hide() {
        setVisibility(GONE);
    }
}
