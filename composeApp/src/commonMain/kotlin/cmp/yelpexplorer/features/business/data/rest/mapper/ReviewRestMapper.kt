package cmp.yelpexplorer.features.business.data.rest.mapper

import cmp.yelpexplorer.core.utils.DateTimeFormatter
import cmp.yelpexplorer.core.utils.Mapper
import cmp.yelpexplorer.features.business.domain.model.Review
import cmp.yelpexplorer.features.business.data.rest.model.ReviewEntity
import cmp.yelpexplorer.features.business.domain.model.User

interface ReviewRestMapper : Mapper<List<ReviewEntity>, List<Review>>

class ReviewRestMapperImpl(
    private val dateTimeFormatter: DateTimeFormatter,
) : ReviewRestMapper {
    override suspend fun map(input: List<ReviewEntity>): List<Review> {
        return input.map {
            Review(
                user = User(
                    name = it.user.name,
                    imageUrl = it.user.imageUrl
                ),
                text = it.text.replace("\\n+".toRegex(), "\n"),
                rating = it.rating,
                timeCreated = dateTimeFormatter.formatDate(it.timeCreated),
            )
        }
    }
}
