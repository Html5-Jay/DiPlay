package com.shilapi.xcertplay.media

/** Optional vendor stream routing for CarPlay navigation guidance. */
object NavigationAudioRouting {
    const val SYSTEM_DEFAULT = -1
    const val BYD_DILINK_STREAM_TYPE = 14
    const val MIN_STREAM_TYPE = 0
    const val MAX_STREAM_TYPE = 20

    fun sanitizeStreamType(streamType: Int?): Int? =
        streamType?.takeIf { it in MIN_STREAM_TYPE..MAX_STREAM_TYPE }

    internal fun streamTypeFor(channel: AudioChannel, configuredStreamType: Int?): Int? =
        if (channel == AudioChannel.NAVIGATION) sanitizeStreamType(configuredStreamType) else null
}
