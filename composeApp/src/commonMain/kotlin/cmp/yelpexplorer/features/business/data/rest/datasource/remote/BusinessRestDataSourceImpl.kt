package cmp.yelpexplorer.features.business.data.rest.datasource.remote

import cmp.yelpexplorer.features.business.data.rest.model.BusinessEntity
import cmp.yelpexplorer.features.business.data.rest.model.BusinessListResponse
import cmp.yelpexplorer.features.business.data.rest.model.ReviewListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * https://api.yelp.com/v3/businesses/search?term=sushi&location=montreal&sortBy=rating&limit=20
 * https://api.yelp.com/v3/businesses/FI3PVYBuz5fioko7qhsPZA
 * https://api.yelp.com/v3/businesses/FI3PVYBuz5fioko7qhsPZA/reviews
 */
class BusinessRestDataSourceImpl(
    private val httpClient: HttpClient,
) : BusinessRestDataSource {
    override suspend fun getBusinessList(
        term: String,
        location: String,
        sortBy: String,
        limit: Int,
    ): BusinessListResponse {
        return try {
            httpClient.get("businesses/search") {
                parameter("term", term)
                parameter("location", location)
                parameter("sortBy", sortBy)
                parameter("limit", "$limit")
            }.body()
        } catch (ex: Exception){
            throw ex
        }
    }

    override suspend fun getBusinessDetails(businessId: String): BusinessEntity {
        return httpClient.get("businesses/$businessId").body()
    }

    override suspend fun getBusinessReviews(businessId: String): ReviewListResponse {
        return httpClient.get("businesses/$businessId/reviews").body()
    }
}
// TODO error handling to have something more generic surfaced to the user rather than
//  something like "illegal input field" trying to parse the json response even
//  if our API KEY is not valid for example
