package cmp.yelpexplorer.utils

import cmp.yelpexplorer.BusinessDetailsQuery
import cmp.yelpexplorer.BusinessListQuery
import cmp.yelpexplorer.core.utils.DateTimeFormatter
import cmp.yelpexplorer.core.utils.ResourceProvider
import cmp.yelpexplorer.features.business.data.rest.model.BusinessEntity
import cmp.yelpexplorer.features.business.data.rest.model.ReviewEntity
import cmp.yelpexplorer.features.business.data.rest.model.UserEntity
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.model.Review
import cmp.yelpexplorer.features.business.domain.model.User
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsUiModel
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListUiModel
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessUiModel
import cmp.yelpexplorer.fragment.BusinessDetails
import cmp.yelpexplorer.fragment.BusinessSummary
import org.jetbrains.compose.resources.StringResource
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_4

private val fakeGraphQLBusinessSummaryFragment = BusinessSummary(
    id = "FI3PVYBuz5fioko7qhsPZA",
    name = "Jun I",
    photos = listOf(
        "https://s3-media1.fl.yelpcdn.com/bphoto/ABGECJSfZWxkhz2oF4h8Fg/o.jpg",
    ),
    rating = 4.0,
    review_count = 87,
    location = BusinessSummary.Location(
        address1 = "156 Avenue Laurier O",
        city = "Montreal",
    ),
    price = "$$$",
    categories = listOf(
        BusinessSummary.Category(title = "Sushi Bars"),
        BusinessSummary.Category(title = "Japanese"),
    ),
)

private val fakeGraphQLBusinessDetailsFragment = BusinessDetails(
    display_phone = "+1 514-276-5864",
    hours = listOf(
        BusinessDetails.Hour(
            open = listOf(
                BusinessDetails.Open(
                    day = 0,
                    start = "1800",
                    end = "2200",
                ),
            ),
        ),
    ),
    reviews = listOf(
        BusinessDetails.Review(
            user = BusinessDetails.User(
                name = "John Smith",
                image_url = "http://example.com/user1.jpg",
            ),
            text = "Great place!\n\nHighly recommended.",
            rating = 5,
            time_created = "2023-10-27 10:00:00",
        )
    )
)

val fakeGraphQLBusinessSummary = BusinessListQuery.Business(
    __typename = "Business",
    businessSummary = fakeGraphQLBusinessSummaryFragment
)

val fakeGraphQLBusinessDetails = BusinessDetailsQuery.Business(
    __typename = "Business",
    businessSummary = fakeGraphQLBusinessSummaryFragment,
    businessDetails = fakeGraphQLBusinessDetailsFragment,
)

val fakeRestBusiness = BusinessEntity(
    id = "FI3PVYBuz5fioko7qhsPZA",
    name = "Jun I",
    imageUrl = "https://s3-media1.fl.yelpcdn.com/bphoto/ABGECJSfZWxkhz2oF4h8Fg/o.jpg",
    reviewCount = 87,
    categories = listOf(
        BusinessEntity.CategoryEntity(title = "Sushi Bars"),
        BusinessEntity.CategoryEntity(title = "Japanese"),
    ),
    rating = 4.0,
    price = "$$$",
    location = BusinessEntity.LocationEntity(
        address1 = "156 Avenue Laurier O",
        city = "Montreal",
    ),
    hours = null,
    reviews = null,
)

val fakeDomainBusiness = Business(
    id = "FI3PVYBuz5fioko7qhsPZA",
    name = "Jun I",
    photoUrl = "https://s3-media1.fl.yelpcdn.com/bphoto/ABGECJSfZWxkhz2oF4h8Fg/o.jpg",
    rating = 4.0,
    reviewCount = 87,
    address = "156 Avenue Laurier O, Montreal",
    price = "$$$",
    categories = listOf("Sushi Bars", "Japanese"),
    hours = null,
    reviews = null,
)

val fakeDomainReview = Review(
    user = User(
        name = "John Smith",
        imageUrl = "http://example.com/user1.jpg",
    ),
    text = "Great place!\nHighly recommended.",
    rating = 5,
    timeCreated = "October 27, 2023",
)

val fakeDomainBusinessDetailsWithReviews = fakeDomainBusiness.copy(
    hours = mapOf(
        0 to listOf("10:00 - 10:00"),
    ),
    reviews = listOf(fakeDomainReview),
)

val fakeRestReview = ReviewEntity(
    user = UserEntity(
        name = "John Smith",
        imageUrl = "http://example.com/user1.jpg"
    ),
    text = "Great place!\n\nHighly recommended.",
    rating = 5,
    timeCreated = "2023-10-27 10:00:00",
)

val fakeBusinessListUiModel = BusinessListUiModel(
    businessList = listOf(
        BusinessUiModel(
            id = "FI3PVYBuz5fioko7qhsPZA",
            name = "Jun I",
            address = "156 Avenue Laurier O, Montreal",
            photoUrl = "https://s3-media1.fl.yelpcdn.com/bphoto/ABGECJSfZWxkhz2oF4h8Fg/o.jpg",
            priceAndCategories = "$$$ - Sushi Bars, Japanese",
            reviewCount = 87,
            rating = Res.drawable.stars_small_4,
        ),
    ),
)

val fakeBusinessDetailsUiModel = BusinessDetailsUiModel(
    id = "FI3PVYBuz5fioko7qhsPZA",
    name = "Jun I",
    address = "156 Avenue Laurier O, Montreal",
    photoUrl = "https://s3-media1.fl.yelpcdn.com/bphoto/ABGECJSfZWxkhz2oF4h8Fg/o.jpg",
    priceAndCategories = "$$$ - Sushi Bars, Japanese",
    reviewCount = 87,
    rating = Res.drawable.stars_small_4,
    openingHours = mapOf("Sunday" to "Closed"),
    reviews = emptyList(),
)

val fakeDateTimeFormatter = object : DateTimeFormatter {
    override fun formatDate(dateTime: String) = "October 27, 2023"
    override fun formatTime(time: String) = "10:00"
}

val fakeResourceProvider = object : ResourceProvider {
    override suspend fun getResourceString(stringResource: StringResource) = "Closed"
    override fun getStarRating(rating: Double) = Res.drawable.stars_small_4
    override suspend fun getDayName(day: Int) = "Sunday"
}
