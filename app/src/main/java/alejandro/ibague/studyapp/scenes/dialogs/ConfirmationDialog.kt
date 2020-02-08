package alejandro.ibague.studyapp.scenes.dialogs

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.QuestionInboxRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_confirmation.view.*

class ConfirmationDialog(private val adapter: QuestionInboxRecyclerViewAdapter): DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_confirmation, container, false)
        this.dialog!!.setCanceledOnTouchOutside(false)
        this.isCancelable = false

        view.okTextView.setOnClickListener {

            Toast.makeText(context!!, "Item Removed.", Toast.LENGTH_SHORT).show()
            adapter.notifyDataSetChanged()
            this.dismiss()
        }

        view.cancelTextView.setOnClickListener {
            adapter.notifyDataSetChanged()
            this.dismiss()
        }

        return view
    }
}