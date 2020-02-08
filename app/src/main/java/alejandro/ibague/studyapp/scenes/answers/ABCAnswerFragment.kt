package alejandro.ibague.studyapp.scenes.answers

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.Constants.Companion.DelayTimeForTransition
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_abc_answer.*
import kotlinx.android.synthetic.main.fragment_abc_question.*

class ABCAnswerFragment(private val parent: QuizContainerFragment,
                        private var questions: List<QuestionEntity>,
                        private val position: Int,
                        private val progressBar: ProgressBar): Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_abc_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sentenceTextView.text = questions[position].interrogativeSentence

        val param = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        param.setMargins(0,32, 0, 32)

        if (!questions[position].answers.isNullOrEmpty()) {
            var positionAnswer = 0

            for (answer in questions[position].answers!!) {
                val button = Button(this.context)
                button.layoutParams = param

                button.setBackgroundColor(resources.getColor(R.color.khaky_light))
                button.setTextColor(resources.getColor(R.color.darkGray))
                button.tag = positionAnswer
                button.textSize = 14f
                button.text = answer.removePrefix("*")
                button.setOnClickListener(this)

                linearLayout.addView(button)
                positionAnswer += 1
            }
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
        val answerSelected: String = questions[position].answers!![p0!!.tag as Int]

        if (answerSelected[0] == '*') {
            if (position == 0 ) {
                progressBar.progress = progressBar.max
            } else {
                progressBar.progress = progressBar.progress + position + 1
            }
            QuizContainerFragment.cantTrueAnswers++
            parent.navigateToNextPosition(questions, position + 1)
        } else {
            p0.background = ColorDrawable(resources.getColor(R.color.red))
            (p0 as Button).setTextColor(resources.getColor(R.color.white))
            if (position == 0 ){
                progressBar.progress = 0
            } else {
                progressBar.progress = (progressBar.max - position)/ position
            }

            progressBar.secondaryProgress = progressBar.max // + progressBar.secondaryProgress + 1
            Handler().postDelayed( {
                parent.navigateToNextPosition(questions, position + 1)
            }, DelayTimeForTransition)
        }
    }
}