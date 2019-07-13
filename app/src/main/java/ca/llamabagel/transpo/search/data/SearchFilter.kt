package ca.llamabagel.transpo.search.data

enum class SearchFilters {
    STOP,
    ROUTE,
    PLACE,
    RECENT,
    CATEGORY
}

data class SearchFilter(
    val stops: Boolean = true,
    val routes: Boolean = true,
    val places: Boolean = true,
    val recent: Boolean = true
) {

    fun getOffFiltersList(): List<SearchFilters> {
        val list = mutableListOf<SearchFilters>()

        if (!stops) list.add(SearchFilters.STOP)
        if (!routes) list.add(SearchFilters.ROUTE)
        if (!places) list.add(SearchFilters.PLACE)
        if (!recent) list.add(SearchFilters.RECENT)

        return list
    }
}