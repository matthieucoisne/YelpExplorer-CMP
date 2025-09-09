package cmp.yelpexplorer.core.utils

object Const {
    // Change the data source for the one you want to use. Choose between REST or GRAPHQL
    val DATASOURCE = DataSource.REST

    const val URL_GRAPHQL = "https://api.yelp.com/v3/graphql"
    const val URL_REST = "https://api.yelp.com/v3/"

    // TODO: Add your API KEY - https://docs.developer.yelp.com/docs/fusion-authentication
    const val API_KEY = "YOUR_API_KEY"

    const val DISPATCHER_IO = "DISPATCHER_IO"
}

enum class DataSource {
    REST,
    GRAPHQL,
}
