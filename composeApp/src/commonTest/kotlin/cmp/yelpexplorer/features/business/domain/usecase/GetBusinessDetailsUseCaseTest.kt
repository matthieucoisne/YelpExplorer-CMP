//package cmp.yelpexplorer.features.business.domain.usecase
//
//import cmp.yelpexplorer.features.business.domain.model.Business
//import cmp.yelpexplorer.features.business.domain.model.Review
//import cmp.yelpexplorer.features.business.domain.model.User
//import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
//import kotlinx.coroutines.test.runTest
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class GetBusinessDetailsUseCaseTest {
//    private val fakeBusiness = Business(
//        id = "id",
//        name = "name",
//        address = "address",
//        photoUrl = "http://",
//        price = "$$",
//        categories = listOf("category#1"),
//        reviewCount = 1337,
//        rating = 4.5,
//        hours = null,
//        reviews = listOf(
//            Review(
//                user = User(
//                    name = "name",
//                    imageUrl = "photoUrl",
//                ),
//                text = "text",
//                rating = 5,
//                timeCreated = "01-01-2020",
//            )
//        ),
//    )
//
//    private val fakeBusinessRepository = object : BusinessRepository {
//        override suspend fun getBusinessList(term: String, location: String, sortBy: String, limit: Int) = throw Error("IGNORED")
//        override suspend fun getBusinessDetailsWithReviews(businessId: String) = Result.success(value = fakeBusiness)
//    }
//
//    @Test
//    fun `execute with success using a fake repository`() = runTest {
//        // ARRANGE
//        val useCase = GetBusinessDetailsUseCase(fakeBusinessRepository)
//
//        // ACT
//        val result = useCase.execute(businessId = "businessId")
//
//        // ASSERT
//        assertEquals(actual = result, expected = Result.success(value = fakeBusiness))
//    }
//}
