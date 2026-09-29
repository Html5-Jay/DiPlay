package com.shilapi.xcertplay.media

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AudioChannelMappingTest {
    @Test
    fun mobileCompatibleMappingMatchesTheOriginalRouting() {
        assertMapped(
            mode = AudioChannelMappingMode.MOBILE_COMPATIBLE,
            audioType = "telephony",
            payloadType = 100,
            channel = AudioChannel.PHONE,
            contentType = AudioContentType.SPEECH,
        )
        assertMapped(
            mode = AudioChannelMappingMode.MOBILE_COMPATIBLE,
            audioType = "speechRecognition",
            payloadType = 100,
            channel = AudioChannel.ASSISTANT,
            contentType = AudioContentType.SPEECH,
        )
        assertMapped(
            mode = AudioChannelMappingMode.MOBILE_COMPATIBLE,
            audioType = "media",
            payloadType = 100,
            channel = AudioChannel.MEDIA,
            contentType = AudioContentType.MUSIC,
        )
        listOf("default", "alert", "compatibility").forEach { audioType ->
            assertMapped(
                mode = AudioChannelMappingMode.MOBILE_COMPATIBLE,
                audioType = audioType,
                payloadType = 100,
                channel = AudioChannel.NAVIGATION,
                contentType = AudioContentType.SPEECH,
            )
        }
    }

    @Test
    fun automotiveMappingUsesTheBusSpecificCarPlayTypes() {
        listOf("media", "default", "compatibility").forEach { audioType ->
            assertMapped(
                mode = AudioChannelMappingMode.AUTOMOTIVE_BUS,
                audioType = audioType,
                payloadType = 100,
                channel = AudioChannel.MEDIA,
                contentType = AudioContentType.MUSIC,
            )
        }
        assertMapped(
            mode = AudioChannelMappingMode.AUTOMOTIVE_BUS,
            audioType = "telephony",
            payloadType = 100,
            channel = AudioChannel.PHONE,
            contentType = AudioContentType.SPEECH,
        )
        assertMapped(
            mode = AudioChannelMappingMode.AUTOMOTIVE_BUS,
            audioType = "speechRecognition",
            payloadType = 100,
            channel = AudioChannel.ASSISTANT,
            contentType = AudioContentType.SPEECH,
        )
        assertMapped(
            mode = AudioChannelMappingMode.AUTOMOTIVE_BUS,
            audioType = "alert",
            payloadType = 100,
            channel = AudioChannel.NAVIGATION,
            contentType = AudioContentType.SPEECH,
        )
    }

    @Test
    fun unknownTypesKeepTheMainHighAudioFallback() {
        assertMapped(
            mode = AudioChannelMappingMode.MOBILE_COMPATIBLE,
            audioType = "unknown",
            payloadType = AudioChannelMapper.STREAM_TYPE_MAIN_HIGH_AUDIO,
            channel = AudioChannel.MEDIA,
            contentType = AudioContentType.MUSIC,
        )
        assertMapped(
            mode = AudioChannelMappingMode.AUTOMOTIVE_BUS,
            audioType = "unknown",
            payloadType = 100,
            channel = AudioChannel.NAVIGATION,
            contentType = AudioContentType.SPEECH,
        )
    }

    @Test
    fun customNavigationStreamTypeOnlyAppliesToGuidance() {
        assertEquals(
            NavigationAudioRouting.BYD_DILINK_STREAM_TYPE,
            NavigationAudioRouting.streamTypeFor(
                AudioChannel.NAVIGATION,
                NavigationAudioRouting.BYD_DILINK_STREAM_TYPE,
            ),
        )
        listOf(AudioChannel.MEDIA, AudioChannel.PHONE, AudioChannel.ASSISTANT).forEach { channel ->
            assertNull(
                NavigationAudioRouting.streamTypeFor(
                    channel,
                    NavigationAudioRouting.BYD_DILINK_STREAM_TYPE,
                ),
            )
        }
        assertNull(
            NavigationAudioRouting.streamTypeFor(
                AudioChannel.NAVIGATION,
                NavigationAudioRouting.SYSTEM_DEFAULT,
            ),
        )
        assertNull(
            NavigationAudioRouting.streamTypeFor(
                AudioChannel.NAVIGATION,
                NavigationAudioRouting.MAX_STREAM_TYPE + 1,
            ),
        )
    }

    private fun assertMapped(
        mode: AudioChannelMappingMode,
        audioType: String,
        payloadType: Int,
        channel: AudioChannel,
        contentType: AudioContentType,
    ) {
        assertEquals(
            AudioChannelSelection(channel, contentType),
            AudioChannelMapper.map(audioType, payloadType, mode),
        )
    }
}
