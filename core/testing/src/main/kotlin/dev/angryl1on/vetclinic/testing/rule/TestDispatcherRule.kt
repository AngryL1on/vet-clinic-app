package dev.angryl1on.vetclinic.testing.rule

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Пользовательский [TestWatcher] для настройки и сброса основного диспетчера с помощью [TestDispatcher]
 * во время тестов, используя [ExperimentalCoroutinesApi].
 *
 * @property [TestDispatcher] для использования в тестах, по умолчанию [UnconfinedTestDispatcher].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

/**
 * Создает новый [CoroutineScope] с правилом TestDispatcher
 */
fun TestDispatcherRule.CoroutineScope(): CoroutineScope = TestScope(testDispatcher)
