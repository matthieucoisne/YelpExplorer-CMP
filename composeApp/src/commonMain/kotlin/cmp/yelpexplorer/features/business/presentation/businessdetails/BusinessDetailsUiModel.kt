package cmp.yelpexplorer.features.business.presentation.businessdetails

import org.jetbrains.compose.resources.DrawableResource

data class BusinessDetailsUiModel(
    val id: String,
    val name: String,
    val photoUrl: String,
    val rating: DrawableResource,
    val reviewCount: Int,
    val address: String,
    val priceAndCategories: String,
    val openingHours: Map<String, String>,
    val reviews: List<ReviewUiModel>,
)

data class ReviewUiModel(
    val userName: String,
    val userImageUrl: String?,
    val text: String,
    val rating: DrawableResource,
    val timeCreated: String,
)
