import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Teal      = Color(0xFF0D4F4F)
private val TealCard  = Color(0xFF0F3D3D)   // slightly darker card bg matching screenshot
private val White     = Color.White
private val Gray      = Color(0xFF6B7280)
private val LightBg   = Color(0xFFF5F7FA)
private val WhiteA60  = Color(0x99FFFFFF)   // label text on dark cards

private fun formatAmount(amount: Long): String {
    val str = amount.toString()
    val result = StringBuilder()
    str.reversed().forEachIndexed { index, char ->
        if (index > 0 && index % 3 == 0) result.append(',')
        result.append(char)
    }
    return result.reverse().toString()
}

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
                backgroundColor = Teal,
                elevation       = 0.dp,
                title           = {
                    Text(
                        text       = "Dashboard",
                        color      = White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 18.sp,
                    )
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text(
                            text       = "Logout",
                            color      = White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp,
                        )
                    }
                }
            )
        },
        backgroundColor = Teal,   // full teal background like screenshot
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(pullRefreshState),
        ) {
            if (dashboardResponse == null && !isRefreshing) {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = White)
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

                    // ── Welcome section ───────────────────────────────────
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, bottom = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Text(
                            text     = "Welcome Back,",
                            fontSize = 16.sp,
                            color    = WhiteA60,
                        )
                        Text(
                            text       = "$firstName $lastName",
                            fontSize   = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color      = White,
                        )
                    }

                    // ── Overview grid (3 columns) ─────────────────────────

                    MetricGrid(
                        items = listOf(
                            MetricItem("${dashboard?.totalCustomers ?: 0}", "Customers"),
                            MetricItem("${dashboard?.allLoans       ?: 0}", "All\nLoans"),
                            MetricItem("${dashboard?.draftLoans     ?: 0}", "Draft\nLoans"),
                            MetricItem("${dashboard?.appliedLoans   ?: 0}", "Applied\nLoans"),
                            MetricItem("${dashboard?.reapplyLoans   ?: 0}", "Reapply\nLoans"),
                            MetricItem("${dashboard?.approvedLoans  ?: 0}", "Approved\nLoans"),
                        )
                    )

//                    // ── Targets ───────────────────────────────────────────
//                    SectionLabel("Targets")
//                    MetricGrid(
//                        items = listOf(
//                            MetricItem("₹${formatAmount(dashboard?.achievedTarget ?: 0L)}", "Achieved\nTarget"),
//                            MetricItem("${dashboard?.totalTarget ?: 0}%",                   "Total\nTarget"),
//                        )
//                    )
//
//                    // ── Repayments ────────────────────────────────────────
//                    SectionLabel("Repayments")
//                    MetricGrid(
//                        items = listOf(
//                            MetricItem("₹${formatAmount(dashboard?.repayments?.collectedDueAmount?.toLong() ?: 0L)}", "Collected\nAmount"),
//                            MetricItem("₹${formatAmount(dashboard?.repayments?.totalDueAmount?.toLong()    ?: 0L)}", "Total Due\nAmount"),
//                            MetricItem("${dashboard?.repayments?.noOfDues     ?: 0}", "No. of\nDues"),
//                            MetricItem("${dashboard?.repayments?.noOfLoans    ?: 0}", "No. of\nLoans"),
//                            MetricItem("${dashboard?.repayments?.noOfOverdues ?: 0}", "No. of\nOverdues"),
//                        )
//                    )

                    Spacer(Modifier.height(16.dp))
                }
            }

            PullRefreshIndicator(
                refreshing   = isRefreshing,
                state        = pullRefreshState,
                modifier     = Modifier.align(Alignment.TopCenter),
                contentColor = Teal,
            )
        }
    }
}

// ─── Data class for a single metric tile ─────────────────────────────────────
private data class MetricItem(val value: String, val label: String)

// ─── 3-column grid of metric cards ───────────────────────────────────────────
@Composable
private fun MetricGrid(items: List<MetricItem>) {
    val rows = items.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        rows.forEach { row ->
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { item ->
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        value    = item.value,
                        label    = item.label,
                    )
                }
                // fill remaining slots in last row if < 3 items
                repeat(3 - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

// ─── Single metric card (dark teal, value on top, label below) ───────────────
@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    value:    String,
    label:    String,
) {
    Box(
        modifier = modifier
            .aspectRatio(0.95f)                         // roughly square
            .clip(RoundedCornerShape(18.dp))
            .background(TealCard),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier            = Modifier.padding(8.dp),
        ) {
            Text(
                text       = value,
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold,
                color      = White,
                textAlign  = TextAlign.Center,
                maxLines   = 1,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text      = label,
                fontSize  = 13.sp,
                color     = WhiteA60,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ─── Section label ────────────────────────────────────────────────────────────
@Composable
private fun SectionLabel(title: String) {
    Text(
        text       = title,
        fontSize   = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color      = WhiteA60,
        modifier   = Modifier.padding(start = 4.dp),
    )
}