package space.algoritmos.habit_tracker.ui.screens.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import space.algoritmos.habit_tracker.R

@Composable
fun HabitDrawerContent(
    isLoggedIn: Boolean,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onStatsClick: () -> Unit,
    onAddHabitClick: () -> Unit,
    onSyncClick: () -> Unit,
    drawerState: DrawerState,
) {
    val coroutineScope = rememberCoroutineScope()

    val themeText = if (isDarkTheme) {
        stringResource(id = R.string.drawer_enable_light_mode)
    } else {
        stringResource(id = R.string.drawer_enable_dark_mode)
    }
    val themeIcon = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.DarkMode

    ModalDrawerSheet(
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.drawer_menu_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider()
            HorizontalDivider()

            NavigationDrawerItem(
                label = { Text(stringResource(id = R.string.drawer_statistics), fontSize = 18.sp) },
                selected = false,
                onClick = {
                    onStatsClick()
                    coroutineScope.launch { drawerState.close() }
                },
                modifier = Modifier.padding(top = 8.dp)
            )

            NavigationDrawerItem(
                label = { Text(stringResource(id = R.string.drawer_add_habit), fontSize = 18.sp) },
                selected = false,
                onClick = {
                    onAddHabitClick()
                    coroutineScope.launch { drawerState.close() }
                }
            )

            NavigationDrawerItem(
                label = { Text(stringResource(id = R.string.drawer_sync), fontSize = 18.sp) },
                selected = false,
                onClick = {
                    onSyncClick()
                    coroutineScope.launch { drawerState.close() }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            NavigationDrawerItem(
                label = { Text(themeText, fontSize = 18.sp) },
                icon = { Icon(imageVector = themeIcon, contentDescription = null) },
                selected = false,
                onClick = {
                    onToggleTheme()
                    coroutineScope.launch { drawerState.close() }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider()
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            val loginLogoutText = if (isLoggedIn) {
                stringResource(id = R.string.drawer_logout)
            } else {
                stringResource(id = R.string.drawer_login)
            }

            NavigationDrawerItem(
                label = { Text(loginLogoutText, fontSize = 18.sp) },
                selected = false,
                onClick = {
                    coroutineScope.launch {
                        if (isLoggedIn) {
                            onLogoutClick()
                        } else {
                            onLoginClick()
                        }
                        drawerState.close()
                    }
                }
            )
        }
    }
}
