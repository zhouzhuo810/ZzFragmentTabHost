# ZzFragmentTabHost
A powerful FragmentTabhost.


# Gradle

```
compile 'me.zhouzhuo.zzfragmenttabhost:zz-fragment-tabhost:1.0.1'
```

# Maven

```xml
<dependency>
  <groupId>me.zhouzhuo.zzfragmenttabhost</groupId>
  <artifactId>zz-fragment-tabhost</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```


![这里写图片描述](https://github.com/zhouzhuo810/ZzFragmentTabHost/blob/master/zzfragmenttabhost.jpg)




# 用法简介


res/layout:

```xml

    <me.zhouzhuo.zzfragmenttabhost.ZzFragmentTabHost
        android:id="@+id/zft"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

    </me.zhouzhuo.zzfragmenttabhost.ZzFragmentTabHost>
```


java:

```java

        ZzFragmentTabHost zft = (ZzFragmentTabHost) findViewById(R.id.zft);
        
        zft.setUp(this, getSupportFragmentManager(), R.id.fgm_container) //设置fragment的容器FrameLayout的id
                .initTabsWithCount(4) //设置Fragment的数量
                .setFragmentClazz(      //设置Fragment的class
                        FragmentOne.class,
                        FragmentTwo.class,
                        FragmentThree.class,
                        FragmentFour.class)
                .setTabNameWithResId(R.array.tab_names)   //设置Tab文字resid
                .setImagePressIds(                      //设置tab按下时图片id
                        R.drawable.mainicon_checked,
                        R.drawable.mainicon_checked,
                        R.drawable.mainicon_checked,
                        R.drawable.mainicon_checked)
                .setImageNormalIds(                     //设置tab未选择时图片id
                        R.drawable.mainicon_normal,
                        R.drawable.mainicon_normal,
                        R.drawable.mainicon_normal,
                        R.drawable.mainicon_normal)
                .setTabTextSizeNormalAll(DisplayUtil.scaleValue(this, 24))      //设置tab未选择时文字大小
                .setTabTextSizeSelectedAll(DisplayUtil.scaleValue(this, 24))    //设置tab选择时文字大小
                .setTabImgSizeNormalAll(DisplayUtil.scaleValue(this, 50))       //设置tab未按下时图片大小
                .setTabImgSizeSelectedAll(DisplayUtil.scaleValue(this, 50))     //设置tab按下时图片大小
                .setTabTextColorResIdNormalAll(R.color.colorTabGray)            //设置tab未按下时文字颜色
                .setTabTextColorResIdSelectedAll(R.color.colorTabMain)          //设置tab按下时文字颜色
                .setMarkNumberSizeAll(DisplayUtil.scaleValue(this, 20))         //设置tab角标文字大小
                .setMarkWidthAll(DisplayUtil.scaleValue(this, 50))              //设置tab角标宽度
                .setMarkHeightAll(DisplayUtil.scaleValue(this, 40))             //设置tab角标高度
                .setMarkNumberAt(0, 100)                                        //设置第postion位置的tab对应的角标的值
                .showMarkAt(0)                                                  //显示第position位置的tab对应的角标
                .setOnTabClickListener(new ZzFragmentTabHost.OnTabClickListener() {    //tab点击监听
                    @Override
                    public void onTabClick(ZzTab tab, boolean changed, int position) {

                    }
                })
                .setSelection(0);          //默认选中第一个tab

```



