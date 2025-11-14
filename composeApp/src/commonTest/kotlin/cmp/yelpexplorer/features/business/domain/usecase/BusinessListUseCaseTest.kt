package cmp.yelpexplorer.features.business.domain.usecase

import app.cash.turbine.test
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import cmp.yelpexplorer.utils.fakeDomainBusiness
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessListUseCaseTest {

    private class FakeBusinessRepository(
        private val result: List<Business>? = null,
        private val error: Error? = null,
    ) : BusinessRepository {
        override fun getBusinessList(
            term: String,
            location: String,
            sortBy: String,
            limit: Int,
        ): Flow<List<Business>> = flow {
            if (error != null) throw error
            emit(result!!)
        }

        override fun getBusinessDetailsWithReviews(
            businessId: String,
        ): Flow<Business> = throw NotImplementedError()
    }

    @Test
    fun `execute with success`() = runTest {
        // ARRANGE
        val useCase = BusinessListUseCaseImpl(
            businessRepository = FakeBusinessRepository(
                result = listOf(fakeDomainBusiness),
            )
        )

        // ACT
        val result = useCase.execute(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 20,
        )

        // ASSERT
        result.test {
            assertEquals(
                expected = listOf(fakeDomainBusiness),
                actual = awaitItem(),
            )
            awaitComplete()
        }
    }

    @Test
    fun `execute with error`() = runTest {
        // ARRANGE
        val expectedResult = "error"
        val useCase = BusinessListUseCaseImpl(
            businessRepository = FakeBusinessRepository(
                error = Error(expectedResult),
            )
        )

        // ACT
        val result = useCase.execute(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 20,
        )

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
