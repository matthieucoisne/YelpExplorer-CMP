package cmp.yelpexplorer.features.business.domain.usecase

import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow

interface BusinessDetailsUseCase {
    fun execute(businessId: String): Flow<Business>
}

class BusinessDetailsUseCaseImpl(
    private val businessRepository: BusinessRepository,
): BusinessDetailsUseCase {
    override fun execute(businessId: String): Flow<Business> {
        return businessRepository.getBusinessDetailsWithReviews(businessId)
    }
}
