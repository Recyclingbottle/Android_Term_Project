package kr.ac.kumoh.s20180468.termprojectforandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180468.termprojectforandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var model:SubjectViewModel
    private val subjectAdapter = SubjectAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[SubjectViewModel:: class.java]


        binding.sublist.apply{
            layoutManager = LinearLayoutManager(application)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = subjectAdapter
        }

        model.list.observe(this)
        {
            subjectAdapter.notifyItemRangeInserted(0, subjectAdapter.itemCount)
        }
        model.requestSubject()
    }
    inner class SubjectAdapter: RecyclerView.Adapter<SubjectAdapter.ViewHolder>(){
        //inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener{
            val subnameText = itemView.findViewById<TextView>(R.id.mtxt_name)
            val subcodeText = itemView.findViewById<TextView>(R.id.mtxt_code)
            val subnationText = itemView.findViewById<TextView>(R.id.mtxt_nation)
            val subgengerText = itemView.findViewById<TextView>(R.id.mtxt_gender)

            val subImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init {
                subImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                //Toast.makeText(application, model.list.value?.get(adapterPosition)?.subname, Toast.LENGTH_SHORT).show()
                val intent = Intent(application, SubjectActivity::class.java)
                intent.putExtra(SubjectActivity.KEY_CODE, model.list.value?.get(adapterPosition)?.subcode)
                intent.putExtra(SubjectActivity.KEY_NAME, model.list.value?.get(adapterPosition)?.subname)
                intent.putExtra(SubjectActivity.KEY_IMAGE, model.getImageUrl(adapterPosition))
                intent.putExtra(SubjectActivity.KEY_NATION, model.list.value?.get(adapterPosition)?.nationality)
                intent.putExtra(SubjectActivity.KEY_GENDER, model.list.value?.get(adapterPosition)?.gender)
                intent.putExtra(SubjectActivity.KEY_WEAPON, model.list.value?.get(adapterPosition)?.weapon)
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.items_subject, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.subnameText.text = model.list.value?.get(position)?.subname ?:null
            holder.subcodeText.text = model.list.value?.get(position)?.subcode ?:null
            holder.subgengerText.text = model.list.value?.get(position)?.gender ?:null
            holder.subnationText.text = model.list.value?.get(position)?.nationality ?:null

            holder.subImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
        }

        override fun getItemCount() = model.list.value?.size ?:0
    }
}