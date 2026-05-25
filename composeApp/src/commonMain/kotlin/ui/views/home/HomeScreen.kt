package ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import networking.models.DashboardResponse
import ui.components.MetricGrid
import ui.components.MetricItem
import ui.theme.AppColors

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    firstName:         String,
    lastName:          String,
    dashboardResponse: DashboardResponse?,
    isRefreshing:      Boolean,
    onRefresh:         () -> Unit,
    onLogout:          () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh  = onRefresh,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = AppColors.Teal,
                elevation       = 0.dp,
                title           = {
                    Text("Dashboard", color = AppColors.White,
                        fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = AppColors.White,
                            fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                },
            )
        },
        backgroundColor = AppColors.Teal,
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(pullRefreshState),
        ) {
            if (dashboardResponse == null && !isRefreshing) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.White)
                }
            } else {
                val dashboard = dashboardResponse?.dashboard

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    // ── Welcome ──────────────────────────────────────────────
                    Column(
                        modifier            = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Text("Welcome Back,", fontSize = 16.sp, color = AppColors.WhiteA60)
                        Text(
                            text       = "$firstName $lastName",
                            fontSize   = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color      = AppColors.White,
                        )
                    }

                    // ── Overview grid ─────────────────────────────────────────
                    MetricGrid(items = listOf(
                        MetricItem("${dashboard?.totalCustomers ?: 0}", "Customers"),
                        MetricItem("${dashboard?.allLoans       ?: 0}", "All\nLoans"),
                        MetricItem("${dashboard?.draftLoans     ?: 0}", "Draft\nLoans"),
                        MetricItem("${dashboard?.appliedLoans   ?: 0}", "Applied\nLoans"),
                        MetricItem("${dashboard?.reapplyLoans   ?: 0}", "Reapply\nLoans"),
                        MetricItem("${dashboard?.approvedLoans  ?: 0}", "Approved\nLoans"),
                    ))

                    Spacer(Modifier.height(16.dp))
                }
            }

            PullRefreshIndicator(
                refreshing   = isRefreshing,
                state        = pullRefreshState,
                modifier     = Modifier.align(Alignment.TopCenter),
                contentColor = AppColors.Teal,
            )
        }
    }
}

// MetricItem, MetricGrid, MetricCard are in ui/components/UiComponents.kt
