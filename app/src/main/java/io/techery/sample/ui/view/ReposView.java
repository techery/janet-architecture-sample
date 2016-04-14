package io.techery.sample.ui.view;

import android.content.Context;

import io.techery.sample.R;
import io.techery.sample.model.Repository;
import io.techery.sample.ui.screen.ReposScreen;
import trikita.anvil.RenderableAdapter;

import static android.widget.LinearLayout.VERTICAL;
import static io.techery.sample.ui.Style.textViewListItem;
import static io.techery.sample.utils.AnvilDSL.dipRes;
import static trikita.anvil.BaseDSL.MATCH;
import static trikita.anvil.BaseDSL.WRAP;
import static trikita.anvil.BaseDSL.centerVertical;
import static trikita.anvil.BaseDSL.padding;
import static trikita.anvil.BaseDSL.sip;
import static trikita.anvil.BaseDSL.size;
import static trikita.anvil.BaseDSL.textSize;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textColor;

public class ReposView extends ListView<ReposScreen.Presenter, Repository> {

    public ReposView(Context context) {
        super(context);
        onRefresh(presenter::loadRepos);
        onItemClick(presenter::selectRepository);
    }

    @Override
    protected RenderableAdapter.Item<Repository> onCreateItemView() {
        return (index, item) -> {
            linearLayout(() -> {
                size(MATCH, WRAP);
                centerVertical();
                orientation(VERTICAL);
                padding(dipRes(R.dimen.Widget_Margin_Medium));
                textViewListItem(() -> {
                    textColor(R.color.dark_grey);
                    textSize(sip(20));
                    text(item.name());
                });
                textViewListItem(() -> {
                    textSize(sip(13));
                    text(item.url());
                });
            });
        };
    }
}
