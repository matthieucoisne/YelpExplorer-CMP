package cmp.yelpexplorer.features.business.domain.usecase

import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow

interface BusinessListUseCase {
    fun execute(
        term: String,
        location: String,
        sortBy: String,
        limit: Int
    ): Flow<List<Business>>
}

class BusinessListUseCaseImpl(
    private val businessRepository: BusinessRepository,
) : BusinessListUseCase {
    override fun execute(
        term: String,
        location: String,
        sortBy: String,
        limit: Int
    ): Flow<List<Business>> {
        return businessRepository.getBusinessList(term, location, sortBy, limit)
    }
}
