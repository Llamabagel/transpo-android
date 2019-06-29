package ca.llamabagel.transpo.search.data

data class SearchFilter(
    val stops: Boolean = true,
    val routes: Boolean = true,
    val places: Boolean = true,
    val recent: Boolean = true
)