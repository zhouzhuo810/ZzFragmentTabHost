package me.zhouzhuo.zzfragmenttabhost;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zz on 2016/10/26.
 */
public class ZzFragmentTabHost extends LinearLayout {

    private Context mContext;
    private FragmentManager mFragmentManger;
    private int mContainerId;

    private List<TabInfo> tabInfos = new ArrayList<>();

    private int lastTabPosition;

    private OnTabClickListener listener;
    private boolean mAttached = true;
    private TabInfo mLastTab;

    public ZzFragmentTabHost(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public ZzFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    public ZzFragmentTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
    }

    static class TabInfo {
        final String tag;
        final Class clz;
        Fragment fragment;

        public TabInfo(String tag, Class clz, Fragment fragment) {
            this.tag = tag;
            this.clz = clz;
            this.fragment = fragment;
        }

        @Override
        public String toString() {
            return "TabInfo{" +
                    "tag='" + tag + '\'' +
                    ", clz=" + clz +
                    ", fragment=" + fragment +
                    '}';
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        String currentTab = getCurrentTabTag();

        if (currentTab != null) {

            // Go through all tabs and make sure their fragments match
            // the correct state.
            FragmentTransaction ft = null;
            for (int i = 0; i < tabInfos.size(); i++) {
                TabInfo tab = tabInfos.get(i);
                tab.fragment = mFragmentManger.findFragmentByTag(tab.tag);
                if (tab.fragment != null && !tab.fragment.isDetached()) {
                    if (tab.tag.equals(currentTab)) {
                        // The fragment for this tab is already there and
                        // active, and it is what we really want to have
                        // as the current tab.  Nothing to do.
                        mLastTab = tab;
                    } else {
                        // This fragment was restored in the active state,
                        // but is not the current tab.  Deactivate it.
                        if (ft == null) {
                            ft = mFragmentManger.beginTransaction();
                        }
                        ft.detach(tab.fragment);
                    }
                }
            }

            // We are now ready to go.  Make sure we are switched to the
            // correct tab.
            mAttached = true;
            ft = doTabChanged(currentTab, ft);
            if (ft != null) {
                ft.commit();
                mFragmentManger.executePendingTransactions();
            }
        }

    }

    private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo newTab = null;
        for (int i = 0; i < tabInfos.size(); i++) {
            TabInfo tab = tabInfos.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManger.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    Log.e("ttt", "two");
                    newTab.fragment = Fragment.instantiate(mContext,
                            newTab.clz.getName(), null);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }

            mLastTab = newTab;
        }
        return ft;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }


    public String getCurrentTabTag() {
        Log.e("xx", tabInfos.toString());
        if (lastTabPosition >= 0 && lastTabPosition < tabInfos.size()) {
            return tabInfos.get(lastTabPosition).tag;
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public ZzFragmentTabHost initTabsWithCount(int count) {
        for (int i = 0; i < count; i++) {
            ZzTab tab = new ZzTab(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            tab.setLayoutParams(layoutParams);
            addView(tab);
        }
        return this;
    }

    public ZzFragmentTabHost setUp(Context context, FragmentManager fragmentManager, int mContainerId) {
        mContext = context;
        mFragmentManger = fragmentManager;
        this.mContainerId = mContainerId;
        return this;
    }

    @SafeVarargs
    public final ZzFragmentTabHost setFragmentClazz(Class<? extends Fragment>... fgm) {
        for (Class<? extends Fragment> aClass : fgm) {
            TabInfo tabInfo = new TabInfo(aClass.getName(), aClass, null);
            tabInfos.add(tabInfo);
        }
        return this;
    }


    public ZzFragmentTabHost setImagePressIds(int... ids) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setImgPress(ids[j]);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setImageNormalIds(int... ids) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setImgNormal(ids[j]);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabNameWithResId(int arrayId) {
        String[] array = getContext().getResources().getStringArray(arrayId);
        setTabName(array);
        return this;
    }

    public ZzFragmentTabHost setTabName(String... names) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setTabText(names[j]);
            }
        }
        return this;
    }

    public ZzFragmentTabHost showMarkAll() {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setShowMark(true);
            }
        }
        return this;
    }

    public ZzFragmentTabHost hideMarkAll() {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setShowMark(false);
            }
        }
        return this;
    }

    public ZzFragmentTabHost showMarkAt(int position) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setShowMark(true);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost hideMarkAt(int position) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setShowMark(false);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkMaxValueAll(int max) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkMaxNumber(max);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkMaxValueAt(int position, int max) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setMarkMaxNumber(max);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkBackgroundColorResIdAt(int position, int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setMarkBgColor(color);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkBackgroundColorResIdAll(int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkBgColor(color);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkBackgroundColorValueAt(int position, int colorValue) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setMarkBgColor(colorValue);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkBackgroundColorValueAll(int colorValue) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkBgColor(colorValue);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkNumberColorValueAll(int colorValue) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkNumberColor(colorValue);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkNumberColorValueAt(int position, int colorValue) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setMarkNumberColor(colorValue);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkNumberColorResIdAll(int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkNumberColor(color);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkNumberColorResIdAt(int position, int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setMarkNumberColor(color);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkNumberAll(int number) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkNumber(number);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkNumberAt(int position, int number) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setMarkNumber(number);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkNumberSizeAll(int px) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkNumberSize(px);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkWidthAll(int px) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkWidth(px);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkHeightAll(int px) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setMarkHeight(px);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setMarkNumberSizeAt(int position, int px) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setMarkNumberSize(px);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabTextSizeNormalAll(int px) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setTextSizeNormal(px);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabTextSizeNormalAt(int position, int px) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setTextSizeNormal(px);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabTextSizeSelectedAll(int px) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setTextSizePress(px);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabTextSizeSelectedAt(int position, int px) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setTextSizePress(px);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabTextColorResIdNormalAll(int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setTextColorNormal(color);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabTextColorResIdNormalAt(int position, int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setTextColorNormal(color);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabTextColorResIdSelectedAll(int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setTextColorPress(color);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabTextColorResIdSelectedAt(int position, int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setTextColorPress(color);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabImgSizeNormalAll(int px) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setImgSizeNormal(px);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabImgSizeNormalAt(int position, int px) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setImgSizeNormal(px);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabImgSizeSelectedAll(int px) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setImgSizePress(px);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabImgSizeSelectedAt(int position, int px) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setImgSizePress(px);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabBgColorValueNormalAll(int colorValue) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setBgColorNormal(colorValue);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabBgColorValueNormalAt(int position, int colorValue) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setBgColorNormal(colorValue);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabBgColorResIdNormalAll(int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setBgColorNormal(color);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabBgColorResIdNormalAt(int position, int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setBgColorNormal(color);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabBgColorValueSelectedAll(int colorValue) {
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setBgColorPress(colorValue);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabBgColorValueSelectedAt(int position, int colorValue) {
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setBgColorPress(colorValue);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabBgColorResIdSelectedAll(int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                ((ZzTab) child).setBgColorPress(color);
            }
        }
        return this;
    }

    public ZzFragmentTabHost setTabBgColorResIdSelectedAt(int position, int colorResId) {
        int color = ContextCompat.getColor(getContext(), colorResId);
        int pos = 0;
        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);
            if (child instanceof ZzTab) {
                if (pos == position) {
                    ((ZzTab) child).setBgColorPress(color);
                }
                pos++;
            }
        }
        return this;
    }

    public ZzFragmentTabHost setOnTabClickListener(final OnTabClickListener listener) {
        this.listener = listener;
        final int count = getChildCount();
        Log.e("xxx", "count = " + count);
        int pos = 0;
        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i);
            if (view instanceof ZzTab) {
                final int finalI = pos;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((ZzTab) view).isSelected()) {
                            for (int j = 0; j < count; j++) {
                                final View child = getChildAt(j);
                                if (child instanceof ZzTab) {
                                    ((ZzTab) child).setSelected(false);
                                }
                            }
                            setSelection(finalI);
                            ((ZzTab) view).setSelected(true);
                            if (listener != null) {
                                listener.onTabClick((ZzTab) view, true, finalI);
                            }
                        } else {
                            if (listener != null) {
                                listener.onTabClick((ZzTab) view, false, finalI);
                            }
                        }
                    }
                });
                pos++;
            }
        }
        return this;
    }

    public void setSelection(int position) {
        lastTabPosition = position;
        int pos = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof ZzTab) {
                final int finalI = pos;
                if (finalI == position) {
                    if (!((ZzTab) view).isSelected()) {
                        for (int j = 0; j < getChildCount(); j++) {
                            final View child = getChildAt(j);
                            if (child instanceof ZzTab) {
                                ((ZzTab) child).setSelected(false);
                            }
                        }
                        ((ZzTab) view).setSelected(true);
                        if (mAttached && tabInfos != null && tabInfos.size() > 0) {
                            Log.e("ttt", "one");
                            FragmentTransaction ft = doTabChanged(tabInfos.get(finalI).tag, null);
                            if (ft != null) {
                                ft.commit();
                            }
                        }
                        if (listener != null) {
                            listener.onTabClick((ZzTab) view, true, finalI);
                        }
                    } else {
                        if (listener != null) {
                            listener.onTabClick((ZzTab) view, false, finalI);
                        }
                    }
                }
                pos++;
            }
        }
        if (tabInfos.size() > 0) {
            TabInfo tabInfo = tabInfos.get(position);
            if (tabInfo.fragment == null) {
                try {
                    tabInfo.fragment = (Fragment) tabInfo.clz.newInstance();
                    doTabChanged(tabInfo.tag, mFragmentManger.beginTransaction());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.curTab = getCurrentTabTag();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentTabByTag(ss.curTab);
    }


    static class SavedState extends BaseSavedState {
        String curTab;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            curTab = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(curTab);
        }

        @Override
        public String toString() {
            return "SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " curTab=" + curTab + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void setCurrentTabByTag(String tag) {
        int i;
        for (i = 0; i < tabInfos.size(); i++) {
            if (tabInfos.get(i).tag.equals(tag)) {
                setSelection(i);
                break;
            }
        }
    }

    public int getSelection() {
        return lastTabPosition;
    }

    public interface OnTabClickListener {
        void onTabClick(ZzTab tab, boolean changed, int position);
    }

}
