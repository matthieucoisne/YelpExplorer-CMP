package cmp.yelpexplorer.features.business.data.graphql.datasource.remote

import cmp.yelpexplorer.BusinessDetailsQuery
import cmp.yelpexplorer.BusinessListQuery
import com.apollographql.apollo.ApolloClient

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

class BusinessGraphQLDataSourceImpl(
    private val apolloClient: ApolloClient,
) : BusinessGraphQLDataSource {
    override suspend fun getBusinessList(
        term: String,
        location: String,
        sortBy: String,
        limit: Int,
    ): List<BusinessListQuery.Business> =
        apolloClient.query(
            query = BusinessListQuery(
                term = term,
                location = location,
                sortBy = sortBy,
                limit = limit
            )
        ).execute().dataOrThrow().search?.businessFilterNotNull()!!

    override suspend fun getBusinessDetails(
        businessId: String,
    ): BusinessDetailsQuery.Business =
        apolloClient.query(
            query = BusinessDetailsQuery(
                id = businessId
            )
        ).execute().dataOrThrow().business!!
}
