package cmp.yelpexplorer.features.business.data.rest.datasource.remote

import cmp.yelpexplorer.features.business.data.rest.model.BusinessEntity
import cmp.yelpexplorer.features.business.data.rest.model.BusinessListResponse
import cmp.yelpexplorer.features.business.data.rest.model.ReviewListResponse

/**
 * https://api.yelp.com/v3/businesses/search?term=sushi&location=montreal&sortBy=rating&limit=20
 * https://api.yelp.com/v3/businesses/FI3PVYBuz5fioko7qhsPZA
 * https://api.yelp.com/v3/businesses/FI3PVYBuz5fioko7qhsPZA/reviews
 */
interface BusinessRestDataSource {
    suspend fun getBusinessList(
        term: String,
        location: String,
        sortBy: String,
        limit: Int
    ): BusinessListResponse
    suspend fun getBusinessDetails(businessId: String): BusinessEntity
    suspend fun getBusinessReviews(businessId: String): ReviewListResponse
}
