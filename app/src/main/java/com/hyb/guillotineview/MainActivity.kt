package com.hyb.guillotineview

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hyb.guillotine.Animation.GuillotineView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var view = findViewById<View>(R.id.toplayout)
        var titleLeftBtn = view.findViewById<ImageView>(R.id.iv_leftbtn)

        var myGuillotineView = LayoutInflater.from(this).inflate(R.layout.guillotine_layout ,null)
        var tvTitle = myGuillotineView.findViewById<TextView>(R.id.tv_title)
        tvTitle.text = "GuillotineView"
        var guillotineViewleftBtn = myGuillotineView.findViewById<ImageView>(R.id.iv_leftbtn)

        GuillotineView.GuillotineBuilder(this)
            .setGuillotineView(myGuillotineView)
            .setCloseItemView(guillotineViewleftBtn)
            .setOpenItemView(titleLeftBtn)
            .setClosedOnStart(true)
            .setIsLeftBtn(true)
            .build()

    }
}
