package ca.llamabagel.transpo.route.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RoutePagerAdapter(activity: RouteActivity, private val routeId: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> RouteMapFragment.newInstance(routeId)
        1 -> RouteTimetableFragment()
        else -> throw IllegalArgumentException("Invalid pager position: $position")
    }
}