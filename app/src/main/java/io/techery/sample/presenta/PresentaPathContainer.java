package io.techery.sample.presenta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.Map;

import flow.Flow;
import flow.path.Path;
import flow.path.PathContainer;
import flow.path.PathContext;
import flow.path.PathContextFactory;
import io.techery.presenta.flow.Utils;

import static flow.Flow.Direction.REPLACE;

public abstract class PresentaPathContainer extends PathContainer {

    private static final Map<Class, View> PATH_LAYOUT_CACHE = new LinkedHashMap<>();

    private final PathContextFactory contextFactory;

    protected PresentaPathContainer(int tagKey, PathContextFactory contextFactory) {
        super(tagKey);
        this.contextFactory = contextFactory;
    }

    @Override
    protected void performTraversal(final ViewGroup containerView,
                                    final TraversalState traversalState, final Flow.Direction direction,
                                    final Flow.TraversalCallback callback) {

        final PathContext context;
        final PathContext oldPath;
        if (containerView.getChildCount() > 0) {
            oldPath = PathContext.get(containerView.getChildAt(0).getContext());
        } else {
            oldPath = PathContext.root(containerView.getContext());
        }

        Path to = traversalState.toPath();

        context = PathContext.create(oldPath, to, contextFactory);

        View newView = getLayout(context, to);

        View fromView = null;
        if (traversalState.fromPath() != null) {
            fromView = containerView.getChildAt(0);
            traversalState.saveViewState(fromView);
        }
        traversalState.restoreViewState(newView);

        if (fromView == null || direction == REPLACE) {
            containerView.removeAllViews();
            containerView.addView(newView);
            oldPath.destroyNotIn(context, contextFactory);
            callback.onTraversalCompleted();
        } else {
            containerView.addView(newView);
            final View finalFromView = fromView;
            Utils.waitForMeasure(newView, new Utils.OnMeasuredCallback() {
                @Override
                public void onMeasured(View view, int width, int height) {
                    runAnimation(containerView, finalFromView, view, direction, new Flow.TraversalCallback() {
                        @Override
                        public void onTraversalCompleted() {
                            containerView.removeView(finalFromView);
                            oldPath.destroyNotIn(context, contextFactory);
                            callback.onTraversalCompleted();
                        }
                    });
                }
            });
        }
    }

    protected abstract View getLayout(Context context, Path path);

    private void runAnimation(final ViewGroup container, final View from, final View to,
                              Flow.Direction direction, final Flow.TraversalCallback callback) {
        Animator animator = createSegue(from, to, direction);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.removeView(from);
                callback.onTraversalCompleted();
            }
        });
        animator.start();
    }

    private Animator createSegue(View from, View to, Flow.Direction direction) {
        boolean backward = direction == Flow.Direction.BACKWARD;
        int fromTranslation = backward ? from.getWidth() : -from.getWidth();
        int toTranslation = backward ? -to.getWidth() : to.getWidth();

        AnimatorSet set = new AnimatorSet();

        set.play(ObjectAnimator.ofFloat(from, View.TRANSLATION_X, fromTranslation));
        set.play(ObjectAnimator.ofFloat(to, View.TRANSLATION_X, toTranslation, 0));

        return set;
    }
}
