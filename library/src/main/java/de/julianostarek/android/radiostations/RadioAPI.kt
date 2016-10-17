/*
 * Copyright 2016 Julian Ostarek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.julianostarek.android.radiostations

import java.net.URL

class RadioAPI @JvmOverloads constructor(val partnerToken: String, timeOutMillis: Int = 20000) : APIRequestHandler(timeOutMillis) {

    fun searchStation(name: String): List<BasicStation>? {
        return query(StationSearchAPIRequest(name))
    }

    fun getStationDetails(station: BasicStation, byName: Boolean): ExtendedStation? {
        return getStationDetails(station.id, station.name, byName)
    }

    fun getStationDetails(stationId: Long): ExtendedStation? {
        return getStationDetails(id = stationId)
    }

    fun getStationDetails(stationName: String): ExtendedStation? {
        return getStationDetails(stationName = stationName, byName = true)
    }

    private fun getStationDetails(id: Long = 0, stationName: String = "", byName: Boolean = false): ExtendedStation? {
        try {
            return query(StationDetailsAPIRequest(id, stationName, partnerToken, byName))
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getStationImage(station: BasicStation, byName: Boolean): Pair<ExtendedStation?, ByteArray>? {
        val extendedStation = getStationDetails(station, byName)
        try {
            return Pair(extendedStation, URL(extendedStation?.imageUrl).readBytes())
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getStationImage(stationId: Long): Pair<ExtendedStation?, ByteArray>? {
        val extendedStation = getStationDetails(stationId)
        try {
            return Pair(extendedStation, URL(extendedStation?.imageUrl).readBytes())
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getStationImage(stationName: String): Pair<ExtendedStation?, ByteArray>? {
        val extendedStation = getStationDetails(stationName)
        try {
            return Pair(extendedStation, URL(extendedStation?.imageUrl).readBytes())
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getStationsMatchingArtist(songArtist: String, forceMatch: Boolean = false): List<BasicStation>? {
        return getStationsMatchingMetadata(songArtist = songArtist, forceMatch = forceMatch)
    }

    fun getStationsMatchingSong(songTitle: String, forceMatch: Boolean = false): List<BasicStation>? {
        return getStationsMatchingMetadata(songTitle, forceMatch = forceMatch)
    }

    fun getStationsMatchingMetadata(songTitle: String = "", songArtist: String = "", forceMatch: Boolean): List<BasicStation>? {
        if (songTitle.isEmpty() && songArtist.isEmpty()) return null
        try {
            return query(StationsMatchingMetadataAPIRequest(songTitle, songArtist, forceMatch))
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}