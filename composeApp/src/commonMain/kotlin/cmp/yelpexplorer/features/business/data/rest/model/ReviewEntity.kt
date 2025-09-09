package cmp.yelpexplorer.features.business.data.rest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewListResponse(
    val reviews: List<ReviewEntity>,
)

@Serializable
data class ReviewEntity(
    val user: UserEntity,
    val text: String,
    val rating: Int,
    @SerialName("time_created")
    val timeCreated: String,
)
