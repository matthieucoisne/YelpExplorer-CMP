package cmp.yelpexplorer.features.business.data.graphql.mapper

import cmp.yelpexplorer.BusinessDetailsQuery
import cmp.yelpexplorer.BusinessListQuery
import cmp.yelpexplorer.core.utils.DateTimeFormatter
import cmp.yelpexplorer.core.utils.Mapper
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.model.Review
import cmp.yelpexplorer.features.business.domain.model.User
import cmp.yelpexplorer.fragment.BusinessDetails
import cmp.yelpexplorer.fragment.BusinessSummary

interface BusinessListGraphQLMapper : Mapper<List<BusinessListQuery.Business?>, List<Business>>
interface BusinessDetailsGraphQLMapper : Mapper<BusinessDetailsQuery.Business, Business>

class BusinessListGraphQLMapperImpl(
    private val dateTimeFormatter: DateTimeFormatter,
): BusinessListGraphQLMapper {
    override suspend fun map(input: List<BusinessListQuery.Business?>): List<Business> {
        return input.mapNotNull {
            it?.toDomainModel(dateTimeFormatter)
        }
    }
}

class BusinessDetailsGraphQLMapperImpl(
    private val dateTimeFormatter: DateTimeFormatter,
): BusinessDetailsGraphQLMapper {
    override suspend fun map(input: BusinessDetailsQuery.Business): Business {
        return input.toDomainModel(dateTimeFormatter)
    }
}

private fun BusinessListQuery.Business.toDomainModel(
    dateTimeFormatter: DateTimeFormatter,
): Business {
    return mapToBusiness(
        summary = businessSummary,
        dateTimeFormatter = dateTimeFormatter,
    )
}

private fun BusinessDetailsQuery.Business.toDomainModel(
    dateTimeFormatter: DateTimeFormatter,
): Business {
    return mapToBusiness(
        summary = businessSummary,
        details = businessDetails,
        dateTimeFormatter = dateTimeFormatter,
    )
}

private fun mapToBusiness(
    summary: BusinessSummary,
    details: BusinessDetails? = null,
    dateTimeFormatter: DateTimeFormatter,
) = Business(
    id = summary.id!!,
    name = summary.name!!,
    photoUrl = summary.photos!!.firstNotNullOfOrNull { it } ?: "",
    rating = summary.rating ?: 0.0,
    reviewCount = summary.review_count ?: 0,
    address = summary.location!!.let { "${it.address1!!}, ${it.city!!}" },
    price = summary.price ?: "",
    categories = summary.categories!!.mapNotNull { it?.title },
    hours = details?.hours?.let { hours ->
        if (hours.isNotEmpty()) {
            hours[0]?.open?.groupBy {
                it!!.day!!
            }?.mapValues {
                it.value.map { open ->
                    val start = dateTimeFormatter.formatTime(open?.start!!)
                    val end = dateTimeFormatter.formatTime(open.end!!)
                    "$start - $end"
                }
            }
        } else {
            emptyMap()
        }
    },
    reviews = details?.reviews?.let { reviews ->
        reviews.map {
            val user = it!!.user!!
            Review(
                user = User(user.name!!, user.image_url),
                text = it.text!!.replace("\\n+".toRegex(), "\n"),
                rating = it.rating!!,
                timeCreated = dateTimeFormatter.formatDate(it.time_created!!),
            )
        }
    }
)
