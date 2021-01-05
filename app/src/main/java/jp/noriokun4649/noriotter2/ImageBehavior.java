package jp.noriokun4649.noriotter2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

public class ImageBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private boolean mIsAnimating = false;

    public ImageBehavior(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NotNull final CoordinatorLayout parent, @NotNull final ImageView bottomBar, @NotNull final View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NotNull final CoordinatorLayout parent, @NotNull final ImageView bottomBar, @NotNull final View dependency) {
        if (!mIsAnimating) {
            if (dependency.getTop() < -140) {
                ViewCompat.animate(bottomBar).scaleX(0.0F).scaleY(0.0F).alpha(0.0F)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(final View view) {
                                mIsAnimating = true;
                            }

                            @Override
                            public void onAnimationEnd(final View view) {
                                mIsAnimating = false;
                            }

                            @Override
                            public void onAnimationCancel(final View view) {
                                mIsAnimating = false;
                            }
                        });
            } else {
                ViewCompat.animate(bottomBar).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
                        .setInterpolator(new FastOutSlowInInterpolator());
            }
        }
        bottomBar.setTranslationY(dependency.getTop());
        return true;
    }

}