package com.gdg.homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Vitaly Kiriilov on 12.08.2017.
 */

public class DipConverter {

    private DisplayMetrics mDisplayMetrics;

    public DipConverter(@NonNull Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    public int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mDisplayMetrics);
    }
}
