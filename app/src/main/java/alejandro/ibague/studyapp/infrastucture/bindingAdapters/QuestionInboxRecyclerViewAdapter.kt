package alejandro.ibague.studyapp.infrastucture.bindingAdapters

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.InteractionListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_question.view.*

class QuestionInboxRecyclerViewAdapter(private val mValues: MutableList<QuestionEntity>,
                                       private val mListener: InteractionListener<QuestionEntity>?,
                                       private val context: Context):
    RecyclerView.Adapter<QuestionInboxRecyclerViewAdapter.ViewHolder>()  {

    private val mOnClickListener: View.OnClickListener
    override fun getItemCount(): Int = mValues.size

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as QuestionEntity
            mListener?.onListenerFragmentInteraction(item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: QuestionEntity = mValues[position]

        holder.description.text = item.interrogativeSentence
        holder.orderNumberTextView.text = "${position + 1}. "
        holder.questionTypeTextView.text = item.type

       /* //TODO: This no is the correct from please fixed.
        holder.description.setOnClickListener {
            val answerDialog = AnswerDialog()
            answerDialog.show(parentFragment.fragmentManager!!, "LoginFragment")
        }*/

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val description: TextView = mView.descriptionTextView
        val orderNumberTextView: TextView = mView.orderNumberTextView
        val questionTypeTextView: TextView = mView.typeQuestionTextView
    }

    fun removeAt(position: Int) {
        mValues.removeAt(position)
    }
}
