package com.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun rememberNavigation(defaultRoute: Route): Navigation {
    return rememberSaveable { AppNavigation(defaultRoute = defaultRoute) }
}

internal class AppNavigation(defaultRoute: Route) : Navigation {

    private val routes: MutableList<Route> = mutableListOf(defaultRoute)
    private val _routeStack: MutableStateFlow<Route> = MutableStateFlow(defaultRoute)
    override val routeStack: StateFlow<Route> = _routeStack

    override fun navigate(route: Route) {
        routes.add(route)
        _routeStack.tryEmit(routes.last())
    }

    override fun navigateAsRoot(route: Route) {
        routes.clear()
        routes.add(route)
        _routeStack.tryEmit(route)
    }

    override fun popLast() {
        routes.removeLast()
        if (routes.isNotEmpty()) _routeStack.tryEmit(routes.last())
    }

    override fun popTo(route: Route) {
        if (!routes.contains(route)) throw IllegalStateException("$route route missing.")
        while (routes.last() != route) {
            routes.removeLast()
            _routeStack.tryEmit(routes.last())
        }
    }
}

interface Navigation {
    val routeStack: StateFlow<Route>
    fun navigate(route: Route)
    fun navigateAsRoot(route: Route)
    fun popLast()
    fun popTo(route: Route)
}