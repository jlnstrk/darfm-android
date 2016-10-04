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

internal class StationSearchAPIRequest(stationName: String) : APIRequest<List<BasicStation>>() {

    override val url: String = RadioAPIConstants.DAR_FM_PLAYLIST + RadioAPIConstants.DAR_FM_PARAM_QUERY + "@" + RadioAPIConstants.DAR_FM_PARAM_CALLSIGN + URLEncoder.encode(stationName, "UTF-8")

    override fun parseResult(doc: Document): List<BasicStation>? {
        return parsePlaylistResult(doc)
    }


}