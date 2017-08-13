package com.gdg.homework;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private CircleImageView mAvatarImageView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mNameTextView;
    private Toolbar mToolbar;
    private TextView mNickNameView;
    private AppBarLayout mAppBarLayout;
    private TextView mTelegramView;

    private DipConverter mConverter;
    private final ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    private int mAppBarMaxHeight;
    private int mAppBarMinHeight;
    private int mPreviousVerticalOffset;

    // Стартовый цвет хедера
    private int mGradientStartColor;

    // Конечный цвет хедера
    private int mGradientEndColor;

    // Стандартный левый отступ имени
    // Он используется когда тулбар полность раскрыт
    private int mNameMaxLeftMargin;

    // Минимальный левый отступ
    // Он используется когда тулбар полность схлопнут
    private int mNameMinLeftMargin;

    // Верхний минимальный отступ имени
    // Используется когда тулбар полностью схлопнут
    private int mNameTopMinMargin;

    // Верхний максимальный отступ имени
    // Используется когда тулбар полностью раскрыт
    private int mNameTopMaxMargin;

    // Дефолтный маргин для аватарки
    // Используется для паралакс эффекта
    private int mDefaultAvatarTopMargin;

    // Дефолтный маргин для аватарки
    // Используется для паралакс эффекта
    private int mDefaultNickNameTopMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(this);

        mAvatarImageView = (CircleImageView) findViewById(R.id.profile_image);
        mNameTextView = (TextView) findViewById(R.id.name);
        mNickNameView = (TextView) findViewById(R.id.nickname);

        mTelegramView = (TextView) findViewById(R.id.telegram_action);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mConverter = new DipConverter(getApplicationContext());

        mGradientStartColor = ContextCompat.getColor(this, R.color.header_gradient_start_color);
        mGradientEndColor = ContextCompat.getColor(this, R.color.header_gradient_end_color);

        mAppBarLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mAppBarLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                mAppBarMinHeight = mToolbar.getHeight();
                mAppBarMaxHeight = mAppBarLayout.getHeight();
                mDefaultAvatarTopMargin = ((ViewGroup.MarginLayoutParams)
                        mAvatarImageView.getLayoutParams()).topMargin;
                mDefaultNickNameTopMargin =
                        ((ViewGroup.MarginLayoutParams) mNickNameView.getLayoutParams()).topMargin;
                calculateAndSetNameDefaultMargin();
                initOtherNameMargins();
                return true;
            }
        });
        initTelegramView();
    }

    private void initTelegramView() {
        mTelegramView.setClickable(true);
        mTelegramView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("tg://resolve?domain=vkirillov"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        SpannableString spannableString = new SpannableString(mTelegramView.getText());
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {}
        }, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTelegramView.setText(spannableString);
    }

    private void calculateAndSetNameDefaultMargin() {
        int displayWidth = AndroidUtils.getDisplayWidthInPx(this);
        int nameViewWidth = mNameTextView.getWidth();
        mNameMaxLeftMargin = displayWidth / 2 - nameViewWidth / 2;
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mNameTextView.getLayoutParams();
        layoutParams.leftMargin = mNameMaxLeftMargin;
        mNameTextView.setLayoutParams(layoutParams);
    }

    private void initOtherNameMargins() {
        ViewGroup.MarginLayoutParams nameLayoutParams =
                (ViewGroup.MarginLayoutParams) mNameTextView.getLayoutParams();
        mNameTopMaxMargin = nameLayoutParams.topMargin;
        mNameTopMinMargin = AndroidUtils.isPortraitOrientation(this) ?
                mConverter.dpToPx(15) : mConverter.dpToPx(12);
        mNameMinLeftMargin = mConverter.dpToPx(72);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mPreviousVerticalOffset == verticalOffset) {
            return;
        }
        mPreviousVerticalOffset = verticalOffset;

        // Берем смещение по модулю
        float absOffset = Math.abs(verticalOffset);
        // Считаем процент на сколько схлопнут тулбар
        // 1.0 - тулбар полностью схлопнут
        // 0.0 - тулбар полность раскрыт
        float collapsingPercent = (absOffset) / (mAppBarMaxHeight - mAppBarMinHeight);

        updateGradientDrawable(collapsingPercent);
        updateNameMargins(collapsingPercent, absOffset);
        parallaxAndFade(collapsingPercent, absOffset);
    }

    private void updateGradientDrawable(float collapsingPercent) {
        float fraction = 1 - collapsingPercent;
        int newEndColor = (int) mArgbEvaluator.evaluate(fraction,
                mGradientStartColor, mGradientEndColor);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{mGradientStartColor, newEndColor});
        mCollapsingToolbarLayout.setBackground(gradientDrawable);
    }

    private void updateNameMargins(float collapsingPercent, float absOffset) {
        float fraction = 1 - collapsingPercent;
        float topMargin = absOffset + mNameTopMinMargin + (mNameTopMaxMargin -
                mNameTopMinMargin) * fraction;

        float leftMargin = mNameMinLeftMargin + (mNameMaxLeftMargin -
                mNameMinLeftMargin) * fraction;

        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mNameTextView.getLayoutParams();
        layoutParams.topMargin = (int) topMargin;
        layoutParams.leftMargin = (int) leftMargin;
        mNameTextView.setLayoutParams(layoutParams);
    }

    private void parallaxAndFade(float collapsingPercent, float absOffset) {
        float fraction = 1 - collapsingPercent;
        mAvatarImageView.setAlpha(fraction);
        mNickNameView.setAlpha(fraction);

        float avatarTopMargin = mDefaultAvatarTopMargin + absOffset / 2;
        ViewGroup.MarginLayoutParams avatarLayoutParams =
                (ViewGroup.MarginLayoutParams) mAvatarImageView.getLayoutParams();
        avatarLayoutParams.topMargin = (int) avatarTopMargin;
        mAvatarImageView.setLayoutParams(avatarLayoutParams);

        float nickNameTopMargin = mDefaultNickNameTopMargin + absOffset / 2;
        ViewGroup.MarginLayoutParams nickNameLayoutParams =
                (ViewGroup.MarginLayoutParams) mNickNameView.getLayoutParams();
        nickNameLayoutParams.topMargin = (int) nickNameTopMargin;
        mNickNameView.setLayoutParams(nickNameLayoutParams);
    }
}
