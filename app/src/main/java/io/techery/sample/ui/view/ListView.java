package io.techery.sample.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import butterknife.Bind;
import io.techery.presenta.mortarscreen.presenter.InjectablePresenter;
import io.techery.sample.R;

public abstract class ListView<T extends InjectablePresenter> extends ScreenView<T> {

    @Bind(R.id.swipe_layout) SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    public ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRefreshing(boolean refreshing){
        refreshLayout.setRefreshing(refreshing);
    }
}
