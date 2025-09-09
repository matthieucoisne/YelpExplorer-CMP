package cmp.yelpexplorer.features.business.data.rest.mapper

import cmp.yelpexplorer.core.utils.DateTimeFormater
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.data.rest.model.BusinessEntity

class BusinessRestMapper(
    private val dateTimeFormater: DateTimeFormater,
) {
    fun map(businessEntities: List<BusinessEntity>): List<Business> {
        return businessEntities.map {
            it.toDomainModel(dateTimeFormater)
        }
    }

    fun map(businessEntity: BusinessEntity): Business {
        return businessEntity.toDomainModel(dateTimeFormater)
    }
}

private fun BusinessEntity.toDomainModel(
    dateTimeFormater: DateTimeFormater,
) = Business(
    id = id,
    name = name,
    photoUrl = imageUrl,
    rating = rating ?: 0.0,
    reviewCount = reviewCount ?: 0,
    address = "${location.address1}, ${location.city}",
    price = price ?: "",
    categories = categories.mapNotNull { it?.title },
    hours = hours?.let { hours ->
        if (hours.isNotEmpty()) {
            hours[0].open.groupBy {
                it.day
            }.mapValues {
                it.value.map { open ->
                    val start = dateTimeFormater.formatTime(open.start)
                    val end = dateTimeFormater.formatTime(open.end)
                    "$start - $end"
                }
            }
        } else {
            null
        }
    },
    reviews = null
)
