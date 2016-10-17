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

package de.julianostarek.android.radiostationsdemo


import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import de.julianostarek.android.radiostations.BasicStation
import de.julianostarek.android.radiostations.ExtendedStation
import de.julianostarek.android.radiostations.RadioAPI
import de.julianostarek.android.radiostations.streamUrl
import de.julianostarek.generictasks.GenericTask
import de.julianostarek.generictasks.TaskCallback

class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener, View.OnClickListener {

    interface StationCallback {

        fun onItemClicked(station: BasicStation)

    }

    override fun onClick(p0: View?) {
        if (editText.text.toString().length > 1) {
            GenericTask(object : TaskCallback<List<BasicStation>?> {
                override fun onTaskStart() {
                    progressDialog!!.show()
                }

                override fun onTaskResult(result: List<BasicStation>?, failed: Boolean) {
                    progressDialog!!.dismiss()
                    if (!failed && result != null) {
                        resultsDialog = MaterialDialog.Builder(this@MainActivity)
                                .title("These are the results")
                                .adapter(ResultsAdapter(this@MainActivity, result, radioApi, object : StationCallback {
                                    override fun onItemClicked(station: BasicStation) {
                                        resultsDialog?.dismiss()
                                        GenericTask(object : TaskCallback<Pair<ExtendedStation?, ByteArray>?> {
                                            override fun onTaskResult(result: Pair<ExtendedStation?, ByteArray>?, failed: Boolean) {
                                                if (!failed && result != null) {
                                                    MaterialStyledDialog(this@MainActivity)
                                                            .setTitle(result.first?.name.orEmpty())
                                                            .setDescription("Genre: ${station.genre}\nCountry: ${result.first?.country}\nBitrate: ${result.first?.bitrate}\nEncoding: ${result.first?.encoding}\nWebsite: ${result.first?.websiteUrl}\n")
                                                            .setIcon(BitmapDrawable(resources, BitmapFactory.decodeByteArray(result.second, 0, result.second.size)))
                                                            .show()
                                                }
                                            }
                                        }).execute { radioApi.getStationImage(station, false) }
                                    }
                                }), LinearLayoutManager(this@MainActivity))
                                .show()
                    } else Snackbar.make(window.decorView, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, { radioApi.searchStation(editText.text.toString()) })
        } else Snackbar.make(window.decorView, "Please enter at least 2 characters", Snackbar.LENGTH_SHORT).show()
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p1 == EditorInfo.IME_ACTION_SEARCH) {
            button.performClick()
            return true
        } else return false
    }

    private var resultsDialog: MaterialDialog? = null
    private var progressDialog: MaterialDialog? = null
    private val radioApi = RadioAPI("4824899887")
    private val editText: EditText by bindView<EditText>(R.id.activity_main_edittext)
    private val button: Button by bindView<Button>(R.id.activity_main_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText.setOnEditorActionListener(this)
        button.setOnClickListener(this)

        progressDialog = MaterialDialog.Builder(this)
                .title("Loading")
                .content("Please wait")
                .progress(true, 0)
                .cancelable(false)
                .build()
    }

    class ResultsAdapter(val context: Context, private val results: List<BasicStation>, val api: RadioAPI, val clickListener: StationCallback) : RecyclerView.Adapter<ResultViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResultViewHolder {
            return ResultViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_result, parent, false))
        }

        override fun getItemCount(): Int {
            return results.size
        }

        override fun onBindViewHolder(holder: ResultViewHolder?, position: Int) {
            val item = results[position]
            holder?.title?.text = item.name
            holder?.subtitle?.text = item.genre
            GenericTask(object : TaskCallback<Pair<ExtendedStation?, ByteArray>?> {
                override fun onTaskResult(result: Pair<ExtendedStation?, ByteArray>?, failed: Boolean) {
                    if (!failed)
                        Glide.with(context).load(result?.second).crossFade().into(holder?.image)
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, { api.getStationImage(item, false) })
            holder?.itemView?.setOnClickListener { clickListener.onItemClicked(item) }
        }
    }


    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView by bindView<TextView>(R.id.item_title)
        val subtitle: TextView by bindView<TextView>(R.id.item_subtitle)
        val image: ImageView by bindView<ImageView>(R.id.item_image)
    }

}
