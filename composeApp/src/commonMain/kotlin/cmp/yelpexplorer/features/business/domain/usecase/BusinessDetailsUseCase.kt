package cmp.yelpexplorer.features.business.domain.usecase

import cmp.yelpexplorer.features.business.domain.model.Business
import kotlinx.coroutines.flow.Flow

interface BusinessDetailsUseCase {
    fun execute(businessId: String): Flow<Business>
}
