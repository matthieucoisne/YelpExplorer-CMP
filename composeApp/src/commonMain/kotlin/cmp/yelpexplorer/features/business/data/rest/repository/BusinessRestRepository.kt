package cmp.yelpexplorer.features.business.data.rest.repository

import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSource
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.ReviewRestMapper
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BusinessRestRepository(
    private val businessRestDataSource: BusinessRestDataSource,
    private val businessRestMapper: BusinessRestMapper,
    private val reviewRestMapper: ReviewRestMapper,
    private val ioDispatcher: CoroutineDispatcher,
) : BusinessRepository {

    override fun getBusinessList(
        term: String,
        location: String,
        sortBy: String,
        limit: Int
    ): Flow<List<Business>> = flow {
        val businessListResponse = businessRestDataSource.getBusinessList(
            term,
            location,
            sortBy,
            limit,
        )
        emit(businessRestMapper.map(businessListResponse.businesses))
    }.flowOn(ioDispatcher)

    override fun getBusinessDetailsWithReviews(
        businessId: String
    ): Flow<Business> = flow {
        coroutineScope {
            val deferredBusinessDetails = async { businessRestDataSource.getBusinessDetails(businessId) }
            val deferredBusinessReviews = async { businessRestDataSource.getBusinessReviews(businessId) }
            val businessDetailsResponse = deferredBusinessDetails.await()
            val businessReviewsResponse = deferredBusinessReviews.await()

            val businessDetails = businessRestMapper.map(businessDetailsResponse)
            val reviews = reviewRestMapper.map(businessReviewsResponse.reviews)
            emit(businessDetails.copy(reviews = reviews))
        }
    }.flowOn(ioDispatcher)
}
