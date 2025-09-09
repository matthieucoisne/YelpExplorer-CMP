package cmp.yelpexplorer.features.business.data.graphql.datasource.remote

import cmp.yelpexplorer.BusinessDetailsQuery
import cmp.yelpexplorer.BusinessListQuery

/**
 * https://api.yelp.com/v3/businesses/search?term=sushi&location=montreal&sortBy=rating&limit=20
 * https://api.yelp.com/v3/businesses/FI3PVYBuz5fioko7qhsPZA
 * https://api.yelp.com/v3/businesses/FI3PVYBuz5fioko7qhsPZA/reviews
 */
interface BusinessGraphQLDataSource {
    suspend fun getBusinessList(
        term: String,
        location: String,
        sortBy: String,
        limit: Int,
    ): List<BusinessListQuery.Business>
    suspend fun getBusinessDetails(businessId: String): BusinessDetailsQuery.Business
}
