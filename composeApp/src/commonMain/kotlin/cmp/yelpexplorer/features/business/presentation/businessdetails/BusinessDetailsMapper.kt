package cmp.yelpexplorer.features.business.presentation.businessdetails

import cmp.yelpexplorer.core.utils.Mapper
import cmp.yelpexplorer.core.utils.ResourceProvider
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.model.Review
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.closed

interface BusinessDetailsMapper : Mapper<Business, BusinessDetailsUiModel>

class BusinessDetailsMapperImpl(
    private val resourceProvider: ResourceProvider,
): BusinessDetailsMapper {
    override suspend fun map(input: Business): BusinessDetailsUiModel {
        return input.toBusinessDetailsUiModel(resourceProvider)
    }
}

private suspend fun Business.toBusinessDetailsUiModel(
    resourceProvider: ResourceProvider,
) = BusinessDetailsUiModel(
    id = id,
    name = name,
    photoUrl = photoUrl,
    rating = resourceProvider.getStarRating(rating),
    reviewCount = reviewCount,
    address = address,
    priceAndCategories = StringBuilder().apply {
        if (price.isNotEmpty()) {
            append("$price - ")
        }
        append(categories.joinToString(separator = ", "))
    }.toString(),
    openingHours = buildMap {
        for (i in 0..6) {
            val day = resourceProvider.getDayName(i)
            this[day] = hours?.get(i)?.joinToString(separator = "\n")
                ?: resourceProvider.getResourceString(Res.string.closed)
        }
    },
    reviews = reviews?.map {
        it.toReviewUiModel(resourceProvider)
    } ?: emptyList(),
)

private fun Review.toReviewUiModel(
    resourceProvider: ResourceProvider,
) = ReviewUiModel(
    userName = user.name,
    userImageUrl = user.imageUrl,
    text = text,
    rating = resourceProvider.getStarRating(rating = rating.toDouble()),
    timeCreated = timeCreated,
)
