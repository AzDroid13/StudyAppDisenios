package alejandro.ibague.studyapp.scenes.answers

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.Constants.Companion.DelayTimeForTransition
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_exact_answer.*
import kotlinx.android.synthetic.main.fragment_exact_question.*

class ExactAnswerFragment(private val parent: QuizContainerFragment,
                          private var questions: List<QuestionEntity>,
                          private val position: Int,
                          private val progressBar: ProgressBar) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exact_answer, container, false)
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sentenceTextView.text = questions[position].interrogativeSentence

        wrongImageView.setOnClickListener {
            wrongImageView.setImageDrawable(resources.getDrawable(R.drawable.wrong_yellow))
            parent.navigateToNextPosition(questions, position + 1)
        }

        unclearImageView.setOnClickListener {
            unclearImageView.setImageDrawable(resources.getDrawable(R.drawable.unclear_blue))
            parent.navigateToNextPosition(questions, position + 1)
        }

        checkButton.setOnClickListener {
            hideKeyboard(view, context)
            if (exactAnswerEditText.text.toString().toLowerCase() == questions[position].answers!![0].toLowerCase()) {
                if (position == 0 ) {
                    progressBar.progress = progressBar.max
                } else {
                    progressBar.progress = progressBar.progress + position + 1
                }
                //progressBar.progress = progressBar.progress + progressBar.secondaryProgress + 1
                parent.navigateToNextPosition(questions, position + 1)
            } else {
                if (position == 0 ){
                    progressBar.progress = 0
                } else {
                    progressBar.progress = (progressBar.max - position)/ position
                }

                progressBar.secondaryProgress = progressBar.max
               // progressBar.secondaryProgress = progressBar.progress + progressBar.secondaryProgress + 1
                Handler().postDelayed( {
                    parent.navigateToNextPosition(questions, position + 1)
                }, DelayTimeForTransition)
            }
        }
    }
}