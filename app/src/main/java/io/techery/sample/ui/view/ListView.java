package io.techery.sample.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import io.techery.presenta.mortarscreen.presenter.InjectablePresenter;
import trikita.anvil.DSL;
import trikita.anvil.RenderableAdapter;
import trikita.anvil.Supportv4DSL;

import static trikita.anvil.Anvil.render;
import static trikita.anvil.BaseDSL.FILL;
import static trikita.anvil.BaseDSL.MATCH;
import static trikita.anvil.BaseDSL.size;
import static trikita.anvil.DSL.adapter;
import static trikita.anvil.DSL.listView;
import static trikita.anvil.RenderableAdapter.withItems;
import static trikita.anvil.Supportv4DSL.refreshing;
import static trikita.anvil.Supportv4DSL.swipeRefreshLayout;

public abstract class ListView<T extends InjectablePresenter, I> extends ScreenView<T> {

    private boolean refreshing;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private OnItemClickListener<I> onItemClickListener;
    private List<I> items = new ArrayList<>();
    private final RenderableAdapter adapter = withItems(items, onCreateItemView());

    public ListView(Context context) {
        super(context);
    }

    protected abstract RenderableAdapter.Item<I> onCreateItemView();

    public final void updateItems(List<I> items) {
        this.items.clear();
        this.items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    public final void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        render();
    }

    public final void onRefresh(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public final void onItemClick(OnItemClickListener<I> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public final void view() {
        swipeRefreshLayout(() -> {
            size(MATCH, MATCH);
            refreshing(refreshing);
            Supportv4DSL.onRefresh(() -> {
                if (onRefreshListener != null)
                    onRefreshListener.onRefresh();
            });
            listView(() -> {
                size(MATCH, MATCH);
                adapter(adapter);
                DSL.onItemClick((parent, view, position, id) -> {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(items.get(position));
                });
            });
        });
        adapter.notifyDataSetChanged();
    }

    public interface OnItemClickListener<I> {
        void onItemClick(I item);
    }
}
