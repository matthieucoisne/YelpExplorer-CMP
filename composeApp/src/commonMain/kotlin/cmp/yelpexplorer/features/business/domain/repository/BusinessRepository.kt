package cmp.yelpexplorer.features.business.domain.repository

import cmp.yelpexplorer.features.business.domain.model.Business
import kotlinx.coroutines.flow.Flow

interface BusinessRepository {
    fun getBusinessList(
        term: String,
        location: String,
        sortBy: String,
        limit: Int,
    ): Flow<List<Business>>
    fun getBusinessDetailsWithReviews(businessId: String): Flow<Business>
}
