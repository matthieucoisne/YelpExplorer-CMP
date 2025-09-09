package cmp.yelpexplorer.features.business.data.rest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val name: String,
    @SerialName("image_url")
    val imageUrl: String?,
)
