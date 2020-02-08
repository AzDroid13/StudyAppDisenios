package alejandro.ibague.studyapp.scenes.answers

import alejandro.ibague.studyapp.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class AnswerDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_answer_question, container, false)
        this.dialog!!.setCanceledOnTouchOutside(false)
        this.isCancelable = false

        return view
    }
}