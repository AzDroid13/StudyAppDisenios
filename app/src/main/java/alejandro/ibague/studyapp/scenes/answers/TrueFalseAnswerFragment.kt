package alejandro.ibague.studyapp.scenes.answers

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.Constants.Companion.DelayTimeForTransition
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_true_false_answer.*
import kotlinx.android.synthetic.main.fragment_true_false_answer.nextButton
import kotlinx.android.synthetic.main.fragment_true_false_question.*

class TrueFalseAnswerFragment(private val parent: QuizContainerFragment,
                              private var questions: List<QuestionEntity>,
                              private val position: Int,
                              private val progressBar: ProgressBar): Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_true_false_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val question = questions[position]
        sentenceTextView.text = question.interrogativeSentence

        if (questions.isEmpty()) {
            backButton.visibility = View.INVISIBLE
        }

        if (!question.answers.isNullOrEmpty()) {
            nextButton.setOnClickListener(this)
            nextButton2.setOnClickListener(this)
        }

        wrongImageView.setOnClickListener {
            wrongImageView.setImageDrawable(resources.getDrawable(R.drawable.wrong_yellow))
            parent.navigateToNextPosition(questions, position + 1)
        }

        unclearImageView.setOnClickListener {
            unclearImageView.setImageDrawable(resources.getDrawable(R.drawable.unclear_blue))
            parent.navigateToNextPosition(questions, position + 1)
        }
    }

    override fun onClick(p0: View?) {
        val question = questions[position]

        if (p0!!.tag == question.answers!![0]) {
            if (position == 0 ) {
                progressBar.progress = progressBar.max
            } else {
                progressBar.progress = progressBar.progress + position + 1
            }
            QuizContainerFragment.cantTrueAnswers++
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