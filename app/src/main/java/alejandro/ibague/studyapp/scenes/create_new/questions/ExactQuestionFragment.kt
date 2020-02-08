package alejandro.ibague.studyapp.scenes.create_new.questions

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.QuestionType
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import alejandro.ibague.studyapp.models.QuestionEntityList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_exact_question.answerEditText
import kotlinx.android.synthetic.main.fragment_exact_question.backButton
import kotlinx.android.synthetic.main.fragment_exact_question.nextButton
import kotlinx.android.synthetic.main.fragment_exact_question.questionSentenceEditText
import kotlinx.android.synthetic.main.fragment_exact_question.sujectionSentenceEditText
import kotlinx.android.synthetic.main.fragment_question_container.*
import java.util.*

class ExactQuestionFragment(private val packageId: String,
                            private val parentFragmentLayout: FrameLayout,
                            private val questionSpinner: Spinner,
                            private var questionList: QuestionEntityList,
                            private val parentView: QuestionContainerFragment,
                            private var objectToPresent: QuestionEntity?
): Fragment() {
    private val viewModel: QuestionContainerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exact_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (null != objectToPresent) {
            loadMySelf(objectToPresent!!)
        }

        nextButton.setOnClickListener {
            val question = QuestionEntity(type= QuestionType.EXACT.name, interrogativeSentence = questionSentenceEditText.text.toString(),
                answers = Collections.singletonList(answerEditText.text.toString()), packageId = packageId,
                subjectionSentence = sujectionSentenceEditText.text.toString())
            viewModel.insert(question)
            questionList.add(question)
            clearAll(view, savedInstanceState)
            Toast.makeText(this@ExactQuestionFragment.context, "Added", Toast.LENGTH_SHORT).show()

            hideKeyboard(view, this@ExactQuestionFragment.context)
        }

        if (parentView.questionTopCounter.text.toString().contains("1")) {
            backButton.visibility = View.GONE
        } else {
            backButton.visibility = View.VISIBLE
        }

        backButton.setOnClickListener {
            val question = if (null == objectToPresent) questionList[questionList.count() - 1] else questionList[questionList.indexOf(objectToPresent!!) -1]
            when (question.type) {
                QuestionType.EXACT.name -> {
                    loadMySelf(question)
                }
                QuestionType.TRUE_FALSE.name -> {
                    parentView.fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    parentView.fragmentTransaction.replace(R.id.fragmentLayout, TrueFalseQuestionFragment(packageId,
                        parentFragmentLayout, questionSpinner, questionList, parentView, objectToPresent = question)).commit()
                    parentFragmentLayout.visibility = View.VISIBLE
                }
                QuestionType.MULTIPLE.name -> {
                    parentView.fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    parentView.fragmentTransaction.replace(R.id.fragmentLayout, ABCQuestionFragment(packageId,
                        parentFragmentLayout, questionSpinner, questionList, parentView, objectToPresent = question)).commit()
                    parentFragmentLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadMySelf(question: QuestionEntity) {
        objectToPresent = question
        parentView.questionTopCounter.text = "${resources.getText(R.string.question)} ${questionList.indexOf(objectToPresent!!) + 1}"

        if (parentView.questionTopCounter.text.toString().contains("1")) {
            backButton.visibility = View.GONE
        } else {
            backButton.visibility = View.VISIBLE
        }

        questionSentenceEditText.setText(question.interrogativeSentence)
        sujectionSentenceEditText.setText(question.subjectionSentence)
        answerEditText.setText(question.answers!![0])
    }

    private fun clearAll(view: View, savedInstanceState: Bundle?) {
        answerEditText.text.clear()
        questionSentenceEditText.text.clear()
        sujectionSentenceEditText.text.clear()

        val numberOfQuestion = if (questionList.count() > 0) questionList.count() + 1  else  1
        parentView.questionTopCounter.text = "${resources.getText(R.string.question)} $numberOfQuestion"

        this.onViewCreated(view, savedInstanceState)
    }
}
