package com.nextnonce.app

import com.nextnonce.app.portfolio.presentation.PortfolioScreen
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
            /**
             * Start Screen
             * Checks if the user is signed in and navigates to the appropriate screen.
             * If signed in, navigates to HomeScreen; otherwise, navigates to AuthScreen.
             */
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

            /**
             * Auth Screen
             * Allows the user to sign in or sign up.
             * Reached from StartScreen if the user is not signed in.
             */
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

            /**
             * Home Screen
             * Displays the user's default portfolio and its wallets.
             * Allows navigation to Portfolio and Wallet screens.
             * Provides option to navigate to AddWallet screen.
             * Reached from StartScreen if the user is signed in.
             * Reached from AuthScreen after successful sign-in or sign-up.
             */
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

            /**
             * Add Wallet Screen
             * Allows the user to add a new wallet to a specific portfolio.
             * Reached from HomeScreen when the "Add Wallet" button is clicked.
             */
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

            /**
             * Portfolio Screen
             * Displays the details of a specific portfolio.
             * Reached from HomeScreen when a portfolio is clicked.
             */
            composable<Portfolio> { navBackStackEntry ->
                val portfolioId = navBackStackEntry.toRoute<Portfolio>().portfolioId
                PortfolioScreen(
                    id = portfolioId,
                    onBackClicked = {
                        navController.navigateUp()
                    },
                )
            }

            /**
             * Wallet Screen
             * Displays the details of a specific wallet.
             * Reached from HomeScreen when a wallet is clicked.
             */
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