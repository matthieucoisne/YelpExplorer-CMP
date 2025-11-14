package cmp.yelpexplorer.core.utils

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.closed
import yelpexplorer_cmp.composeapp.generated.resources.friday
import yelpexplorer_cmp.composeapp.generated.resources.monday
import yelpexplorer_cmp.composeapp.generated.resources.saturday
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_0
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_1
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_1_half
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_2
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_2_half
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_3
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_3_half
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_4
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_4_half
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_5
import yelpexplorer_cmp.composeapp.generated.resources.sunday
import yelpexplorer_cmp.composeapp.generated.resources.thursday
import yelpexplorer_cmp.composeapp.generated.resources.tuesday
import yelpexplorer_cmp.composeapp.generated.resources.wednesday

interface ResourceProvider {
    suspend fun getResourceString(stringResource: StringResource): String
    fun getStarRating(rating: Double): DrawableResource
    suspend fun getDayName(day: Int): String
}

class ResourceProviderImpl : ResourceProvider {
    override suspend fun getResourceString(stringResource: StringResource): String {
        return when (stringResource) {
            Res.string.closed -> getString(Res.string.closed)
            else -> throw IllegalArgumentException("Invalid stringResource: $stringResource.")
        }
    }

    override fun getStarRating(rating: Double): DrawableResource = when (rating) {
        in 0.8..1.2 -> Res.drawable.stars_small_1
        in 1.3..1.7 -> Res.drawable.stars_small_1_half
        in 1.8..2.2 -> Res.drawable.stars_small_2
        in 2.3..2.7 -> Res.drawable.stars_small_2_half
        in 2.8..3.2 -> Res.drawable.stars_small_3
        in 3.3..3.7 -> Res.drawable.stars_small_3_half
        in 3.8..4.2 -> Res.drawable.stars_small_4
        in 4.3..4.7 -> Res.drawable.stars_small_4_half
        in 4.8..5.0 -> Res.drawable.stars_small_5
        else -> Res.drawable.stars_small_0
    }

    override suspend fun getDayName(day: Int): String = when (day) {
        0 -> getString(Res.string.monday)
        1 -> getString(Res.string.tuesday)
        2 -> getString(Res.string.wednesday)
        3 -> getString(Res.string.thursday)
        4 -> getString(Res.string.friday)
        5 -> getString(Res.string.saturday)
        6 -> getString(Res.string.sunday)
        else -> throw IllegalArgumentException("Invalid day: $day. Must be between 0 and 6.")
    }
}
