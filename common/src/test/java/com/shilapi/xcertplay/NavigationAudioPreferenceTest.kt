package com.shilapi.xcertplay

import com.shilapi.xcertplay.media.NavigationAudioRouting
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], manifest = Config.NONE)
class NavigationAudioPreferenceTest {
    private val context get() = RuntimeEnvironment.getApplication()

    @Test fun systemRoutingIsTheSafeDefault() {
        assertNull(AirPlayPersistence.loadNavigationStreamType(context))
    }

    @Test fun bydStreamTypeCanBeSavedAndRestored() {
        AirPlayPersistence.saveNavigationStreamType(
            context,
            NavigationAudioRouting.BYD_DILINK_STREAM_TYPE,
        )

        assertEquals(
            NavigationAudioRouting.BYD_DILINK_STREAM_TYPE,
            AirPlayPersistence.loadNavigationStreamType(context),
        )

        AirPlayPersistence.saveNavigationStreamType(context, null)
        assertNull(AirPlayPersistence.loadNavigationStreamType(context))
    }

    @Test fun unsupportedStreamTypeFallsBackToSystemRouting() {
        AirPlayPersistence.saveNavigationStreamType(
            context,
            NavigationAudioRouting.MAX_STREAM_TYPE + 1,
        )

        assertNull(AirPlayPersistence.loadNavigationStreamType(context))
    }
}
