package cmp.yelpexplorer.features.business.presentation.businesslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import cmp.yelpexplorer.core.utils.StarsProvider
import cmp.yelpexplorer.core.theme.YelpExplorerTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.app_name
import yelpexplorer_cmp.composeapp.generated.resources.business_name
import yelpexplorer_cmp.composeapp.generated.resources.business_price
import yelpexplorer_cmp.composeapp.generated.resources.business_reviews_count
import yelpexplorer_cmp.composeapp.generated.resources.error_something_went_wrong
import yelpexplorer_cmp.composeapp.generated.resources.loading
import yelpexplorer_cmp.composeapp.generated.resources.placeholder_business_list

@Composable
fun BusinessListScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessListViewModel = koinViewModel(),
    onBusinessClicked: (String) -> Unit
) {
    val viewState by viewModel.uiState.collectAsStateWithLifecycle()

    BusinessListContent(
        modifier = modifier,
        viewState = viewState,
        onRefresh = {
            viewModel.getBusinessList()
        },
        onBusinessClicked = onBusinessClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessListContent(
    modifier: Modifier = Modifier,
    viewState: BusinessListViewModel.ViewState,
    onRefresh: () -> Unit,
    onBusinessClicked: (String) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(resource = Res.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            state = pullToRefreshState,
            onRefresh = onRefresh,
            isRefreshing = viewState is BusinessListViewModel.ViewState.ShowLoading,
            modifier = modifier.padding(innerPadding)
        ) {
            when (viewState) {
                is BusinessListViewModel.ViewState.ShowBusinessList -> {
                    BusinessList(
                        data = viewState.businessList.businessList,
                        onBusinessClicked = onBusinessClicked
                    )
                }

                is BusinessListViewModel.ViewState.ShowError -> {
                    CenteredText(
                        text = stringResource(resource = Res.string.error_something_went_wrong)
                    )
                }

                is BusinessListViewModel.ViewState.ShowLoading -> {
                    if (viewState.businessList != null) {
                        BusinessList(
                            data = viewState.businessList.businessList,
                            onBusinessClicked = {} // dont allow clicking while loading
                        )
                    } else {
                        CenteredText(
                            text = stringResource(resource = Res.string.loading)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CenteredText(
    modifier: Modifier = Modifier,
    text: String,
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
    modifier: Modifier = Modifier,
    data: List<BusinessUiModel>,
    onBusinessClicked: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 8.dp),
    ) {
        itemsIndexed(items = data) { index, businessUiModel ->
            BusinessListItem(
                businessUiModel = businessUiModel,
                position = index + 1,
                onBusinessClicked = onBusinessClicked
            )
        }
    }
}

@Composable
fun BusinessListItem(
    modifier: Modifier = Modifier,
    businessUiModel: BusinessUiModel,
    position: Int,
    onBusinessClicked: (String) -> Unit,
) {
    val price = if (businessUiModel.price.isNotEmpty()) {
        stringResource(resource = Res.string.business_price, businessUiModel.price)
    } else ""

    Card(
        onClick = {
            onBusinessClicked(businessUiModel.id)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier,
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
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
                        businessUiModel.name.uppercase()
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
                    modifier = Modifier
                ) {
                    Image(
                        painter = painterResource(resource = StarsProvider.getDrawableResource(businessUiModel.rating)),
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
                    text = "$price${businessUiModel.categories}",
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
fun PreviewLoading() {
    YelpExplorerTheme(darkTheme = true) {
        Scaffold {
            CenteredText(
                text = stringResource(Res.string.loading),
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview
@Composable
fun PreviewBusinessList() {
    YelpExplorerTheme(darkTheme = true) {
        Scaffold {
            BusinessList(
                modifier = Modifier.padding(it),
                data = MutableList(10) {
                    BusinessUiModel(
                        id = "id",
                        name = "Jun i",
                        photoUrl = "",
                        rating = 4.5,
                        reviewCount = 2,
                        address = "4567 Rue St-Denis, Montreal",
                        price = "$$",
                        categories = "Sushi Bars, Japanese"
                    )
                },
                onBusinessClicked = {}
            )
        }
    }
}
