package cmp.yelpexplorer.features.business.data.graphql.repository

import app.cash.turbine.test
import cmp.yelpexplorer.BusinessDetailsQuery
import cmp.yelpexplorer.BusinessListQuery
import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSource
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessDetailsGraphQLMapper
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessListGraphQLMapper
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.utils.fakeDomainBusiness
import cmp.yelpexplorer.utils.fakeDomainBusinessDetailsWithReviews
import cmp.yelpexplorer.utils.fakeGraphQLBusinessDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessGraphQLRepositoryTest {

    private class FakeGraphQLDataSource(
        private val error: Exception? = null,
    ) : BusinessGraphQLDataSource {
        override suspend fun getBusinessList(
            term: String,
            location: String,
            sortBy: String,
            limit: Int,
        ): List<BusinessListQuery.Business> {
            if (error != null) throw error
            return emptyList() // We only care about the mapper output
        }

        override suspend fun getBusinessDetails(businessId: String): BusinessDetailsQuery.Business {
            if (error != null) throw error
            return fakeGraphQLBusinessDetails
        }
    }

    private class FakeBusinessListGraphQLMapper(
        private val result: List<Business>? = null,
        private val error: Exception? = null,
    ) : BusinessListGraphQLMapper {
        override suspend fun map(input: List<BusinessListQuery.Business?>): List<Business> {
            if (error != null) throw error
            return result!!
        }
    }

    private class FakeBusinessDetailsGraphQLMapper(
        private val result: Business? = null,
        private val error: Exception? = null,
    ) : BusinessDetailsGraphQLMapper {
        override suspend fun map(input: BusinessDetailsQuery.Business): Business {
            if (error != null) throw error
            return result!!
        }
    }

    @Test
    fun `get business list success`() = runTest {
        // ARRANGE
        val repository = BusinessGraphQLRepository(
            businessGraphQLDataSource = FakeGraphQLDataSource(),
            businessListGraphQLMapper = FakeBusinessListGraphQLMapper(
                result = listOf(fakeDomainBusiness),
            ),
            businessDetailsGraphQLMapper = FakeBusinessDetailsGraphQLMapper(),
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
        val repository = BusinessGraphQLRepository(
            businessGraphQLDataSource = FakeGraphQLDataSource(
                error = Exception("error"),
            ),
            businessListGraphQLMapper = FakeBusinessListGraphQLMapper(),
            businessDetailsGraphQLMapper = FakeBusinessDetailsGraphQLMapper(),
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
                expected = "error",
                actual = awaitError().message,
            )
        }
    }

    @Test
    fun `get business list mapper error`() = runTest {
        // ARRANGE
        val repository = BusinessGraphQLRepository(
            businessGraphQLDataSource = FakeGraphQLDataSource(),
            businessListGraphQLMapper = FakeBusinessListGraphQLMapper(
                error = Exception("error"),
            ),
            businessDetailsGraphQLMapper = FakeBusinessDetailsGraphQLMapper(),
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
                expected = "error",
                actual = awaitError().message,
            )
        }
    }

    @Test
    fun `get business details success`() = runTest {
        // ARRANGE
        val repository = BusinessGraphQLRepository(
            businessGraphQLDataSource = FakeGraphQLDataSource(),
            businessListGraphQLMapper = FakeBusinessListGraphQLMapper(),
            businessDetailsGraphQLMapper = FakeBusinessDetailsGraphQLMapper(
                result = fakeDomainBusinessDetailsWithReviews,
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
        val repository = BusinessGraphQLRepository(
            businessGraphQLDataSource = FakeGraphQLDataSource(
                error = Exception("error"),
            ),
            businessListGraphQLMapper = FakeBusinessListGraphQLMapper(),
            businessDetailsGraphQLMapper = FakeBusinessDetailsGraphQLMapper(),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessDetailsWithReviews(
            businessId = "businessId",
        )

        // ASSERT
        result.test {
            assertEquals(
                expected = "error",
                actual = awaitError().message,
            )
        }
    }

    @Test
    fun `get business details mapper error`() = runTest {
        // ARRANGE
        val repository = BusinessGraphQLRepository(
            businessGraphQLDataSource = FakeGraphQLDataSource(),
            businessListGraphQLMapper = FakeBusinessListGraphQLMapper(),
            businessDetailsGraphQLMapper = FakeBusinessDetailsGraphQLMapper(
                error = Exception("error"),
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
                expected = "error",
                actual = awaitError().message,
            )
        }
    }
}
