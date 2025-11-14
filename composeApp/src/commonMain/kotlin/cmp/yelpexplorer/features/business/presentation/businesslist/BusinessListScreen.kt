package cmp.yelpexplorer.features.business.presentation.businesslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
import yelpexplorer_cmp.composeapp.generated.resources.business_name
import yelpexplorer_cmp.composeapp.generated.resources.business_reviews_count
import yelpexplorer_cmp.composeapp.generated.resources.error_something_went_wrong
import yelpexplorer_cmp.composeapp.generated.resources.loading
import yelpexplorer_cmp.composeapp.generated.resources.placeholder_business_list
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_4_half

@Composable
fun BusinessListScreen(
    viewModel: BusinessListViewModel = koinViewModel(),
    onBusinessClicked: (String) -> Unit
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    BusinessListContent(
        viewState = viewState,
        onEvent = {
            if (it is BusinessListScreenEvent.OnBusinessClicked) {
                onBusinessClicked(it.business.id)
            } // else viewModel.onEvent(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessListContent(
    viewState: BusinessListViewState,
    onEvent: (BusinessListScreenEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(resource = Res.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { innerPadding ->
        when (viewState) {
            is BusinessListViewState.ShowBusinessList -> {
                BusinessList(
                    modifier = Modifier.padding(
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                    ),
                    data = viewState.businessList.businessList,
                    onEvent = onEvent
                )
            }

            is BusinessListViewState.ShowError -> {
                CenteredText(
                    modifier = Modifier.padding(innerPadding),
                    text = stringResource(Res.string.error_something_went_wrong)
                )
            }

            is BusinessListViewState.ShowLoading -> {
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
fun BusinessList(
    data: List<BusinessUiModel>,
    onEvent: (BusinessListScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding(),
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(items = data) { index, businessUiModel ->
            BusinessListItem(
                businessUiModel = businessUiModel,
                position = index + 1,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun BusinessListItem(
    businessUiModel: BusinessUiModel,
    position: Int,
    onEvent: (BusinessListScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = {
            onEvent(BusinessListScreenEvent.OnBusinessClicked(businessUiModel))
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(4.dp),
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(businessUiModel.photoUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(resource = Res.drawable.placeholder_business_list),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(120.dp),
            )
            Spacer(modifier = Modifier.size(4.dp))
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Text(
                    text = stringResource(
                        resource = Res.string.business_name,
                        position,
                        businessUiModel.name
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier,
                ) {
                    Image(
                        painter = painterResource(resource = businessUiModel.rating),
                        contentDescription = null,
                        modifier = Modifier
                            .width(82.dp)
                            .height(14.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = pluralStringResource(
                            resource = Res.plurals.business_reviews_count,
                            quantity = businessUiModel.reviewCount,
                            businessUiModel.reviewCount
                        ),
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = businessUiModel.priceAndCategories,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = businessUiModel.address,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBusinessList() {
    YelpExplorerTheme(darkTheme = true) {
        BusinessListContent(
            viewState = BusinessListViewState.ShowBusinessList(
                BusinessListUiModel(
                    businessList = List(10) {
                        BusinessUiModel(
                            id = "id",
                            name = "Jun i",
                            photoUrl = "fake.url/business1.png",
                            rating = Res.drawable.stars_small_4_half,
                            reviewCount = 2,
                            address = "4567 Rue St-Denis, Montreal",
                            priceAndCategories = "$$ - Sushi Bars, Japanese",
                        )
                    }
                )
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun PreviewLoading() {
    YelpExplorerTheme(darkTheme = true) {
        BusinessListContent(
            viewState = BusinessListViewState.ShowLoading,
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun PreviewError() {
    YelpExplorerTheme(darkTheme = true) {
        BusinessListContent(
            viewState = BusinessListViewState.ShowError,
            onEvent = {}
        )
    }
}
