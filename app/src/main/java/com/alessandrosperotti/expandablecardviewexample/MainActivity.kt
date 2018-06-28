package com.alessandrosperotti.expandablecardviewexample

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.alespero.expandablecardview.ExpandableCardView
import android.R.menu
import android.content.Intent
import android.net.Uri
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.location.*


class MainActivity : AppCompatActivity() {

    lateinit var uri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.ctl)
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.WHITE))

        buttonMaps.setOnClickListener {
            val uri = Uri.parse("geo:25.7906500,-80.1300500?q=Miami Beach&z=10")
            intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


        val card : ExpandableCardView = findViewById(R.id.profile)

        card.setOnExpandedListener { _, isExpanded ->
            if(isExpanded) Toast.makeText(applicationContext, "Expanded!", Toast.LENGTH_SHORT).show()
            else Toast.makeText(applicationContext, "Collapsed!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){

            R.id.aboutme -> {
                uri = Uri.parse("https://www.alessandrosperotti.com")
                intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            R.id.getlibrary -> {
                uri = Uri.parse("https://github.com/AleSpero/ExpandableCardView")
                intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            R.id.myotherapps -> {
                uri = Uri.parse("market://search?q=alessandro%20sperotti")
                intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
