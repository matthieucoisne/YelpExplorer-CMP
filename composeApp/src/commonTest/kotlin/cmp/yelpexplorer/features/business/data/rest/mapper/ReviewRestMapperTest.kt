//package cmp.yelpexplorer.features.business.data.rest.mapper
//
//import cmp.yelpexplorer.core.utils.DateTimeFormater
//import cmp.yelpexplorer.features.business.data.rest.model.ReviewEntity
//import cmp.yelpexplorer.features.business.data.rest.model.UserEntity
//import cmp.yelpexplorer.features.business.domain.model.Review
//import cmp.yelpexplorer.features.business.domain.model.User
//import io.mockk.every
//import io.mockk.annotations.MockK // Changed this import
//import io.mockk.junit5.MockKExtension // Or appropriate MockK test runner integration
//import io.mockk.mockk
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import org.junit.jupiter.api.extension.ExtendWith // If using JUnit 5
//
//// If using JUnit 5 with MockK, you might need this extension
//// @ExtendWith(MockKExtension::class) // Or ensure your test runner integrates MockK
//class ReviewRestMapperTest {
//
//    @MockK // Using the potentially different annotation
//    private lateinit var dateTimeFormater: DateTimeFormater
//
//    private lateinit var mapper: ReviewRestMapper
//
//    @BeforeTest
//    fun setUp() {
//        dateTimeFormater = mockk() // Manual initialization
//        mapper = ReviewRestMapper(dateTimeFormater)
//    }
//
//    @Test
//    fun `map should correctly map ReviewEntity list to Review list`() {
//        // Given
//        val reviewEntities = listOf(
//            ReviewEntity(
//                user = UserEntity(name = "John Doe", imageUrl = "http://example.com/user1.jpg"),
//                text = "Great place!\nHighly recommended.",
//                rating = 5,
//                timeCreated = "2023-10-27 10:00:00"
//            ),
//            ReviewEntity(
//                user = UserEntity(name = "Jane Smith", imageUrl = "http://example.com/user2.jpg"),
//                text = "Okay experience.\nCould be better.",
//                rating = 3,
//                timeCreated = "2023-10-26 15:30:00"
//            )
//        )
//        val expectedFormattedDate1 = "October 27, 2023"
//        val expectedFormattedDate2 = "October 26, 2023"
//
//        every { dateTimeFormater.formatDate(reviewEntities[0].timeCreated) } returns expectedFormattedDate1
//        every { dateTimeFormater.formatDate(reviewEntities[1].timeCreated) } returns expectedFormattedDate2
//
//        val expectedReviews = listOf(
//            Review(
//                user = User(name = "John Doe", imageUrl = "http://example.com/user1.jpg"),
//                text = "Great place!\nHighly recommended.",
//                rating = 5,
//                timeCreated = expectedFormattedDate1
//            ),
//            Review(
//                user = User(name = "Jane Smith", imageUrl = "http://example.com/user2.jpg"),
//                text = "Okay experience.\nCould be better.",
//                rating = 3,
//                timeCreated = expectedFormattedDate2
//            )
//        )
//
//        // When
//        val actualReviews = mapper.map(reviewEntities)
//
//        // Then
//        assertEquals(expectedReviews.size, actualReviews.size)
//        for (i in expectedReviews.indices) {
//            assertEquals(expectedReviews[i].user, actualReviews[i].user)
//            assertEquals(expectedReviews[i].text, actualReviews[i].text)
//            assertEquals(expectedReviews[i].rating, actualReviews[i].rating)
//            assertEquals(expectedReviews[i].timeCreated, actualReviews[i].timeCreated)
//        }
//    }
//
//    @Test
//    fun `map should replace multiple newlines in text with a single newline`() {
//        // Given
//        val reviewEntity = ReviewEntity(
//            user = UserEntity(name = "Test User", imageUrl = "http://example.com/user.jpg"),
//            text = "Line 1\n\nLine 2\n\n\nLine 3",
//            rating = 4,
//            timeCreated = "2023-10-27 12:00:00"
//        )
//        val formattedDate = "October 27, 2023"
//        every { dateTimeFormater.formatDate(reviewEntity.timeCreated) } returns formattedDate
//
//        val expectedReview = Review(
//            user = User(name = "Test User", imageUrl = "http://example.com/user.jpg"),
//            text = "Line 1\nLine 2\nLine 3",
//            rating = 4,
//            timeCreated = formattedDate
//        )
//
//        // When
//        val actualReview = mapper.map(listOf(reviewEntity)).first()
//
//        // Then
//        assertEquals(expectedReview.text, actualReview.text)
//    }
//}
