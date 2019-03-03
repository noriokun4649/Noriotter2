package jp.noriokun4649.noriotter2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class ImageBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private boolean mIsAnimating = false;
    private int defaultDependencyTop = 1;

    public ImageBehavior(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NotNull final CoordinatorLayout parent, @NotNull final ImageView bottomBar, @NotNull final View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NotNull final CoordinatorLayout parent, @NotNull final ImageView bottomBar, @NotNull final View dependency) {
        if (defaultDependencyTop == 1) {
            defaultDependencyTop = -dependency.getTop();
        }
        if (defaultDependencyTop < 5) {
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
        }
        bottomBar.setTranslationY(dependency.getTop() + defaultDependencyTop);
        return true;
    }

}