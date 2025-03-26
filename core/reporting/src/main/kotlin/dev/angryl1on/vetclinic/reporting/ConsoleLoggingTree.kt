package dev.angryl1on.vetclinic.reporting

import android.util.Log
import timber.log.Timber

/**
 * Пользовательское дерево логирования, наследующееся от Timber.Tree
 *
 * @constructor Создает экземпляр Timber.Tree.
 */
class ConsoleLoggingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (t == null) {
            when (priority) {
                // Processing logs without exception
                Log.DEBUG -> Log.d(tag, message)
                Log.VERBOSE -> Log.v(tag, message)
                Log.INFO -> Log.i(tag, message)
                Log.WARN -> Log.w(tag, message)
                Log.ERROR -> Log.e(tag, message)
            }
        } else {
            when (priority) {
                // Processing logs with exception
                Log.DEBUG -> Log.d(tag, message, t)
                Log.VERBOSE -> Log.v(tag, message, t)
                Log.INFO -> Log.i(tag, message, t)
                Log.WARN -> Log.w(tag, message, t)
                Log.ERROR -> Log.e(tag, message, t)
            }
        }
    }
}
