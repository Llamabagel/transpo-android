/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.data.api.TripsService
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.data.db.TransitDatabase
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TripsRepositoryTest {

    private val transitDatabase: TransitDatabase = getDatabase()
    private val apiService: TripsService = createTestTripsService(createMockServer())

    private val repository = TripsRepository(transitDatabase, apiService, mock())

    @Test
    fun `get stop returns success`() = runBlockingTest {
        val result = repository.getStop(TestStops.walkleyJasper.id)

        assertEquals(Result.Success(TestStops.walkleyJasper), result)
    }

    @Test
    fun `get stop returns error`() = runBlockingTest {
        val result = repository.getStop(StopId("NOT A REAL STOP"))

        assertTrue(result is Result.Error)
    }
}