package cmp.yelpexplorer.features.business.presentation.businesslist

import cmp.yelpexplorer.core.utils.Mapper
import cmp.yelpexplorer.core.utils.ResourceProvider
import cmp.yelpexplorer.features.business.domain.model.Business

interface BusinessListMapper: Mapper<List<Business>, BusinessListUiModel>

class BusinessListMapperImpl(
    private val resourceProvider: ResourceProvider,
):BusinessListMapper {
    override suspend fun map(input: List<Business>): BusinessListUiModel {
        return BusinessListUiModel(
            businessList = input.map {
                it.toBusinessUiModel(resourceProvider)
            }
        )
    }
}

private fun Business.toBusinessUiModel(
    resourceProvider: ResourceProvider,
) = BusinessUiModel(
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
)
