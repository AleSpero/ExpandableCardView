package com.alessandrosperotti.expandablecardviewexample

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.alespero.expandablecardview.ExpandableCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.ctl)
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.WHITE))

        val card : ExpandableCardView = findViewById(R.id.profile)

        card.setOnExpandedListener { _, isExpanded ->
            if(isExpanded) Toast.makeText(applicationContext, "Expanded!", Toast.LENGTH_SHORT).show()
            else Toast.makeText(applicationContext, "Collapsed!", Toast.LENGTH_SHORT).show()
        }
    }

}
