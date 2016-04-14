package io.techery.sample.utils;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import trikita.anvil.Anvil;

import static trikita.anvil.BaseDSL.attr;

public class PicassoLoadDSL {

    public static Void picasso(Request request) {
        return attr(PicassoImageUrlFunc.instance, request);
    }

    public static Request load(String url, @DrawableRes int placeholder) {
        return new Request(url, placeholder);
    }

    public static Request load(String url) {
        return new Request(url, 0);
    }

    private static final class PicassoImageUrlFunc implements Anvil.AttrFunc<Request> {
        private static final PicassoImageUrlFunc instance = new PicassoImageUrlFunc();

        public void apply(View v, final Request arg, final Request old) {
            if (v instanceof ImageView) {
                Picasso.with(v.getContext())
                        .load(arg.url)
                        .placeholder(arg.placeholder)
                        .into((ImageView) v);
            }
        }
    }

    public static class Request {
        private String url;
        private int placeholder;

        private Request(String url, int placeholder) {
            this.url = url;
            this.placeholder = placeholder;
        }
    }
}
