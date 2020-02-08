package alejandro.ibague.studyapp.scenes.dialogs

import alejandro.ibague.studyapp.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_information_alert.*
import kotlinx.android.synthetic.main.dialog_information_alert.view.*

class InformationAlertDialog(private val message: String): DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_information_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.dialog!!.setCanceledOnTouchOutside(false)
        this.isCancelable = false

        descriptionTextView.text = message
        view.okTextView.setOnClickListener {
            this.dismiss()
        }
    }
}