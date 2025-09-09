package cmp.yelpexplorer.features.business.data.rest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusinessListResponse(
    val businesses: List<BusinessEntity>,
)

@Serializable
data class BusinessEntity(
    val id: String,
    val name: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("review_count")
    val reviewCount: Int?,
    val categories: List<CategoryEntity?>,
    val rating: Double?,
    val price: String?,
    val location: LocationEntity,
    val hours: List<HoursEntity>?,
    val reviews: List<ReviewEntity>?,
) {
    @Serializable
    data class CategoryEntity(
        val title: String,
    )

    @Serializable
    data class LocationEntity(
        val address1: String,
        val city: String,
    )

    @Serializable
    data class HoursEntity(
        val open: List<OpenEntity>,
    )

    @Serializable
    data class OpenEntity(
        val start: String,
        val end: String,
        val day: Int,
    )
}
