package io.techery.sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.techery.sample.R;
import io.techery.sample.model.User;

public class ContributorsAdapter extends RecyclerViewAdapter<ContributorsAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<User> repositories = new ArrayList<>();
    private final Context context;

    public ContributorsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.user_item, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        User user = getItem(position);
        holder.name.setText(user.login());
        holder.url.setText(user.url());
        Picasso.with(context)
                .load(user.avatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.photo);
    }

    @Override public int getItemCount() {
        return repositories.size();
    }

    public void setItems(List<User> repositories) {
        this.repositories = repositories;
    }

    public User getItem(int position) {
        return repositories.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.photo)
        public ImageView photo;
        @Bind(R.id.name)
        public TextView name;
        @Bind(R.id.url)
        public TextView url;


        public ViewHolder(View item) {
            super(item);
            ButterKnife.bind(this, item);
        }
    }

}
