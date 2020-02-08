package alejandro.ibague.studyapp.infrastucture.bindingAdapters

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.models.Subject
import alejandro.ibague.studyapp.infrastucture.commons.InteractionListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import kotlinx.android.synthetic.main.item_general_package.view.*

class BestRecyclerViewAdapter(private val mValues: List<*>,
                              private val mListener: InteractionListener<Subject>?,
                              private val context: Context):
    RecyclerView.Adapter<BestRecyclerViewAdapter.ViewHolder>()  {

    private val mOnClickListener: View.OnClickListener
    override fun getItemCount(): Int = mValues.size

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Subject
            mListener?.onListenerFragmentInteraction(item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        lateinit var item: Any
        item = mValues[position] as PackageEntity
        Glide.with(context).load(item.thumbnailImage).centerCrop().transition(withCrossFade())
            .into(holder.thumbnailImageView)
        holder.titleTextView.text = item.name

        /*with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_general_package, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(mView: View): RecyclerView.ViewHolder(mView) {
        val thumbnailImageView: ImageView = mView.thumbnailImageView
        val titleTextView: TextView = mView.titleTextView
    }
}