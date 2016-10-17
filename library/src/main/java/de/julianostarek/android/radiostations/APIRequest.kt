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
import java.util.*

abstract class APIRequest<out Result> {

    abstract val url: String?

    abstract fun parseResult(doc: Document): Result?

    protected fun parsePlaylistResult(doc: Document): List<BasicStation>? {
        val playlist = doc.getElementsByTag("playlist").first()
        val stations = playlist.getElementsByTag("station")

        val items = ArrayList<BasicStation>()

        for (i in 0..stations.size - 1) {
            val station = stations[i]
            val title = station.getElementsByTag("callsign").first().text()
            val genre = station.getElementsByTag("genre").first().text()
            val id = station.getElementsByTag("station_id").first().text().toLong()
            items.add(BasicStation(title, genre, id))
        }
        return items
    }



}