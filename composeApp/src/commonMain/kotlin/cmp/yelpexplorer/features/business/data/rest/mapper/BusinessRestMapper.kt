package cmp.yelpexplorer.features.business.data.rest.mapper

import cmp.yelpexplorer.core.utils.DateTimeFormatter
import cmp.yelpexplorer.core.utils.Mapper
import cmp.yelpexplorer.features.business.data.rest.model.BusinessEntity
import cmp.yelpexplorer.features.business.domain.model.Business

interface BusinessListRestMapper : Mapper<List<BusinessEntity>, List<Business>>
interface BusinessDetailsRestMapper : Mapper<BusinessEntity, Business>

class BusinessListRestMapperImpl(
    private val dateTimeFormatter: DateTimeFormatter,
): BusinessListRestMapper {
    override suspend fun map(input: List<BusinessEntity>): List<Business> {
        return input.map {
            it.toDomainModel(dateTimeFormatter)
        }
    }
}

class BusinessDetailsRestMapperImpl(
    private val dateTimeFormatter: DateTimeFormatter,
): BusinessDetailsRestMapper {
    override suspend fun map(input: BusinessEntity): Business {
        return input.toDomainModel(dateTimeFormatter)
    }
}

private fun BusinessEntity.toDomainModel(
    dateTimeFormatter: DateTimeFormatter,
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
                    val start = dateTimeFormatter.formatTime(open.start)
                    val end = dateTimeFormatter.formatTime(open.end)
                    "$start - $end"
                }
            }
        } else {
            null
        }
    },
    reviews = null
)
