package cmp.yelpexplorer.features.business.data.rest.repository

import app.cash.turbine.test
import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSource
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessDetailsRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessListRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.ReviewRestMapper
import cmp.yelpexplorer.features.business.data.rest.model.BusinessEntity
import cmp.yelpexplorer.features.business.data.rest.model.BusinessListResponse
import cmp.yelpexplorer.features.business.data.rest.model.ReviewEntity
import cmp.yelpexplorer.features.business.data.rest.model.ReviewListResponse
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.model.Review
import cmp.yelpexplorer.utils.fakeDomainBusiness
import cmp.yelpexplorer.utils.fakeDomainBusinessDetailsWithReviews
import cmp.yelpexplorer.utils.fakeDomainReview
import cmp.yelpexplorer.utils.fakeRestBusiness
import cmp.yelpexplorer.utils.fakeRestReview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessRestRepositoryTest {

    private class FakeRestDataSource(
        private val error: Exception? = null,
    ) : BusinessRestDataSource {
        override suspend fun getBusinessList(
            term: String,
            location: String,
            sortBy: String,
            limit: Int,
        ): BusinessListResponse {
            if (error != null) throw error
            return BusinessListResponse(
                businesses = listOf(fakeRestBusiness),
            )
        }

        override suspend fun getBusinessDetails(businessId: String): BusinessEntity {
            if (error != null) throw error
            return fakeRestBusiness
        }

        override suspend fun getBusinessReviews(businessId: String): ReviewListResponse {
            if (error != null) throw error
            return ReviewListResponse(
                reviews = listOf(fakeRestReview),
            )
        }
    }

    private class FakeBusinessListRestMapper(
        private val result: List<Business>? = null,
        private val error: Exception? = null,
    ) : BusinessListRestMapper {
        override suspend fun map(input: List<BusinessEntity>): List<Business> {
            if (error != null) throw error
            return result!!
        }
    }

    private class FakeBusinessDetailsRestMapper(
        private val result: Business? = null,
        private val error: Exception? = null,
    ) : BusinessDetailsRestMapper {
        override suspend fun map(input: BusinessEntity): Business {
            if (error != null) throw error
            return result!!
        }
    }

    private class FakeReviewRestMapper(
        private val result: List<Review>? = null,
        private val error: Exception? = null,
    ) : ReviewRestMapper {
        override suspend fun map(input: List<ReviewEntity>): List<Review> {
            if (error != null) throw error
            return result!!
        }
    }

    @Test
    fun `get business list success`() = runTest {
        // ARRANGE
        val repository = BusinessRestRepository(
            businessRestDataSource = FakeRestDataSource(),
            businessListRestMapper = FakeBusinessListRestMapper(
                result = listOf(fakeDomainBusiness),
            ),
            businessDetailsRestMapper = FakeBusinessDetailsRestMapper(),
            reviewRestMapper = FakeReviewRestMapper(),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessList(
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
    fun `get business list data source error`() = runTest {
        // ARRANGE
        val error = "error"
        val repository = BusinessRestRepository(
            businessRestDataSource = FakeRestDataSource(
                error = Exception(error),
            ),
            businessListRestMapper = FakeBusinessListRestMapper(),
            businessDetailsRestMapper = FakeBusinessDetailsRestMapper(),
            reviewRestMapper = FakeReviewRestMapper(),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessList(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 20,
        )

        // ASSERT
        result.test {
            assertEquals(
                expected = error,
                actual = awaitError().message,
            )
        }
    }

    @Test
    fun `get business list mapper error`() = runTest {
        // ARRANGE
        val error = "error"
        val repository = BusinessRestRepository(
            businessRestDataSource = FakeRestDataSource(),
            businessListRestMapper = FakeBusinessListRestMapper(
                error = Exception(error),
            ),
            businessDetailsRestMapper = FakeBusinessDetailsRestMapper(),
            reviewRestMapper = FakeReviewRestMapper(),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessList(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 20,
        )

        // ASSERT
        result.test {
            assertEquals(
                expected = error,
                actual = awaitError().message,
            )
        }
    }

    @Test
    fun `get business details success`() = runTest {
        // ARRANGE
        val repository = BusinessRestRepository(
            businessRestDataSource = FakeRestDataSource(),
            businessListRestMapper = FakeBusinessListRestMapper(),
            businessDetailsRestMapper = FakeBusinessDetailsRestMapper(
                result = fakeDomainBusinessDetailsWithReviews,
            ),
            reviewRestMapper = FakeReviewRestMapper(
                result = listOf(fakeDomainReview),
            ),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessDetailsWithReviews(
            businessId = "businessId",
        )

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
    fun `get business details data source error`() = runTest {
        // ARRANGE
        val error = "error"
        val repository = BusinessRestRepository(
            businessRestDataSource = FakeRestDataSource(
                error = Exception(error),
            ),
            businessListRestMapper = FakeBusinessListRestMapper(),
            businessDetailsRestMapper = FakeBusinessDetailsRestMapper(),
            reviewRestMapper = FakeReviewRestMapper(),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessDetailsWithReviews(
            businessId = "businessId",
        )

        // ASSERT
        result.test {
            assertEquals(
                expected = error,
                actual = awaitError().message,
            )
        }
    }

    @Test
    fun `get business details mapper error`() = runTest {
        // ARRANGE
        val error = "error"
        val repository = BusinessRestRepository(
            businessRestDataSource = FakeRestDataSource(),
            businessListRestMapper = FakeBusinessListRestMapper(),
            businessDetailsRestMapper = FakeBusinessDetailsRestMapper(
                error = Exception(error),
            ),
            reviewRestMapper = FakeReviewRestMapper(),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessDetailsWithReviews(
            businessId = "businessId",
        )

        // ASSERT
        result.test {
            assertEquals(
                expected = error,
                actual = awaitError().message,
            )
        }
    }

    @Test
    fun `get business details review mapper error`() = runTest {
        // ARRANGE
        val error = "error"
        val repository = BusinessRestRepository(
            businessRestDataSource = FakeRestDataSource(),
            businessListRestMapper = FakeBusinessListRestMapper(),
            businessDetailsRestMapper = FakeBusinessDetailsRestMapper(
                fakeDomainBusinessDetailsWithReviews
            ),
            reviewRestMapper = FakeReviewRestMapper(
                error = Exception(error),
            ),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessDetailsWithReviews(
            businessId = "businessId",
        )

        // ASSERT
        result.test {
            assertEquals(
                expected = error,
                actual = awaitError().message,
            )
        }
    }
}
