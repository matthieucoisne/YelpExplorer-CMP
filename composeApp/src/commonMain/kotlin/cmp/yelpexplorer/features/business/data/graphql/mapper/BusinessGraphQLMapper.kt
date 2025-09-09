package cmp.yelpexplorer.features.business.data.graphql.mapper

import cmp.yelpexplorer.BusinessDetailsQuery
import cmp.yelpexplorer.BusinessListQuery
import cmp.yelpexplorer.core.utils.DateTimeFormater
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.model.Review
import cmp.yelpexplorer.features.business.domain.model.User
import cmp.yelpexplorer.fragment.BusinessDetails
import cmp.yelpexplorer.fragment.BusinessSummary

class BusinessGraphQLMapper(
    private val dateTimeFormater: DateTimeFormater,
) {
    fun map(businessList: List<BusinessListQuery.Business?>): List<Business> {
        return businessList.mapNotNull {
            it?.toDomainModel(dateTimeFormater)
        }
    }

    fun map(business: BusinessDetailsQuery.Business): Business {
        return business.toDomainModel(dateTimeFormater)
    }
}

private fun BusinessListQuery.Business.toDomainModel(
    dateTimeFormater: DateTimeFormater,
): Business {
    return mapToBusiness(
        summary = businessSummary,
        dateTimeFormater = dateTimeFormater,
    )
}

private fun BusinessDetailsQuery.Business.toDomainModel(
    dateTimeFormater: DateTimeFormater,
): Business {
    return mapToBusiness(
        summary = businessSummary,
        details = businessDetails,
        dateTimeFormater = dateTimeFormater,
    )
}

private fun mapToBusiness(
    summary: BusinessSummary,
    details: BusinessDetails? = null,
    dateTimeFormater: DateTimeFormater,
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
                    val start = dateTimeFormater.formatTime(open?.start!!)
                    val end = dateTimeFormater.formatTime(open.end!!)
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
                text = it.text!!,
                rating = it.rating!!,
                timeCreated = dateTimeFormater.formatDate(it.time_created!!)
            )
        }
    }
)
