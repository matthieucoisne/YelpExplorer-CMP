package cmp.yelpexplorer.features.business.data.rest.repository

import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSource
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessDetailsRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessListRestMapper
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
    private val businessListRestMapper: BusinessListRestMapper,
    private val businessDetailsRestMapper: BusinessDetailsRestMapper,
    private val reviewRestMapper: ReviewRestMapper,
    private val ioDispatcher: CoroutineDispatcher,
) : BusinessRepository {

    override fun getBusinessList(
        term: String,
        location: String,
        sortBy: String,
        limit: Int,
    ): Flow<List<Business>> = flow {
        val businessListResponse = businessRestDataSource.getBusinessList(
            term,
            location,
            sortBy,
            limit,
        )
        emit(businessListRestMapper.map(businessListResponse.businesses))
    }.flowOn(ioDispatcher)

    override fun getBusinessDetailsWithReviews(
        businessId: String,
    ): Flow<Business> = flow {
        coroutineScope {
            val deferredBusinessDetails = async { businessRestDataSource.getBusinessDetails(businessId) }
            val deferredBusinessReviews = async { businessRestDataSource.getBusinessReviews(businessId) }
            val businessDetailsResponse = deferredBusinessDetails.await()
            val businessReviewsResponse = deferredBusinessReviews.await()

            val businessDetails = businessDetailsRestMapper.map(businessDetailsResponse)
            val reviews = reviewRestMapper.map(businessReviewsResponse.reviews)
            emit(businessDetails.copy(reviews = reviews))
        }
    }.flowOn(ioDispatcher)
}
