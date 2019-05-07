# GuillotineView
切割式、断头台式view弹出效果

![image](https://github.com/yoyoyaobin/GuillotineView/blob/master/app/src/main/assets/sample.gif)

## 1使用方式

### 1.1在project的gradle添加
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### 1.2在Module的gradle添加
```
implementation 'com.github.yoyoyaobin:GuillotineView:1.0.0'
```

### 1.3在你的activity里面实现代码
```
GuillotineView.GuillotineBuilder(this)
            ...
            .build()
```

### 1.4栗子
```
  var view = findViewById<View>(R.id.toplayout)
  var titleLeftBtn = view.findViewById<ImageView>(R.id.iv_leftbtn)
  var myGuillotineView = LayoutInflater.from(this).inflate(R.layout.guillotine_layout ,null)
  var tvTitle = myGuillotineView.findViewById<TextView>(R.id.tv_title)
  tvTitle.text = "GuillotineView"
  var guillotineViewleftBtn = myGuillotineView.findViewById<ImageView>(R.id.iv_leftbtn)

  GuillotineView.GuillotineBuilder(this)
      .setGuillotineView(myGuillotineView)//设置弹出view
      .setCloseItemView(guillotineViewleftBtn)//设置关闭view的触发按钮
      .setOpenItemView(titleLeftBtn)//设置开启view的触发按钮
      .setClosedOnStart(true)//是否默认关闭
      .setIsLeftBtn(true)//触发按钮为左边
      .build()
```

## 2说明
1.首页layout根布局需要为FrameLayout（弹出的view则不限制）<br>
2.允许自定义插值器，可以使用setInterpolator()设置<br>
3.首页跟弹出view的两个按钮最好是相同位置，效果最佳（比如两个layout都复用了相同的topbar）<br>
2.触发按钮在左边，则设置setIsLeftBtn，反之则设置setIsRightBtn




