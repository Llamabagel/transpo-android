package ca.llamabagel.transpo.search.data

enum class SearchFilters(val id: String) {
    STOP("search_filter_stop_id"),
    ROUTE("search_filter_route_id"),
    PLACE("search_filter_place_id"),
    CATEGORY("search_filter_category_id")
}

data class SearchFilter(
    val stops: Boolean = false,
    val routes: Boolean = false,
    val places: Boolean = false
) {

    fun getOffFiltersList(): List<SearchFilters> {
        val list = mutableListOf<SearchFilters>()

        if (!stops) list.add(SearchFilters.STOP)
        if (!routes) list.add(SearchFilters.ROUTE)
        if (!places) list.add(SearchFilters.PLACE)

        return list
    }
}