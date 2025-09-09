//package cmp.yelpexplorer.features.business.domain.usecase
//
//import cmp.yelpexplorer.features.business.domain.model.Business
//import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
//import kotlinx.coroutines.test.runTest
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class GetBusinessListUseCaseTest {
//    @Test
//    fun `execute with success`() = runTest {
//        // ARRANGE
//        val fakeBusiness = Business(
//            id = "id",
//            name = "name",
//            address = "address",
//            photoUrl = "http://",
//            price = "$$",
//            categories = listOf("category#1"),
//            reviewCount = 1337,
//            rating = 4.5,
//            hours = null,
//            reviews = null,
//        )
//        val businessList = listOf(fakeBusiness, fakeBusiness)
//        val fakeBusinessRepository = object: BusinessRepository {
//            override suspend fun getBusinessList(term: String, location: String, sortBy: String, limit: Int): Result<List<Business>> {
//                return Result.success(value = businessList)
//            }
//            override suspend fun getBusinessDetailsWithReviews(businessId: String) = throw Error("IGNORED")
//        }
//        val useCase = GetBusinessListUseCase(fakeBusinessRepository)
//
//        // ACT
//        val result = useCase.execute(
//            term = "term",
//            location = "location",
//            sortBy = "sortBy",
//            limit = 20
//        )
//
//        // ASSERT
//        assertEquals(expected = result, actual = Result.success(value = businessList))
//    }
//
//    @Test
//    fun `execute with error`() = runTest {
//        // ARRANGE
//        val expectedResult: Result<List<Business>> = Result.failure(exception = Error("error message"))
//        val fakeBusinessRepository = object: BusinessRepository {
//            override suspend fun getBusinessList(term: String, location: String, sortBy: String, limit: Int) = expectedResult
//            override suspend fun getBusinessDetailsWithReviews(businessId: String) = throw Error("IGNORED")
//        }
//        val useCase = GetBusinessListUseCase(fakeBusinessRepository)
//
//        // ACT
//        val result = useCase.execute(
//            term = "term",
//            location = "location",
//            sortBy = "sortBy",
//            limit = 20
//        )
//
//        // ASSERT
//        assertEquals(expected = result, actual = expectedResult)
//    }
//}
