package cmp.yelpexplorer.features.business.domain.usecase

import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow

interface GetBusinessDetailsUseCase {
    operator fun invoke(businessId: String): Flow<Business>
}

class GetBusinessDetailsUseCaseImpl(
    private val businessRepository: BusinessRepository,
): GetBusinessDetailsUseCase {
    override fun invoke(businessId: String): Flow<Business> {
        return businessRepository.getBusinessDetailsWithReviews(businessId)
    }
}
