package cmp.yelpexplorer.features.business.data.rest.mapper

import cmp.yelpexplorer.core.utils.DateTimeFormater
import cmp.yelpexplorer.features.business.domain.model.Review
import cmp.yelpexplorer.features.business.data.rest.model.ReviewEntity
import cmp.yelpexplorer.features.business.domain.model.User

class ReviewRestMapper(
    private val dateTimeFormater: DateTimeFormater,
) {
    fun map(reviewEntities: List<ReviewEntity>): List<Review> {
        return reviewEntities.map {
            Review(
                user = User(
                    name = it.user.name,
                    imageUrl = it.user.imageUrl
                ),
                text = it.text.replace("\\n+".toRegex(), "\n"),
                rating = it.rating,
                timeCreated = dateTimeFormater.formatDate(it.timeCreated),
            )
        }
    }
}
