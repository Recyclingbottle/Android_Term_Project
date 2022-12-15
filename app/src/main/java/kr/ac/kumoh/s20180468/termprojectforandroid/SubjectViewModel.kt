package kr.ac.kumoh.s20180468.termprojectforandroid

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class SubjectViewModel(application: Application) : AndroidViewModel(application) {
    data class Subject (var id: Int, var subcode: String, var subname: String, var age: Int,
                        var nationality: String, var gender: String, var weapon: String, var image: String)

    companion object {
        const val QUEUE_TAG = "SubjectVolleyRequest"
        const val SERVER_URL = "https://expressdb-ysayx.run.goorm.io/"
    }

    private val subjects = ArrayList<Subject>()
    private  val _list = MutableLiveData<ArrayList<Subject>>()
    val list: LiveData<ArrayList<Subject>>
        get() = _list
    private var queue: RequestQueue

    val imageLoader: ImageLoader

    init {
        _list.value = subjects
        queue = Volley.newRequestQueue(getApplication())

        imageLoader = ImageLoader(queue,
            object : ImageLoader.ImageCache{
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String?): Bitmap? {
                    return cache.get(url)
                }

                override fun putBitmap(url: String?, bitmap: Bitmap?) {
                    cache.put(url, bitmap)
                }
            }
            )
    }
    fun getImageUrl(i: Int): String = "$SERVER_URL/image/" + URLEncoder.encode(subjects[i].image, "utf-8")
    fun requestSubject(){

        val request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL,
            null,
            {
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                subjects.clear()
                parseJson(it)
                _list.value = subjects
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        queue.add(request)
    }
    private fun parseJson(items:JSONArray) {
        for (i in 0 until items.length()) {
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val subcode = item.getString("subcode")
            val subname = item.getString("subname")
            val age = item.getInt("age")
            val nationality = item.getString("nationality")
            val gender = item.getString("gender")
            val weapon = item.getString("weapon")
            val image = item.getString("image")
            subjects.add(Subject(id, subcode, subname, age, nationality, gender, weapon, image))
        }
    }
    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}