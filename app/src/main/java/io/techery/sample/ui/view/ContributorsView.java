package io.techery.sample.ui.view;

import android.content.Context;

import io.techery.sample.R;
import io.techery.sample.model.User;
import io.techery.sample.ui.screen.ContributorsScreen;
import trikita.anvil.RenderableAdapter;

import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.LinearLayout.VERTICAL;
import static io.techery.sample.ui.Style.textViewListItem;
import static io.techery.sample.utils.AnvilDSL.dipRes;
import static io.techery.sample.utils.AnvilDSL.marginEnd;
import static io.techery.sample.utils.PicassoLoadDSL.load;
import static io.techery.sample.utils.PicassoLoadDSL.picasso;
import static trikita.anvil.BaseDSL.MATCH;
import static trikita.anvil.BaseDSL.WRAP;
import static trikita.anvil.BaseDSL.centerVertical;
import static trikita.anvil.BaseDSL.margin;
import static trikita.anvil.BaseDSL.sip;
import static trikita.anvil.BaseDSL.size;
import static trikita.anvil.BaseDSL.textSize;
import static trikita.anvil.BaseDSL.toEndOf;
import static trikita.anvil.DSL.id;
import static trikita.anvil.DSL.imageView;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.relativeLayout;
import static trikita.anvil.DSL.scaleType;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textColor;

public class ContributorsView extends ListView<ContributorsScreen.Presenter, User> {

    public ContributorsView(Context context) {
        super(context);
        onRefresh(presenter::loadContributors);
        onItemClick(presenter::selectContributor);
    }

    @Override
    protected RenderableAdapter.Item<User> onCreateItemView() {
        return (index, item) -> {
            relativeLayout(() -> {
                size(MATCH, dipRes(R.dimen.user_photo_size));
                imageView(() -> {
                    id(R.id.photo);
                    size(dipRes(R.dimen.user_photo_size), dipRes(R.dimen.user_photo_size));
                    scaleType(CENTER_CROP);
                    margin(dipRes(R.dimen.Widget_Margin_Small));
                    picasso(load(item.avatarUrl(), R.drawable.avatar_placeholder));
                });
                linearLayout(() -> {
                    size(MATCH, WRAP);
                    centerVertical();
                    marginEnd(dipRes(R.dimen.Widget_Margin_Medium));
                    toEndOf(R.id.photo);
                    orientation(VERTICAL);
                    textViewListItem(() -> {
                        textColor(R.color.dark_grey);
                        textSize(sip(20));
                        text(item.login());
                    });
                    textViewListItem(() -> {
                        textColor(R.color.dark_grey);
                        textSize(sip(13));
                        text(item.url());
                    });
                });
            });
        };
    }
}
