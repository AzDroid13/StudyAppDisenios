package alejandro.ibague.studyapp.scenes.create_new.questions

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.QuestionType
import alejandro.ibague.studyapp.models.QuestionEntityList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.facebook.FacebookSdk.getApplicationContext
import kotlinx.android.synthetic.main.fragment_question_container.*
import kotlinx.android.synthetic.main.fragment_true_false_question.*
import kotlinx.android.synthetic.main.fragment_true_false_question.backButton
import kotlinx.android.synthetic.main.fragment_true_false_question.nextButton
import java.util.*

class TrueFalseQuestionFragment(private val packageId: String,
                                private val parentFragmentLayout: FrameLayout,
                                private val questionSpinner: Spinner,
                                private var questionList: QuestionEntityList,
                                private val parentView: QuestionContainerFragment,
                                private var objectToPresent: QuestionEntity?
) : Fragment() {
    private val viewModel: QuestionContainerViewModel by activityViewModels()
    private var answerIs: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_true_false_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (null != objectToPresent) {
            loadMySelf(objectToPresent!!)
        }

        nextButton.setOnClickListener {
            val question = QuestionEntity(type= QuestionType.TRUE_FALSE.name,
                interrogativeSentence = questionSentenceEditText.text.toString(),
                answers = Collections.singletonList(answerIs.toString()).toList(), packageId = packageId,
                subjectionSentence = sujectionSentenceEditText.text.toString())
            viewModel.insert(question)
            questionList.add(question)
            Toast.makeText(this@TrueFalseQuestionFragment.context, "Added", Toast.LENGTH_SHORT).show()
           // frameLayout.visibility = View.GONE

            clearAll(view, savedInstanceState)
        }

        falseButton.setOnClickListener { it as Button
            it.background = ContextCompat.getDrawable(getApplicationContext(),R.drawable.rounded_red_color)
            it.setTextColor(resources.getColor( R.color.white))
            trueButton.background = ContextCompat.getDrawable(getApplicationContext(),R.drawable.rounded_khaky)
            trueButton.setTextColor(resources.getColor(R.color.darkGray))
            answerIs = false
        }

        trueButton.setOnClickListener { it as Button
            it.background = ContextCompat.getDrawable(getApplicationContext(),R.drawable.rounded_green)
            it.setTextColor(resources.getColor( R.color.white))
            falseButton.background = ContextCompat.getDrawable(getApplicationContext(),R.drawable.rounded_khaky)
            falseButton.setTextColor(resources.getColor(R.color.darkGray))
            answerIs = true
        }

        if (parentView.questionTopCounter.text.toString().contains("1")) {
            backButton.visibility = View.GONE
        } else {
            backButton.visibility = View.VISIBLE
        }
        backButton.setOnClickListener {

            val question = if (null == objectToPresent) questionList[questionList.count() - 1] else questionList[questionList.indexOf(objectToPresent!!) -1]
            when (question.type) {
                QuestionType.TRUE_FALSE.name -> {
                    loadMySelf(question)
                }
                QuestionType.MULTIPLE.name -> {
                    parentView.fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    parentView.fragmentTransaction.replace(R.id.fragmentLayout, ABCQuestionFragment(packageId,
                        parentFragmentLayout, questionSpinner, questionList, parentView, objectToPresent = question)).commit()
                    parentFragmentLayout.visibility = View.VISIBLE

                }
                QuestionType.EXACT.name -> {
                    parentView.fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    parentView.fragmentTransaction.replace(R.id.fragmentLayout, ExactQuestionFragment(packageId,
                        parentFragmentLayout, questionSpinner, questionList, parentView, objectToPresent = question)).commit()
                    parentFragmentLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadMySelf(question: QuestionEntity) {
        objectToPresent = question
        parentView.questionTopCounter.text = "${resources.getText(R.string.question)} ${questionList.indexOf(objectToPresent!!) + 1}"


        questionSentenceEditText.setText(question.interrogativeSentence)
        sujectionSentenceEditText.setText(question.subjectionSentence)

        if (question.answers!![0].toBoolean()) {
            trueButton.background =
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_green)
            trueButton.setTextColor(resources.getColor(R.color.white))
        } else {
            falseButton.background =
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_red_color)
            falseButton.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun clearAll(view: View, savedInstanceState: Bundle?) {
        trueButton.background = ContextCompat.getDrawable(getApplicationContext(),R.drawable.rounded_khaky)
        falseButton.background = ContextCompat.getDrawable(getApplicationContext(),R.drawable.rounded_khaky)
        questionSentenceEditText.text.clear()
        sujectionSentenceEditText.text.clear()

        val numberOfQuestion = if (questionList.count() > 0) questionList.count() + 1  else  1
        parentView.questionTopCounter.text = "${resources.getText(R.string.question)} $numberOfQuestion"

        this.onViewCreated(view, savedInstanceState)
    }
}
