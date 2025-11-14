package cmp.yelpexplorer.features.business.data.graphql.repository

import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSource
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessDetailsGraphQLMapper
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessListGraphQLMapper
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BusinessGraphQLRepository(
    private val businessGraphQLDataSource: BusinessGraphQLDataSource,
    private val businessListGraphQLMapper: BusinessListGraphQLMapper,
    private val businessDetailsGraphQLMapper: BusinessDetailsGraphQLMapper,
    private val ioDispatcher: CoroutineDispatcher,
) : BusinessRepository {
    override fun getBusinessList(
        term: String,
        location: String,
        sortBy: String,
        limit: Int,
    ): Flow<List<Business>> = flow {
        val businessListResponse = businessGraphQLDataSource.getBusinessList(
            term,
            location,
            sortBy,
            limit
        )
        emit(businessListGraphQLMapper.map(businessListResponse))
    }.flowOn(ioDispatcher)

    override fun getBusinessDetailsWithReviews(
        businessId: String,
    ): Flow<Business> = flow {
        val businessDetailsResponse = businessGraphQLDataSource.getBusinessDetails(businessId)
        emit(businessDetailsGraphQLMapper.map(businessDetailsResponse))
    }.flowOn(ioDispatcher)
}
