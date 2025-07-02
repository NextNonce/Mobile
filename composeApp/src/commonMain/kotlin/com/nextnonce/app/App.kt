package com.nextnonce.app

import PortfolioScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nextnonce.app.auth.presentation.AuthScreen
import com.nextnonce.app.core.navigation.AddWallet
import com.nextnonce.app.core.navigation.Auth
import com.nextnonce.app.core.navigation.Home
import com.nextnonce.app.core.navigation.Portfolio
import com.nextnonce.app.core.navigation.Start
import com.nextnonce.app.home.presentation.HomeScreen
import com.nextnonce.app.portfolio.presentation.AddPortfolioWalletScreen
import com.nextnonce.app.start.presentation.StartScreen
import com.nextnonce.app.theme.NextNonceTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()
    NextNonceTheme {
        NavHost(
            navController = navController,
            startDestination = Start,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

        ) {
            composable<Start> {
                StartScreen(
                    onUserSignedIn = {
                        navController.navigate(Home) {
                            popUpTo(Start) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onUserNotSignedIn = {
                        navController.navigate(Auth) {
                            popUpTo(Start) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Auth> {
                AuthScreen(
                    onDone = {
                        navController.navigate(Home) {
                            popUpTo(Auth) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Home> {
                HomeScreen(
                    onPortfolioClicked = {
                        navController.navigate(Portfolio)
                    },
                    onAddWalletClicked = { portfolioId ->
                        navController.navigate(AddWallet(portfolioId)) {
                            popUpTo(Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onWalletClicked = { walletId ->

                    },
                )
            }

            composable<AddWallet> { navBackStackEntry ->
                val portfolioId = navBackStackEntry.toRoute<AddWallet>().portfolioId
                AddPortfolioWalletScreen(
                    portfolioId = portfolioId,
                    onBackClicked = {
                        navController.navigateUp()
                    },
                    onWalletAdded = {
                        // Handle wallet added action
                        /*navController.navigate(Home) {
                            popUpTo(AddWallet) { inclusive = true }
                            launchSingleTop = true
                        }*/
                        val addWalletRoute = navBackStackEntry.toRoute<AddWallet>()

                        navController.navigate(Home) {
                            // Change popUpTo to use the route object, not the class name
                            popUpTo(addWalletRoute) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Portfolio> {
                PortfolioScreen(
                    onNavigateBackClicked = {
                        navController.navigateUp()
                    },
                    onTokenItemClicked = { tokenId ->
                        // Handle token item click action
                    },
                )
            }

//                composable<Token> {
//                    TokensListScreen(navController)
//                }
        }
    }
}


/*
var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
* */