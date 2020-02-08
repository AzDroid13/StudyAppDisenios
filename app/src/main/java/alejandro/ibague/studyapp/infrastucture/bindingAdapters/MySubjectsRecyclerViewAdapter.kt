package alejandro.ibague.studyapp.infrastucture.bindingAdapters

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.infrastucture.commons.InteractionListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import kotlinx.android.synthetic.main.item_my_package.view.*

class MySubjectsRecyclerViewAdapter(private val mValues: List<*>,
                                    private val mListener: InteractionListener<PackageEntity>?,
                                    private val context: Context): RecyclerView.Adapter<MySubjectsRecyclerViewAdapter.ViewHolder>()  {

    private val mOnClickListener: View.OnClickListener
    override fun getItemCount(): Int = mValues.size

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as PackageEntity
            mListener?.onListenerFragmentInteraction(item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        lateinit var item: Any
        when {
            mValues[position] is PackageEntity -> {
                item = mValues[position] as PackageEntity

                holder.titleTextView.text = item.name
                holder.descriptionTextView.text = item.description
                holder.accessModifierSwitch.isChecked = item.isPublicAccess
                holder.titleTextView.setOnClickListener(mListener)
                holder.descriptionTextView.setOnClickListener(mListener)
            }
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_my_subject, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val titleTextView: TextView = mView.titleTextView
        val descriptionTextView: TextView = mView.descriptionTextView
        val accessModifierSwitch: Switch = mView.accessModifierSwitch
    }
}