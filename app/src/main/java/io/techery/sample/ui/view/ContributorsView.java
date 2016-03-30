package io.techery.sample.ui.view;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import java.util.List;

import io.techery.sample.model.User;
import io.techery.sample.ui.adapter.ContributorsAdapter;
import io.techery.sample.ui.screen.ContributorsScreen;

public class ContributorsView extends ListView<ContributorsScreen.Presenter> {

    private ContributorsAdapter adapter;

    public ContributorsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ContributorsAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) ->
                presenter.selectContributor(adapter.getItem(position)));
        refreshLayout.setOnRefreshListener(() -> presenter.loadContributors());
    }

    public void updateItems(List<User> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

}
