/*
 * Copyright (c) 2017 Evident Solutions Oy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.dalesbred

import org.dalesbred.testutils.LoggingController
import org.dalesbred.testutils.SuppressLogging
import org.dalesbred.transaction.Isolation.SERIALIZABLE
import org.dalesbred.transaction.TransactionSerializationException
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

class DatabaseTransactionIsolationTest {

    private val db1 = TestDatabaseProvider.createInMemoryHSQLDatabase()
    private val db2 = TestDatabaseProvider.createInMemoryHSQLDatabase()

    @get:Rule val rule = LoggingController()

    @Test
    @SuppressLogging
    fun concurrentUpdatesInSerializableTransactionThrowTransactionSerializationException() {
        assertFailsWith<TransactionSerializationException> {
            db1.update("DROP TABLE IF EXISTS isolation_test")
            db1.update("CREATE TABLE isolation_test (value INT)")
            db1.update("INSERT INTO isolation_test (value) VALUES (1)")

            db1.withVoidTransaction(SERIALIZABLE) {
                db1.findUniqueInt("SELECT value FROM isolation_test")

                db2.withVoidTransaction(SERIALIZABLE) {
                    db2.findUniqueInt("SELECT value FROM isolation_test")
                    db2.update("UPDATE isolation_test SET value=2")
                }

                db1.update("UPDATE isolation_test SET value=3")
            }
        }
    }
}
