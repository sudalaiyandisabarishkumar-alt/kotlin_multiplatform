import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule  // ✅ changed import
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.plcoding.cmp_koin_di.MainActivity
import dependencies.DbClient
import dependencies.MyRepository
import dependencies.MyRepositoryImpl
import dependencies.MyViewModel
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import networking.InsultCensorClient
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class AppUITest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()  // ✅ already renders App

    private val testModule = module {
        single { DbClient(ApplicationProvider.getApplicationContext<Context>()) }
        single<MyRepository> { MyRepositoryImpl(get()) }
        viewModel { MyViewModel(get()) }
    }

    @Before
    fun setup() {
        loadKoinModules(testModule)
    }

    @After
    fun teardown() {
        unloadKoinModules(testModule)
    }

    @Test
    fun android_text_isDisplayed() {
        // ✅ No setContent needed — MainActivity already shows App
        composeRule
            .onNodeWithText("Android", substring = true)
            .assertIsDisplayed()
    }
}