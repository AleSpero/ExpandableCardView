package com.alessandrosperotti.expandablecardviewexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val card : ExpandableCardView = findViewById(R.id.card)

        card.setOnClickListener({
            if(card.isExpanded) card.collapse()
            else card.expand()
        })
    }

}
