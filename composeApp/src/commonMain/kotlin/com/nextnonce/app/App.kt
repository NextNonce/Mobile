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
import com.nextnonce.app.core.navigation.Wallet
import com.nextnonce.app.home.presentation.HomeScreen
import com.nextnonce.app.portfolio.presentation.AddPortfolioWalletScreen
import com.nextnonce.app.start.presentation.StartScreen
import com.nextnonce.app.theme.NextNonceTheme
import com.nextnonce.app.wallet.presentation.WalletScreen
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
                    onPortfolioClicked = { portfolioId ->
                        navController.navigate(Portfolio(portfolioId)) {
                            popUpTo(Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onAddWalletClicked = { portfolioId ->
                        navController.navigate(AddWallet(portfolioId)) {
                            popUpTo(Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onWalletClicked = { walletId, walletName ->
                        navController.navigate(Wallet(walletId, walletName)) {
                            popUpTo(Home) { inclusive = false }
                            launchSingleTop = true
                        }
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
                        navController.navigateUp()
                    }
                )
            }

            composable<Portfolio> { navBackStackEntry ->
                val portfolioId = navBackStackEntry.toRoute<Portfolio>().portfolioId
                PortfolioScreen(
                    id = portfolioId,
                    onBackClicked = {
                        navController.navigateUp()
                    },
                )
            }

            composable<Wallet> { navBackStackEntry ->
                val walletId = navBackStackEntry.toRoute<Wallet>().walletId
                val walletName = navBackStackEntry.toRoute<Wallet>().walletName
                WalletScreen(
                    id = walletId,
                    name = walletName,
                    onBackClicked = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}