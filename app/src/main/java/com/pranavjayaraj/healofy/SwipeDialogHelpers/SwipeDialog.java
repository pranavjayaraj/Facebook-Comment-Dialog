package com.pranavjayaraj.healofy.SwipeDialogHelpers;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.DialogFragment;

import com.pranavjayaraj.healofy.SwipeDialogHelpers.DismissOnSwipeListener;
import com.pranavjayaraj.healofy.SwipeDialogHelpers.SwipeableFrameLayout;

public class SwipeDialog extends DialogFragment {

    private boolean mSwipeable = true;
    private boolean mTiltEnabled = true;
    private boolean mSwipeLayoutGenerated = false;
    private DismissOnSwipeListener mListener = null;

    /**
     * Set whether dialog can be swiped away.
     */
    public void setSwipeable(boolean swipeable) {
        mSwipeable = swipeable;
    }

    /**
     * Get whether dialog can be swiped away.
     */
    public boolean isSwipeable() {
        return mSwipeable;
    }

    /**
     * Set whether tilt effect is enabled on swiping.
     */
    public void setTiltEnabled(boolean tiltEnabled) {
        mTiltEnabled = tiltEnabled;
        if (mListener != null) {
            mListener.setTiltEnabled(tiltEnabled);
        }
    }

    /**
     * Get whether tilt effect is enabled on swiping.
     */
    public boolean isTiltEnabled() {
        return mTiltEnabled;
    }

    /**
     * Called when dialog is swiped away to dismiss.
     * @return true to prevent dismissing
     */
    public boolean onSwipedAway(boolean totop) {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mSwipeLayoutGenerated && getShowsDialog()) {
            Window window = getDialog().getWindow();
            ViewGroup decorView = (ViewGroup)window.getDecorView();
            View content = decorView.getChildAt(0);
            decorView.removeView(content);

            SwipeableFrameLayout layout = new SwipeableFrameLayout(getActivity());
            layout.addView(content);
            decorView.addView(layout);

            mListener = new DismissOnSwipeListener(decorView, "layout", new DismissOnSwipeListener.DismissCallbacks() {
                @Override
                public boolean canDismiss(Object token) {
                    return isCancelable() && mSwipeable;
                }

                @Override
                public void onDismiss(View view, boolean totop, Object token) {
                    if (!onSwipedAway(totop)) {
                        dismiss();
                    }
                }
            });
            mListener.setTiltEnabled(mTiltEnabled);
            layout.setSwipeDismissTouchListener(mListener);
            layout.setOnTouchListener(mListener);
            layout.setClickable(true);
            mSwipeLayoutGenerated = true;
        }
    }

}
