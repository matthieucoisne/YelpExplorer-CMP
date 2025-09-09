package cmp.yelpexplorer.features.business.domain.model

data class Review(
    val user: User,
    val text: String,
    val rating: Int,
    val timeCreated: String,
)
