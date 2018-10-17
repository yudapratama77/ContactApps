package com.yudapratama.contact_apps_jenius_test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yudapratama.contact_apps_jenius_test.R;
import com.yudapratama.contact_apps_jenius_test.activity.base.BaseActivity;
import com.yudapratama.contact_apps_jenius_test.res.RestApi;
import com.yudapratama.contact_apps_jenius_test.res.RestApiCall;
import com.yudapratama.contact_apps_jenius_test.res.body.Contact;
import com.yudapratama.contact_apps_jenius_test.res.group.ContactGroup;
import com.yudapratama.contact_apps_jenius_test.res.response.ContactResponse;
import com.yudapratama.contact_apps_jenius_test.view.ErrorView;
import com.yudapratama.contact_apps_jenius_test.view.RecyclerView;
import com.yudapratama.contact_apps_jenius_test.view.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yudapratama on 10/16/18.
 * yudaapratamaa77@gmail.com
 */

public class ContactListActivity extends BaseActivity {

    @BindView(R.id.toolbar_contact_list)
    Toolbar toolbar;
    @BindView(R.id.swiperefreshlayout_contactlist)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview_contactlist)
    RecyclerView<Contact> recyclerView;
    @BindView(R.id.errorview_contactlist)
    ErrorView errorView;
    @BindView(R.id.fab_contactlist)
    FloatingActionButton floatingActionButton;

    @Override
    public int getContentView() {
        return R.layout.activity_contact_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        recyclerView.setAdapter(new ContactAdapter(), errorView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setPagination(new RecyclerView.Pagination.Builder<>(recyclerView)
                .withSwipeRefreshLayout(swipeRefreshLayout)
                .withErrorView(errorView)
                .onShouldPaginate((pagination, recyclerView) -> new RestApiCall<>(RestApi.BASE_URL.create(ContactGroup.class).getAllContact())
                        .start(ContactResponse.class, response -> {
                            pagination.loadingSuccess(response.data.isEmpty());
                            if (!response.data.isEmpty()) {
                                recyclerView.addAll(response.data);
                                recyclerView.getAdapter().notifyItemRangeInserted(recyclerView.size() - response.data.size() + 1, response.data.size());
                            }
                        }, (e, message) -> pagination.loadingError()))
                .build());

        addNewContact();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Anda yakin akan keluar ?");
        alertDialogBuilder.setMessage("Klik oke untuk keluar")
                .setCancelable(false)
                .setPositiveButton("Oke", (dialog, which) -> {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                })
                .setNegativeButton("Tidak", ((dialog, which) -> {
                    dialog.cancel();
                }));

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void addNewContact() {
        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(this, AddContactActivity.class)));
    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

        @Override
        public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContactAdapter.ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.recycler_contact_list, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Picasso.with(getContext()).load(recyclerView.get(position).photo).into(holder.imageView);
            holder.textViewFirstName.setText(recyclerView.get(position).firstName);
            holder.textViewLastName.setText(recyclerView.get(position).lastName);
            //holder.textViewAge.setText(recyclerView.get(position).age);
            holder.cardView.setOnClickListener(v -> getContext().startActivity(new Intent(getContext(), ContactDetailActivity.class)
                    .putExtra(ContactDetailActivity.CONTACT_ID,recyclerView.get(position).id)));
        }

        @Override
        public int getItemCount() {
            return recyclerView.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.cardview_contact)
            CardView cardView;
            @BindView(R.id.imageview_contact_photo)
            ImageView imageView;
            @BindView(R.id.textview_contact_firstname)
            TextView textViewFirstName;
            @BindView(R.id.textview_contact_lastname)
            TextView textViewLastName;
            @BindView(R.id.textview_contact_age)
            TextView textViewAge;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
