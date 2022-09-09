package com.acc.goodwill.data.source.presentation.navigation

import com.navigation.Route

interface DonationRoute : Route {
    val value: Int get() = 0
}

object SearchContribute : DonationRoute {
    override val value: Int = 1
}

object AddContributor : DonationRoute
object AddProduct : DonationRoute {
    override val value: Int = 2
}
object Confirm : DonationRoute {
    override val value: Int = 3
}