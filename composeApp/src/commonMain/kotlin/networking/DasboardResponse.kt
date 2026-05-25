import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    @SerialName("dashboard") val dashboard: Dashboard
)

@Serializable
data class Dashboard(
    @SerialName("achieved_target")  val achievedTarget:  Long,
    @SerialName("total_target")     val totalTarget:     Double,
    @SerialName("total_customers")  val totalCustomers:  Int,
    @SerialName("all_loans")        val allLoans:        Int,
    @SerialName("draft_loans")      val draftLoans:      Int,
    @SerialName("applied_loans")    val appliedLoans:    Int,
    @SerialName("approved_loans")   val approvedLoans:   Int,
    @SerialName("reapply_loans")    val reapplyLoans:    Int,
    @SerialName("repayments")       val repayments:      Repayments
)

@Serializable
data class Repayments(
    @SerialName("collected_due_amount") val collectedDueAmount: Double,
    @SerialName("total_due_amount")     val totalDueAmount:     Double,
    @SerialName("no_of_dues")           val noOfDues:           Int,
    @SerialName("no_of_loans")          val noOfLoans:          Int,
    @SerialName("no_of_overdues")       val noOfOverdues:       Int
)