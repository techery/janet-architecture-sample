package io.techery.sample.ui;

import android.text.TextUtils.TruncateAt;

import io.techery.sample.R;
import trikita.anvil.Anvil;

import static trikita.anvil.DSL.MATCH;
import static trikita.anvil.DSL.WRAP;
import static trikita.anvil.DSL.ellipsize;
import static trikita.anvil.DSL.maxLines;
import static trikita.anvil.DSL.singleLine;
import static trikita.anvil.DSL.size;
import static trikita.anvil.DSL.textColor;
import static trikita.anvil.DSL.textView;

public class Style {

    public static Void textViewListItem() {
        return textViewListItem(() -> {
        });
    }

    public static Void textViewListItem(Anvil.Renderable r) {
        return textView(() -> {
            size(MATCH, WRAP);
            textColor(R.color.light_grey);
            maxLines(1);
            singleLine(true);
            ellipsize(TruncateAt.END);
            r.view();
        });
    }
}
