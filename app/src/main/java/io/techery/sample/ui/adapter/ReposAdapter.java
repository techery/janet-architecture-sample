package io.techery.sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.techery.sample.R;
import io.techery.sample.model.Repository;

public class ReposAdapter extends RecyclerViewAdapter<ReposAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<Repository> repositories = new ArrayList<>();

    public ReposAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.repo_item, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Repository repository = getItem(position);
        holder.name.setText(repository.name());
        holder.url.setText(repository.url());
    }

    @Override public int getItemCount() {
        return repositories.size();
    }

    public void setItems(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public Repository getItem(int position){
        return repositories.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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
