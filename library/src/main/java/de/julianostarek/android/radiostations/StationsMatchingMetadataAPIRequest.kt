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

internal class StationsMatchingMetadataAPIRequest (private val title: String = "",private val artist: String = "", val match: Boolean) : APIRequest<List<BasicStation>>() {

    override val url: String
        get() {
            var base = RadioAPIConstants.DAR_FM_PLAYLIST + RadioAPIConstants.DAR_FM_PARAM_QUERY
            if (match) {
                if (artist.isNotEmpty()) base += "%20" + RadioAPIConstants.DAR_FM_PARAM_FORCE_MATCH_ARTIST + "%20" + URLEncoder.encode(artist, "UTF-8")
                if (title.isNotEmpty()) base += "%20" + RadioAPIConstants.DAR_FM_PARAM_FORCE_MATCH_TITLE + "%20" + URLEncoder.encode(title, "UTF-8")
            } else {
                if (artist.isNotEmpty()) base += URLEncoder.encode(artist, "UTF-8")
                if (title.isNotEmpty()) {
                    if (artist.isNotEmpty()) base += "%20"
                    base += URLEncoder.encode(title, "UTF-8")
                }
            }
            return base
        }

    override fun parseResult(doc: Document): List<BasicStation>? {
        return parsePlaylistResult(doc)
    }
}