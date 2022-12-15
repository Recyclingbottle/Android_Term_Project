package kr.ac.kumoh.s20180468.termprojectforandroid

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20180468.termprojectforandroid.databinding.ActivityMainBinding
import kr.ac.kumoh.s20180468.termprojectforandroid.databinding.ActivitySubjectBinding

class SubjectActivity : AppCompatActivity() {
    companion object {
        const val KEY_IMAGE = "SubImage"
        const val KEY_CODE = "SubCode"
        const val KEY_NAME = "SubName"
        const val KEY_GENDER = "SubGender"
        const val KEY_NATION = "SubNation"
        const val KEY_WEAPON = "SubWeapon"
    }

    private lateinit var binding: ActivitySubjectBinding
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoader = ImageLoader(Volley.newRequestQueue(this),
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String?): Bitmap? {
                    return cache.get(url)
                }

                override fun putBitmap(url: String?, bitmap: Bitmap?) {
                    cache.put(url, bitmap)
                }
            })

        binding.imageSub.setImageUrl(intent.getStringExtra(KEY_IMAGE), imageLoader)
        binding.textSubcode.text = intent.getStringExtra(KEY_CODE)
        binding.textSubname.text = intent.getStringExtra(KEY_NAME)
        binding.textGender.text = intent.getStringExtra(KEY_GENDER)
        binding.textSubnation.text = intent.getStringExtra(KEY_NATION)
        binding.textWeapon.text = intent.getStringExtra(KEY_WEAPON)
    }
}