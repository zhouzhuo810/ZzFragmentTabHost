package me.zhouzhuo.zzfragmenttabhost;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zz on 2016/10/26.
 */
public class ZzTab extends FrameLayout {

    private int textSizePress;
    private int textSizeNormal;
    private int textColorPress;
    private int textColorNormal;
    private int bgColorPress;
    private int bgColorNormal;
    private boolean showMark;
    private int markNumber;
    private int markBgColor;
    private int markNumberSize;
    private int markWidth;
    private int markHeight;
    private int markNumberColor;
    private int markMaxNumber;

    private String tabText;

    private int imgPress;
    private int imgNormal;

    private int imgSizePress;
    private int imgSizeNormal;

    private View rootView;
    private TextView tvTabName;
    private ImageView ivTabIcon;
    private ZzMarkView zzMarkView;

    private boolean selected = false;

    public ZzTab(Context context) {
        super(context);
        init(context, null);
    }

    public ZzTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZzTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initLayouts(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZzTab);
        imgPress = a.getResourceId(R.styleable.ZzTab_zt_tab_img_selected, android.R.color.transparent);
        imgNormal = a.getResourceId(R.styleable.ZzTab_zt_tab_img_normal, android.R.color.transparent);
        tabText = a.getString(R.styleable.ZzTab_zt_tab_text);
        markNumber = a.getInteger(R.styleable.ZzTab_zt_mark_default_number, 0);
        markNumberSize = a.getDimensionPixelSize(R.styleable.ZzTab_zt_mark_text_size, 24);
        markBgColor = a.getColor(R.styleable.ZzTab_zt_mark_bg_color, Color.RED);
        markNumberColor = a.getColor(R.styleable.ZzTab_zt_mark_text_color, Color.WHITE);
        markMaxNumber = a.getInteger(R.styleable.ZzTab_zt_mark_max_number, 99);
        showMark = a.getBoolean(R.styleable.ZzTab_zt_show_mark, false);
        selected = a.getBoolean(R.styleable.ZzTab_zt_selected, false);
        textSizeNormal = a.getDimensionPixelSize(R.styleable.ZzTab_zt_text_size_normal, 30);
        textSizePress = a.getDimensionPixelSize(R.styleable.ZzTab_zt_text_size_selected, 30);
        textColorPress = a.getColor(R.styleable.ZzTab_zt_text_color_selected, Color.BLACK);
        textColorNormal = a.getColor(R.styleable.ZzTab_zt_text_color_normal, Color.BLACK);
        bgColorPress = a.getColor(R.styleable.ZzTab_zt_bg_color_selected, Color.TRANSPARENT);
        bgColorNormal = a.getColor(R.styleable.ZzTab_zt_bg_color_normal, Color.TRANSPARENT);
        imgSizeNormal = a.getDimensionPixelSize(R.styleable.ZzTab_zt_img_size_normal, 70);
        imgSizePress = a.getDimensionPixelSize(R.styleable.ZzTab_zt_img_size_selected, 70);

        a.recycle();
    }

    private void initLayouts(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.zz_tab_layout, null);

        tvTabName = (TextView) rootView.findViewById(R.id.tv_tab);
        ivTabIcon = (ImageView) rootView.findViewById(R.id.iv_tab);
        zzMarkView = (ZzMarkView) rootView.findViewById(R.id.zz_mark_view);
        zzMarkView.setMarkNumber(markNumber);
        zzMarkView.setMarkNumberSize(markNumberSize);
        zzMarkView.setMarkBgColor(markBgColor);
        zzMarkView.setMarkNumberColor(markNumberColor);
        zzMarkView.setVisibility(showMark ? VISIBLE : INVISIBLE);
        tvTabName.setText(tabText);
        tvTabName.setTextSize(TypedValue.COMPLEX_UNIT_PX, selected ? textSizePress : textSizeNormal);
        tvTabName.setTextColor(selected ? textColorPress : textColorNormal);
        ivTabIcon.setImageResource(selected ? imgPress : imgNormal);
        ivTabIcon.getLayoutParams().width = selected ? imgSizePress : imgSizeNormal;
        ivTabIcon.getLayoutParams().height = selected ? imgSizePress : imgSizeNormal;
        rootView.setBackgroundColor(selected ? bgColorPress : bgColorNormal);
        addView(rootView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    public void setTextSizePress(int textSizePress) {
        this.textSizePress = textSizePress;
        tvTabName.setTextSize(TypedValue.COMPLEX_UNIT_PX, selected ? textSizePress : textSizeNormal);
    }

    public void setTextSizeNormal(int textSizeNormal) {
        this.textSizeNormal = textSizeNormal;
        tvTabName.setTextSize(TypedValue.COMPLEX_UNIT_PX, selected ? textSizePress : textSizeNormal);
    }

    public void setTextColorPress(int textColorPress) {
        this.textColorPress = textColorPress;
        tvTabName.setTextColor(selected ? textColorPress : textColorNormal);
    }

    public void setTextColorNormal(int textColorNormal) {
        this.textColorNormal = textColorNormal;
        tvTabName.setTextColor(selected ? textColorPress : textColorNormal);
    }

    public void setBgColorPress(int bgColorPress) {
        this.bgColorPress = bgColorPress;
        rootView.setBackgroundColor(selected ? bgColorPress : bgColorNormal);
    }

    public void setBgColorNormal(int bgColorNormal) {
        this.bgColorNormal = bgColorNormal;
        rootView.setBackgroundColor(selected ? bgColorPress : bgColorNormal);
    }

    public void setShowMark(boolean showMark) {
        this.showMark = showMark;
        zzMarkView.setVisibility(showMark ? VISIBLE : INVISIBLE);
    }

    public void setMarkNumber(int markNumber) {
        this.markNumber = markNumber;
        zzMarkView.setMarkNumber(markNumber);
    }

    public void setMarkBgColor(int markBgColor) {
        this.markBgColor = markBgColor;
        zzMarkView.setMarkBgColor(markBgColor);
    }

    public void setMarkNumberSize(int markNumberSize) {
        this.markNumberSize = markNumberSize;
        zzMarkView.setMarkNumberSize(markNumberSize);
    }

    public void setMarkWidth(int markWidth) {
        this.markWidth = markWidth;
        zzMarkView.setMarkWidth(markWidth);
    }
    public void setMarkHeight(int markHeight) {
        this.markHeight = markHeight;
        zzMarkView.setMarkHeight(markHeight);
    }

    public void setMarkNumberColor(int markNumberColor) {
        this.markNumberColor = markNumberColor;
        zzMarkView.setMarkNumberColor(markNumberColor);
    }

    public void setTabText(String tabText) {
        this.tabText = tabText;
        tvTabName.setText(tabText);
    }

    public void setMarkMaxNumber(int markMaxNumber) {
        this.markMaxNumber = markMaxNumber;
        zzMarkView.setMaxNumber(markMaxNumber);
    }

    public void setImgPress(int imgPress) {
        this.imgPress = imgPress;
        ivTabIcon.setImageResource(selected ? imgPress : imgNormal);
    }

    public void setImgNormal(int imgNormal) {
        this.imgNormal = imgNormal;
        ivTabIcon.setImageResource(selected ? imgPress : imgNormal);
    }

    public void setImgSizePress(int imgSizePress) {
        this.imgSizePress = imgSizePress;
        ivTabIcon.getLayoutParams().width = selected ? imgSizePress : imgSizeNormal;
        ivTabIcon.getLayoutParams().height = selected ? imgSizePress : imgSizeNormal;
    }

    public void setImgSizeNormal(int imgSizeNormal) {
        this.imgSizeNormal = imgSizeNormal;
        ivTabIcon.getLayoutParams().width = selected ? imgSizePress : imgSizeNormal;
        ivTabIcon.getLayoutParams().height = selected ? imgSizePress : imgSizeNormal;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        ivTabIcon.setImageResource(selected ? imgPress : imgNormal);
        rootView.setBackgroundColor(selected ? bgColorPress : bgColorNormal);
        tvTabName.setTextSize(TypedValue.COMPLEX_UNIT_PX, selected ? textSizePress : textSizeNormal);
        tvTabName.setTextColor(selected ? textColorPress : textColorNormal);
        ivTabIcon.getLayoutParams().width = selected ? imgSizePress : imgSizeNormal;
        ivTabIcon.getLayoutParams().height = selected ? imgSizePress : imgSizeNormal;
    }


    @Override
    public boolean isSelected() {
        return selected;
    }

    public boolean isShowMark() {
        return showMark;
    }
}

