import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.plcoding.cmp_koin_di.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppUITest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun login_screen_is_displayed_on_first_launch() {
        composeRule
            .onNodeWithText("Login to VGro", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun employee_id_field_is_visible() {
        composeRule
            .onNodeWithText("Employee ID", substring = true)
            .assertIsDisplayed()
    }
}
