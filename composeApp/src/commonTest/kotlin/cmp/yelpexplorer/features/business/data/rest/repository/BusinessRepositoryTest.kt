package cmp.yelpexplorer.features.business.data.rest.repository

import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.model.Review
import cmp.yelpexplorer.features.business.domain.model.User

abstract class BusinessRepositoryTest {
    protected val expectedBusiness = Business(
        id = "FI3PVYBuz5fioko7qhsPZA",
        name = "Jun I",
        photoUrl = "https://s3-media1.fl.yelpcdn.com/bphoto/ABGECJSfZWxkhz2oF4h8Fg/o.jpg",
        rating = 4.0,
        reviewCount = 87,
        address = "156 Avenue Laurier O, Montreal",
        price = "$$$",
        categories = listOf("Sushi Bars", "Japanese"),
        hours = null,
        reviews = null,
    )

    protected val expectedBusinessDetailsWithReviews = expectedBusiness.copy(
        hours = mapOf(
            0 to listOf("06:00 pm - 10:00 pm"),
            1 to listOf("11:30 am - 02:00 pm", "06:00 pm - 10:00 pm"),
            2 to listOf("11:30 am - 02:00 pm", "06:00 pm - 10:00 pm"),
            3 to listOf("11:30 am - 02:00 pm", "06:00 pm - 10:00 pm"),
            4 to listOf("11:30 am - 02:00 pm", "06:00 pm - 11:00 pm"),
            5 to listOf("06:00 pm - 11:00 pm"),
        ),
        reviews = listOf(
            Review(
                user = User(name = "Katerina B.", imageUrl = "https://s3-media2.fl.yelpcdn.com/photo/JMoM8Ea5FkmtvrxZIOz8ZA/o.jpg"),
                text = "Amazing sushi and lovely atmosphere.  This resto is perfect for any special occasion  or that romantic date. The fish is fresh and hits all the right spots....",
                rating = 5,
                timeCreated = "8/2/2023"
            ),
            Review(
                user = User(name = "Wenn S.", imageUrl = "https://s3-media1.fl.yelpcdn.com/photo/vIkg4OoELiWQlmBqz3WFfA/o.jpg"),
                text = "Fantastic modern  sushi spot in Montreal in a restaurant with French decor . Sushi chefs are Japanese and every piece is expertly sliced. We loved our food...",
                rating = 4,
                timeCreated = "7/29/2023"
            ),
            Review(
                user = User(name = "Will S.", imageUrl = "https://s3-media3.fl.yelpcdn.com/photo/1YsS_WGK7nI7u9jQk4RyRw/o.jpg"),
                text = "Atypical of any omakase I've had and wowz was it very good. First off the omakase was made from mostly local québécois products;  plates were  beyond...",
                rating = 5,
                timeCreated = "7/3/2022"
            )
        ),
    )
}
