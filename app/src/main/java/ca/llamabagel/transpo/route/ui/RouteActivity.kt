package ca.llamabagel.transpo.route.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import ca.llamabagel.transpo.R

class RouteActivity : AppCompatActivity(R.layout.activity_route) {

    private val args: RouteActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(findViewById(R.id.toolbar))

        val pager = findViewById<ViewPager2>(R.id.view_pager)
        pager.isUserInputEnabled = false
        pager.adapter = RoutePagerAdapter(this, args.routeId)
    }
}