package cmp.yelpexplorer.features.business.presentation.businesslist

import org.jetbrains.compose.resources.DrawableResource

data class BusinessListUiModel(
    val businessList: List<BusinessUiModel>,
)

data class BusinessUiModel(
    val id: String,
    val name: String,
    val photoUrl: String,
    val rating: DrawableResource,
    val reviewCount: Int,
    val address: String,
    val priceAndCategories: String,
)
