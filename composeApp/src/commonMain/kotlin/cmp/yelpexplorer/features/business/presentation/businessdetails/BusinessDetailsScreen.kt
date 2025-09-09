package cmp.yelpexplorer.features.business.presentation.businessdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp.yelpexplorer.core.theme.YelpExplorerTheme
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.app_name
import yelpexplorer_cmp.composeapp.generated.resources.business_reviews_count
import yelpexplorer_cmp.composeapp.generated.resources.error_something_went_wrong
import yelpexplorer_cmp.composeapp.generated.resources.latest_reviews
import yelpexplorer_cmp.composeapp.generated.resources.loading
import yelpexplorer_cmp.composeapp.generated.resources.opening_hours
import yelpexplorer_cmp.composeapp.generated.resources.placeholder_business_list
import yelpexplorer_cmp.composeapp.generated.resources.placeholder_user
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_3_half
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_5

@Composable
fun BusinessDetailsScreen(
    viewModel: BusinessDetailsViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    BusinessDetailsContent(
        viewState = viewState,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDetailsContent(
    viewState: BusinessDetailsViewState,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(resource = Res.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        when (viewState) {
            is BusinessDetailsViewState.ShowBusinessDetails -> {
                BusinessDetails(
                    modifier = Modifier.padding(
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                    ),
                    businessDetailsUiModel = viewState.businessDetails,
                )
            }

            is BusinessDetailsViewState.ShowError -> {
                CenteredText(
                    modifier = Modifier.padding(innerPadding),
                    text = stringResource(resource = Res.string.error_something_went_wrong)
                )
            }

            is BusinessDetailsViewState.ShowLoading -> {
                CenteredText(
                    modifier = Modifier.padding(innerPadding),
                    text = stringResource(resource = Res.string.loading)
                )
            }
        }
    }
}

@Composable
fun CenteredText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BusinessDetails(
    businessDetailsUiModel: BusinessDetailsUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        BusinessPhoto(
            modifier = Modifier,
            businessDetailsUiModel = businessDetailsUiModel,
        )
        Spacer(modifier = Modifier.size(16.dp))
        BusinessInfo(
            modifier = Modifier.padding(horizontal = 8.dp),
            businessDetailsUiModel = businessDetailsUiModel,
        )
        Spacer(modifier = Modifier.size(16.dp))
        BusinessHours(
            modifier = Modifier.padding(horizontal = 8.dp),
            openingHours = businessDetailsUiModel.openingHours,
        )
        Spacer(modifier = Modifier.size(16.dp))
        BusinessReviews(
            modifier = Modifier.padding(horizontal = 8.dp),
            data = businessDetailsUiModel.reviews
        )
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
    }
}

@Composable
fun BusinessPhoto(
    businessDetailsUiModel: BusinessDetailsUiModel,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(businessDetailsUiModel.photoUrl)
            .crossfade(true)
            .build(),
        error = painterResource(resource = Res.drawable.placeholder_business_list),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth(),
    )
}

@Composable
fun BusinessInfo(
    businessDetailsUiModel: BusinessDetailsUiModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = businessDetailsUiModel.name.uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.size(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Image(
                painter = painterResource(resource = businessDetailsUiModel.rating),
                contentDescription = null,
                modifier = Modifier
                    .width(82.dp)
                    .height(14.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = pluralStringResource(
                    resource = Res.plurals.business_reviews_count,
                    quantity = businessDetailsUiModel.reviewCount,
                    businessDetailsUiModel.reviewCount
                ),
                fontSize = 12.sp,
            )
        }
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = businessDetailsUiModel.priceAndCategories,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = businessDetailsUiModel.address,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun BusinessHours(
    openingHours: Map<String, String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(resource = Res.string.opening_hours),
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.size(4.dp))
        openingHours.forEach { (day, hours) ->
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Text(
                    text = day,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 1.4.em,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = hours,
                    fontSize = 12.sp,
                    lineHeight = 1.4.em,
                    modifier = Modifier.weight(3f)
                )
            }
        }
    }
}

@Composable
fun BusinessReviews(
    data: List<ReviewUiModel>,
    modifier: Modifier = Modifier,
) {
    if (data.isNotEmpty()) {
        Column(modifier = modifier) {
            Text(
                text = stringResource(resource = Res.string.latest_reviews),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(4.dp))
            (data.indices).forEach {
                BusinessReview(
                    modifier = Modifier,
                    reviewUiModel = data[it]
                )
                Spacer(modifier = Modifier.size(4.dp))
            }
        }
    }
}

@Composable
fun BusinessReview(
    reviewUiModel: ReviewUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(4.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(reviewUiModel.userImageUrl)
                        .crossfade(true)
                        .build(),
                    error = painterResource(resource = Res.drawable.placeholder_user),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column {
                    Text(
                        text = reviewUiModel.userName,
                        fontWeight = FontWeight.Bold,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(resource = reviewUiModel.rating),
                            contentDescription = null,
                            modifier = Modifier
                                .width(82.dp)
                                .height(14.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = reviewUiModel.timeCreated,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = reviewUiModel.text,
                fontSize = 13.sp,
                lineHeight = 1.4.em,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
fun PreviewBusinessDetails() {
    YelpExplorerTheme(darkTheme = true) {
        Scaffold {
            BusinessDetails(
                modifier = Modifier.padding(it),
                businessDetailsUiModel = BusinessDetailsUiModel(
                    id = "id",
                    name = "Jun i",
                    photoUrl = "fake.url/restaurant.png",
                    rating = Res.drawable.stars_small_3_half,
                    reviewCount = 2,
                    address = "4567 Rue St-Denis, Montreal",
                    priceAndCategories = "$$ - Sushi Bars, Japanese",
                    openingHours = mapOf(
                        "Monday" to "Closed",
                        "Tuesday" to "Closed",
                        "Wednesday" to "4:30 PM - 10:00 PM",
                        "Thursday" to "4:30 PM - 10:00 PM",
                        "Friday" to "4:30 PM - 10:00 PM",
                        "Saturday" to "10:30 AM - 1:00 PM\n4:30 PM - 10:00 PM",
                        "Sunday" to "4:30 PM - 10:00 PM",
                    ),
                    reviews = listOf(
                        ReviewUiModel(
                            userName = "Matthieu C.",
                            userImageUrl = "fake.url/user1.png",
                            text = "This place is well worth the money. You should try the tuna sushi!",
                            rating = Res.drawable.stars_small_5,
                            timeCreated = "4/21/2020"
                        ),
                        ReviewUiModel(
                            userName = "Jean G.",
                            userImageUrl = "fake.url/user2.png",
                            text = "Amazing!",
                            rating = Res.drawable.stars_small_5,
                            timeCreated = "4/15/2025"
                        )
                    ),
                ),
            )
        }
    }
}
