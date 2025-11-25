package cmp.yelpexplorer.features.business.domain.usecase

import app.cash.turbine.test
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import cmp.yelpexplorer.utils.fakeDomainBusinessDetailsWithReviews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetBusinessDetailsUseCaseTest {

    private class FakeBusinessRepository(
        private val result: Business? = null,
        private val error: Error? = null,
    ) : BusinessRepository {
        override fun getBusinessList(
            term: String,
            location: String,
            sortBy: String,
            limit: Int,
        ): Flow<List<Business>> = throw NotImplementedError()

        override fun getBusinessDetailsWithReviews(
            businessId: String,
        ): Flow<Business> = flow {
            if (error != null) throw error
            emit(result!!)
        }
    }

    @Test
    fun `execute with success`() = runTest {
        // ARRANGE
        val getBusinessDetailsUseCase = GetBusinessDetailsUseCaseImpl(
            businessRepository = FakeBusinessRepository(
                result = fakeDomainBusinessDetailsWithReviews,
            )
        )

        // ACT
        val result = getBusinessDetailsUseCase(businessId = "businessId")

        // ASSERT
        result.test {
            assertEquals(
                expected = fakeDomainBusinessDetailsWithReviews,
                actual = awaitItem(),
            )
            awaitComplete()
        }
    }

    @Test
    fun `execute with error`() = runTest {
        // ARRANGE
        val expectedResult = "error"
        val getBusinessDetailsUseCase = GetBusinessDetailsUseCaseImpl(
            businessRepository = FakeBusinessRepository(
                error = Error(expectedResult),
            )
        )

        // ACT
        val result = getBusinessDetailsUseCase(businessId = "businessId")

        // ASSERT
        result.test {
            assertEquals(
                expected = expectedResult,
                actual = awaitError().message,
            )
            // awaitComplete() - Exceptions are termination events
        }
    }
}
