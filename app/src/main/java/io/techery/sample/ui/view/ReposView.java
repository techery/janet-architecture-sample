package io.techery.sample.ui.view;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import java.util.List;

import io.techery.sample.model.Repository;
import io.techery.sample.ui.adapter.ReposAdapter;
import io.techery.sample.ui.screen.ReposScreen;

public class ReposView extends ListView<ReposScreen.Presenter> {

    private ReposAdapter adapter;

    public ReposView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ReposAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) ->
                presenter.selectRepository(adapter.getItem(position)));
        refreshLayout.setOnRefreshListener(() -> presenter.loadRepos());
    }

    public void updateItems(List<Repository> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

}
