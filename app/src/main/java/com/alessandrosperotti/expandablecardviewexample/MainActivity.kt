package com.alessandrosperotti.expandablecardviewexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alespero.expandablecardview.ExpandableCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val card : ExpandableCardView = findViewById(R.id.mycard)

        card.setOnClickListener({
            if(card.isExpanded) card.collapse()
            else card.expand()
        })

        card.setOnExpandedListener(object : ExpandableCardView.OnExpandedListener {
            override fun onCollapsed(v: View) {
                Toast.makeText(applicationContext, "Collapsed!", Toast.LENGTH_SHORT).show()
            }

            override fun onExpanded(v: View) {
                Toast.makeText(applicationContext, "Expanded!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
