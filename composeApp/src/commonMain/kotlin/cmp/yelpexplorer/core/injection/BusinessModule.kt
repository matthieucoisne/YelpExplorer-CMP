package cmp.yelpexplorer.core.injection

import cmp.yelpexplorer.core.utils.Const
import cmp.yelpexplorer.core.utils.DataSource
import cmp.yelpexplorer.core.utils.ResourceProvider
import cmp.yelpexplorer.core.utils.DateTimeFormater
import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSource
import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSourceImpl
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessGraphQLMapper
import cmp.yelpexplorer.features.business.data.graphql.repository.BusinessGraphQLRepository
import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSource
import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSourceImpl
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.ReviewRestMapper
import cmp.yelpexplorer.features.business.data.rest.repository.BusinessRestRepository
import cmp.yelpexplorer.features.business.domain.repository.BusinessRepository
import cmp.yelpexplorer.features.business.domain.usecase.BusinessDetailsUseCase
import cmp.yelpexplorer.features.business.domain.usecase.BusinessListUseCase
import cmp.yelpexplorer.features.business.domain.usecase.BusinessDetailsUseCaseImpl
import cmp.yelpexplorer.features.business.domain.usecase.BusinessListUseCaseImpl
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsMapper
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsMapperImpl
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsViewModel
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListMapper
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListMapperImpl
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val businessModule = module {
    viewModelOf(::BusinessListViewModel)
    viewModelOf(::BusinessDetailsViewModel)

    factoryOf(::BusinessListUseCaseImpl).bind(BusinessListUseCase::class)
    factoryOf(::BusinessDetailsUseCaseImpl).bind(BusinessDetailsUseCase::class)

    singleOf(::BusinessListMapperImpl).bind(BusinessListMapper::class)
    singleOf(::BusinessDetailsMapperImpl).bind(BusinessDetailsMapper::class)

    singleOf(::DateTimeFormater)
    singleOf(::ResourceProvider)

    when (Const.DATASOURCE) {
        DataSource.REST -> {
            singleOf(::BusinessRestDataSourceImpl).bind(BusinessRestDataSource::class)
            singleOf(::BusinessRestMapper)
            singleOf(::ReviewRestMapper)
            single<BusinessRepository> {
                BusinessRestRepository(
                    businessRestDataSource = get(),
                    businessRestMapper = get(),
                    reviewRestMapper = get(),
                    ioDispatcher = get(named(Const.DISPATCHER_IO))
                )
            }
        }
        DataSource.GRAPHQL -> {
            singleOf(::BusinessGraphQLDataSourceImpl).bind(BusinessGraphQLDataSource::class)
            singleOf(::BusinessGraphQLMapper)
            single<BusinessRepository> {
                BusinessGraphQLRepository(
                    businessGraphQLDataSource = get(),
                    businessGraphQLMapper = get(),
                    ioDispatcher = get(named(Const.DISPATCHER_IO))
                )
            }
        }
    }
}
