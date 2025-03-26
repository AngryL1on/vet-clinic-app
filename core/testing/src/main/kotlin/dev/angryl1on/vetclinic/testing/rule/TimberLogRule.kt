package dev.angryl1on.vetclinic.testing.rule

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import timber.log.Timber

/**
 * Пользовательский [TestRule] для перенаправления логов Timber на стандартный выход во время тестов.
 *
 * Это правило инициализирует [Timber.DebugTree] с помощью пользовательского метода log, который отображает
 * логи в консоли, обеспечивая лучшую видимость журналов во время тестирования.
 */
class TimberLogRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println(">>> Timber: $tag: $message")
            }
        })
        return base
    }
}
