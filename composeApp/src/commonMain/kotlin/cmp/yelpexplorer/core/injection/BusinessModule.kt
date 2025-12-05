package cmp.yelpexplorer.core.injection

import cmp.yelpexplorer.core.utils.Const
import cmp.yelpexplorer.core.utils.DataSource
import cmp.yelpexplorer.core.utils.DateTimeFormatter
import cmp.yelpexplorer.core.utils.DateTimeFormatterImpl
import cmp.yelpexplorer.core.utils.ResourceProvider
import cmp.yelpexplorer.core.utils.ResourceProviderImpl
import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSource
import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSourceImpl
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessDetailsGraphQLMapper
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessDetailsGraphQLMapperImpl
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessListGraphQLMapper
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessListGraphQLMapperImpl
import cmp.yelpexplorer.features.business.data.graphql.repository.BusinessGraphQLRepository
import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSource
import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSourceImpl
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessDetailsRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessDetailsRestMapperImpl
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessListRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessListRestMapperImpl
import cmp.yelpexplorer.features.business.data.rest.mapper.ReviewRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.ReviewRestMapperImpl
import cmp.yelpexplorer.features.business.data.rest.repository.BusinessRestRepository
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import cmp.yelpexplorer.features.business.domain.usecase.GetBusinessDetailsUseCase
import cmp.yelpexplorer.features.business.domain.usecase.GetBusinessDetailsUseCaseImpl
import cmp.yelpexplorer.features.business.domain.usecase.GetBusinessListUseCase
import cmp.yelpexplorer.features.business.domain.usecase.GetBusinessListUseCaseImpl
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsMapper
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsMapperImpl
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsViewModel
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListMapper
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListMapperImpl
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val businessModule = module {
    viewModel<BusinessListViewModel> {
        BusinessListViewModel(
            getBusinessListUseCase = get(),
            businessListMapper = get(),
            mainDispatcher = get(named(Const.DISPATCHER_MAIN))
        )
    }
    viewModel<BusinessDetailsViewModel> { params ->
        BusinessDetailsViewModel(
            businessId = params.get(),
            getBusinessDetailsUseCase = get(),
            businessDetailsMapper = get()
        )
    }

    factoryOf(::GetBusinessListUseCaseImpl).bind(GetBusinessListUseCase::class)
    factoryOf(::GetBusinessDetailsUseCaseImpl).bind(GetBusinessDetailsUseCase::class)

    singleOf(::BusinessListMapperImpl).bind(BusinessListMapper::class)
    singleOf(::BusinessDetailsMapperImpl).bind(BusinessDetailsMapper::class)

    singleOf(::DateTimeFormatterImpl).bind(DateTimeFormatter::class)
    singleOf(::ResourceProviderImpl).bind(ResourceProvider::class)

    when (Const.DATASOURCE) {
        DataSource.REST -> {
            singleOf(::BusinessRestDataSourceImpl).bind(BusinessRestDataSource::class)
            singleOf(::BusinessListRestMapperImpl).bind(BusinessListRestMapper::class)
            singleOf(::BusinessDetailsRestMapperImpl).bind(BusinessDetailsRestMapper::class)
            singleOf(::ReviewRestMapperImpl).bind(ReviewRestMapper::class)
            single<BusinessRepository> {
                BusinessRestRepository(
                    businessRestDataSource = get(),
                    businessListRestMapper = get(),
                    businessDetailsRestMapper = get(),
                    reviewRestMapper = get(),
                    ioDispatcher = get(named(Const.DISPATCHER_IO))
                )
            }
        }
        DataSource.GRAPHQL -> {
            singleOf(::BusinessGraphQLDataSourceImpl).bind(BusinessGraphQLDataSource::class)
            singleOf(::BusinessListGraphQLMapperImpl).bind(BusinessListGraphQLMapper::class)
            singleOf(::BusinessDetailsGraphQLMapperImpl).bind(BusinessDetailsGraphQLMapper::class)
            single<BusinessRepository> {
                BusinessGraphQLRepository(
                    businessGraphQLDataSource = get(),
                    businessListGraphQLMapper = get(),
                    businessDetailsGraphQLMapper = get(),
                    ioDispatcher = get(named(Const.DISPATCHER_IO))
                )
            }
        }
    }
}
