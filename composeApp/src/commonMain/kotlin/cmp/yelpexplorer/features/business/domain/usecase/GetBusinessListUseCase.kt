package cmp.yelpexplorer.features.business.domain.usecase

import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow

interface GetBusinessListUseCase {
    operator fun invoke(
        term: String,
        location: String,
        sortBy: String,
        limit: Int
    ): Flow<List<Business>>
}

class GetBusinessListUseCaseImpl(
    private val businessRepository: BusinessRepository,
) : GetBusinessListUseCase {
    override fun invoke(
        term: String,
        location: String,
        sortBy: String,
        limit: Int
    ): Flow<List<Business>> {
        return businessRepository.getBusinessList(term, location, sortBy, limit)
    }
}
