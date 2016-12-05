package me.zhouzhuo.zzfragmenttabhostdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import me.zhouzhuo.zzandroidutils.display.DisplayUtil;
import me.zhouzhuo.zzfragmenttabhost.ZzFragmentTabHost;
import me.zhouzhuo.zzfragmenttabhost.ZzTab;

/**
 * Created by zz on 2016/10/27.
 */

public class SecondActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        DisplayUtil.scaleContentView((ViewGroup) findViewById(R.id.act_second));

        ZzFragmentTabHost zft = (ZzFragmentTabHost) findViewById(R.id.zft);
        zft.setUp(this, getSupportFragmentManager(), R.id.fgm_container)
                .initTabsWithCount(4)
                .setFragmentClazz(
                        FragmentOne.class,
                        FragmentTwo.class,
                        FragmentThree.class,
                        FragmentFour.class)
                .setTabNameWithResId(R.array.tab_names)
                .setImagePressIds(
                        R.drawable.mainicon_checked,
                        R.drawable.mainicon_checked,
                        R.drawable.mainicon_checked,
                        R.drawable.mainicon_checked)
                .setImageNormalIds(
                        R.drawable.mainicon_normal,
                        R.drawable.mainicon_normal,
                        R.drawable.mainicon_normal,
                        R.drawable.mainicon_normal)
                .setTabTextSizeNormalAll(DisplayUtil.scaleValue(this, 24))
                .setTabTextSizeSelectedAll(DisplayUtil.scaleValue(this, 24))
                .setTabImgSizeNormalAll(DisplayUtil.scaleValue(this, 50))
                .setTabImgSizeSelectedAll(DisplayUtil.scaleValue(this, 50))
                .setTabTextColorResIdNormalAll(R.color.colorTabGray)
                .setTabTextColorResIdSelectedAll(R.color.colorTabMain)
                .setMarkNumberSizeAll(DisplayUtil.scaleValue(this, 20))
                .setMarkWidthAll(DisplayUtil.scaleValue(this, 50))
                .setMarkHeightAll(DisplayUtil.scaleValue(this, 40))
                .setMarkNumberAt(0, 100)
                .showMarkAt(0)
                .setOnTabClickListener(new ZzFragmentTabHost.OnTabClickListener() {
                    @Override
                    public void onTabClick(ZzTab tab, boolean changed, int position) {

                    }
                })
                .setSelection(0);

    }
}
