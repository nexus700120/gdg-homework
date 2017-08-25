package com.gdg.crypto.currencies.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.gdg.crypto.currencies.R;
import com.gdg.crypto.currencies.ext.NumbersExtKt;

/**
 * Created by Vitaly Kiriilov on 27.06.2016.
 */
public class SmallButton extends AppCompatTextView {

    public SmallButton(Context context) {
        this(context, null);
    }

    public SmallButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int textColor = Color.BLACK;
        Drawable background = null;
        int textSize = -1;
        int gravity = -1;
        int drawableLeftColor = Color.BLACK;
        int padding = -1;

        if (attrs != null) {
            TypedArray textColorTypedArray = getContext()
                    .obtainStyledAttributes(attrs, new int[]{android.R.attr.textColor});
            textColor = textColorTypedArray.getColor(0, Color.BLACK);
            textColorTypedArray.recycle();

            TypedArray textSizeTypedArray = getContext()
                    .obtainStyledAttributes(attrs, new int[]{android.R.attr.textSize});
            textSize = (int) textSizeTypedArray.getDimension(0, -1);
            textSizeTypedArray.recycle();

            TypedArray textGravityTypedArray = getContext()
                    .obtainStyledAttributes(attrs, new int[]{android.R.attr.gravity});
            gravity = textGravityTypedArray.getInt(0, -1);
            textGravityTypedArray.recycle();

            TypedArray textBackgroundTypedArray = getContext()
                    .obtainStyledAttributes(attrs, new int[]{android.R.attr.background});
            background = textBackgroundTypedArray.getDrawable(0);
            textBackgroundTypedArray.recycle();

            TypedArray paddingTypedArray = getContext()
                    .obtainStyledAttributes(attrs, new int[]{android.R.attr.padding});
            padding = paddingTypedArray.getDimensionPixelOffset(0, -1);
            paddingTypedArray.recycle();

            TypedArray custom = getContext().obtainStyledAttributes(attrs, R.styleable.SmallButton);
            drawableLeftColor = custom.getColor(R.styleable.SmallButton_drawableLeftColor,
                    drawableLeftColor);
            custom.recycle();
        }

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F);

        setGravity(gravity > 0 ? gravity : Gravity.CENTER);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextColor(textColor);

        setClickable(true);
        setAllCaps(true);
        if (isInEditMode()) {
            if (background != null) {
                setBackground(background);
                return;
            }
            setBackgroundResource(R.color.white_30);
        } else if (background != null) {
            setBackground(background);
        } else {
            setBackgroundResource(R.drawable.small_btn_background);
        }

        setHeight(NumbersExtKt.toPx(36));

        Drawable[] drawables = getCompoundDrawables();
        if (drawables[0] != null) {
            drawables[0].setColorFilter(drawableLeftColor, PorterDuff.Mode.SRC_ATOP);
        }
        int internalPadding = padding >= 0 ? padding : NumbersExtKt.toPx(8);
        setPadding(internalPadding, 0, internalPadding, 0);
    }
}
