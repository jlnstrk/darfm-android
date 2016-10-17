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

import org.jsoup.nodes.Document
import java.net.URLEncoder

internal class StationDetailsAPIRequest (private val id: Long = 0, private val name: String = "", private val partnerToken: String, private val byName: Boolean = false) : APIRequest<ExtendedStation>() {

    override val url: String
        get() {
            var base = RadioAPIConstants.DAR_FM_SPECIFIC + "?"
            if (byName)
                base += RadioAPIConstants.DAR_FM_PARAM_CALLSIGN + URLEncoder.encode(name, "UTF-8")
            else
                base += RadioAPIConstants.DAR_FM_PARAM_ID + id
            base += "&" + RadioAPIConstants.DAR_FM_PARAM_TOKEN + partnerToken

            return base
        }

    override fun parseResult(doc: Document): ExtendedStation? {
        val stations = doc.getElementsByTag("stations").first()
        val station = stations.getElementsByTag("station").first()

        val id = station.getElementsByTag("station_id").first().text().toLong()
        val name = station.getElementsByTag("callsign").first().text()
        val genre = station.getElementsByTag("genre").first().text()
        val websiteUrl = station.getElementsByTag("websiteurl").first().text()
        val imageUrl = station.getElementsByTag("imageurl").first().text()
        val bitrate = station.getElementsByTag("bitrate").first().text()
        val encoding = station.getElementsByTag("encoding").first().text()
        val description = station.getElementsByTag("description").first().text()
        val country = station.getElementsByTag("country").first().text()

        return ExtendedStation(name, genre, id, websiteUrl, imageUrl, encoding, bitrate, description, country)
    }
}