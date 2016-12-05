package me.zhouzhuo.zzfragmenttabhostdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import me.zhouzhuo.zzfragmenttabhost.ZzFragmentTabHost;
import me.zhouzhuo.zzfragmenttabhost.ZzTab;

public class MainActivity extends AppCompatActivity {

    private ZzFragmentTabHost zth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        zth = (ZzFragmentTabHost) findViewById(R.id.zft);
        zth.initTabsWithCount(4);
        zth.setOnTabClickListener(new ZzFragmentTabHost.OnTabClickListener() {
            @Override
            public void onTabClick(ZzTab tab, boolean changed, int position) {
                Log.e("eee", changed + "," + position);
            }
        });

        zth.setSelection(0);

    }

    public void orderPic(View v) {
        zth.setImagePressIds(R.drawable.mainicon_checked, R.drawable.mainicon_checked,
                R.drawable.mainicon_checked, R.drawable.mainicon_checked);
        zth.setImageNormalIds(R.drawable.mainicon_normal, R.drawable.mainicon_normal,
                R.drawable.mainicon_normal, R.drawable.mainicon_normal);
    }

    public void reorderPic(View v) {
        zth.setImageNormalIds(R.drawable.mainicon_checked, R.drawable.mainicon_checked,
                R.drawable.mainicon_checked, R.drawable.mainicon_checked);
        zth.setImagePressIds(R.drawable.mainicon_normal, R.drawable.mainicon_normal,
                R.drawable.mainicon_normal, R.drawable.mainicon_normal);
    }

    public void updateName(View v) {
        zth.setTabNameWithResId(R.array.tab_names);
    }

    public void showMark(View v) {
        zth.showMarkAll();
    }

    public void hideMark(View v) {
        zth.hideMarkAll();
    }

    public void hideMarkOne(View v) {
        zth.hideMarkAt(0);
    }

    public void changeMarkValue(View v) {
        zth.setMarkNumberAll(99);
    }

    public void changeMarkValueOne(View v) {
        zth.setMarkNumberAt(1, 100);
    }

    public void fgm(View v) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

}
