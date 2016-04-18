package io.techery.sample.utils;

import android.support.annotation.DimenRes;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.View;
import android.view.ViewGroup;

import trikita.anvil.Anvil;
import trikita.anvil.BaseDSL;

import static trikita.anvil.BaseDSL.attr;

public class AnvilDSL {

    public static int dipRes(@DimenRes int res) {
        return BaseDSL.R().getDimensionPixelSize(res);
    }

    public static Void marginEnd(int w) {
        return attr(MarginEndFunc.instance, w);
    }

    private static final class MarginEndFunc implements Anvil.AttrFunc<Integer> {
        private static final MarginEndFunc instance = new MarginEndFunc();

        @Override
        public void apply(View v, Integer newValue, Integer oldValue) {
            MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams) v.getLayoutParams(), newValue);
        }
    }
}
