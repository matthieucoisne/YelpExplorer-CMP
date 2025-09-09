package cmp.yelpexplorer.features.business.domain.usecase

import cmp.yelpexplorer.features.business.domain.model.Business
import kotlinx.coroutines.flow.Flow

interface BusinessListUseCase {
    fun execute(
        term: String,
        location: String,
        sortBy: String,
        limit: Int
    ): Flow<List<Business>>
}
