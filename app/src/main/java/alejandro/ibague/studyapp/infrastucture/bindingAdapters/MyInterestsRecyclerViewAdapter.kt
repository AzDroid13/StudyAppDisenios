package alejandro.ibague.studyapp.infrastucture.bindingAdapters

import alejandro.ibague.studyapp.R

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_my_interest.view.*


class MyInterestsRecyclerViewAdapter(private val mValues: List<String>,

                                     private val context: Context) : RecyclerView.Adapter<MyInterestsRecyclerViewAdapter.ViewHolder>(){

    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //To change body of created functions use File | Settings | File Templates.
        lateinit var item: Any

        when {
            mValues[position] is String -> {
                item = mValues[position] as String
                holder?.nameTextView?.text = item
            }
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }


    }

    override fun getItemCount(): Int = mValues.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_my_interest, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val nameTextView: TextView = mView.name
    }


}