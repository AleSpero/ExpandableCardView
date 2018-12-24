package com.alessandrosperotti.expandablecardviewexample

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.location.*


class MainActivity : AppCompatActivity() {

    lateinit var uri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)

        main_collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)
        main_collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.WHITE))

        buttonMaps.setOnClickListener {
            val uri = Uri.parse("geo:25.7906500,-80.1300500?q=Miami Beach&z=10")
            intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        main_profile_card.setOnExpandedListener { _, isExpanded ->
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
