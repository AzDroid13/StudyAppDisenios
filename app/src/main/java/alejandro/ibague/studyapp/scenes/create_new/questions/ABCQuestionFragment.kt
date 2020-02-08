package alejandro.ibague.studyapp.scenes.create_new.questions

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.QuestionType
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import alejandro.ibague.studyapp.models.QuestionEntityList
import alejandro.ibague.studyapp.scenes.dialogs.InformationAlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_abc_question.*
import kotlinx.android.synthetic.main.fragment_abc_question.backButton
import kotlinx.android.synthetic.main.fragment_abc_question.nextButton
import kotlinx.android.synthetic.main.fragment_question_container.*

class ABCQuestionFragment(private val packageId: String,
                          private val parentFragmentLayout: FrameLayout,
                          private val questionSpinner: Spinner,
                          private var questionList: QuestionEntityList,
                          private val parentView: QuestionContainerFragment,
                          private var objectToPresent: QuestionEntity?
): Fragment() {
    private val viewModel: QuestionContainerViewModel by activityViewModels()
    private val listOfAnswers = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_abc_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (null != objectToPresent) {
            loadMySelf(objectToPresent!!)
        }

        nextButton.setOnClickListener {
            groupAnswers()
            val question = QuestionEntity(type= QuestionType.MULTIPLE.name, interrogativeSentence = questionSentenceEditText.text.toString(),
                answers = listOfAnswers, packageId = packageId, subjectionSentence = sujectionSentenceEditText.text.toString())
            viewModel.insert(question)
            questionList.add(question)
            Toast.makeText(this@ABCQuestionFragment.context, "Added", Toast.LENGTH_SHORT).show()
            clearAll(view, savedInstanceState)
            hideKeyboard(view, this@ABCQuestionFragment.context)
        }

        addIncorrectAnserButton.setOnClickListener {
            when {
                linearLayout1.visibility == View.GONE -> linearLayout1.visibility = View.VISIBLE
                linearLayout2.visibility == View.GONE -> linearLayout2.visibility = View.VISIBLE
                linearLayout3.visibility == View.GONE -> {
                    linearLayout3.visibility = View.VISIBLE
                    InformationAlertDialog(getString(R.string.more_than_4_answer)).show(fragmentManager!!, "confirmationDialog")
                }
                /* answerEditText4.visibility == View.GONE -> answerEditText4.visibility = View.VISIBLE
                   answerEditText5.visibility == View.GONE -> answerEditText5.visibility = View.VISIBLE
                   answerEditText6.visibility == View.GONE -> answerEditText6.visibility = View.VISIBLE
                   answerEditText7.visibility == View.GONE -> answerEditText7.visibility = View.VISIBLE
                   answerEditText8.visibility == View.GONE -> answerEditText8.visibility = View.VISIBLE
                   answerEditText9.visibility == View.GONE -> answerEditText9.visibility = View.VISIBLE
                   answerEditText10.visibility == View.GONE -> answerEditText10.visibility = View.VISIBLE*/
                else -> {
                    addIncorrectAnserButton.visibility = View.GONE
                }
            }
        }

        deleteAnswerButton1.setOnClickListener {
            linearLayout1.visibility = View.GONE
            answerEditText1.text.clear()
        }

        deleteAnswerButton2.setOnClickListener {
            linearLayout2.visibility = View.GONE
            answerEditText2.text.clear()
        }

        deleteAnswerButton3.setOnClickListener {
            linearLayout3.visibility = View.GONE
            answerEditText3.text.clear()
        }

        deleteAnswerButton4.setOnClickListener {
            linearLayout4.visibility = View.GONE
            answerEditText4.text.clear()
        }

        if (parentView.questionTopCounter.text.toString().contains("1")) {
            backButton.visibility = View.GONE
        } else {
            backButton.visibility = View.VISIBLE
        }

        backButton.setOnClickListener {
            val question = if (null == objectToPresent) questionList[questionList.count() - 1] else questionList[questionList.indexOf(objectToPresent!!) -1]
            when (question.type) {
                QuestionType.MULTIPLE.name -> {
                    loadMySelf(question)
                }
                QuestionType.TRUE_FALSE.name -> {
                    parentView.fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    parentView.fragmentTransaction.replace(R.id.fragmentLayout, TrueFalseQuestionFragment(packageId,
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
        if (parentView.questionTopCounter.text.toString().contains("1")) {
            backButton.visibility = View.GONE
        } else {
            backButton.visibility = View.VISIBLE
        }
        questionSentenceEditText.setText(question.interrogativeSentence)
        sujectionSentenceEditText.setText(question.subjectionSentence)
        var count = -1
        question.answers?.forEach { ans ->
            count++
            when {
                ans.contains("*") -> {
                    answerEditText.setText(ans)
                    linearLayout1.visibility = View.VISIBLE
                }
                count == 1 -> {
                    answerEditText1.setText(ans)
                    linearLayout2.visibility = View.VISIBLE
                }
                count == 2 -> {
                    answerEditText2.setText(ans)
                    linearLayout3.visibility = View.VISIBLE
                }
                count == 3 -> {
                    answerEditText3.setText(ans)
                    linearLayout4.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun clearAll(view: View, savedInstanceState: Bundle?) {
        answerEditText.text.clear()
        answerEditText1.text.clear()
        answerEditText2.text.clear()
        answerEditText3.text.clear()
        answerEditText4.text.clear()
        questionSentenceEditText.text.clear()
        sujectionSentenceEditText.text.clear()

        val numberOfQuestion = if (questionList.count() > 0) questionList.count() + 1  else  1
        parentView.questionTopCounter.text = "${resources.getText(R.string.question)} $numberOfQuestion"

        this.onViewCreated(view, savedInstanceState)
    }

    private fun groupAnswers() {
        if (!answerEditText.text.isNullOrBlank()) {
            listOfAnswers.add("*" + answerEditText.text.toString())
        }
        if (!answerEditText1.text.isNullOrBlank()) {
            listOfAnswers.add(answerEditText1.text.toString())
        }
        if (!answerEditText2.text.isNullOrBlank()) {
            listOfAnswers.add(answerEditText2.text.toString())
        }
        if (!answerEditText3.text.isNullOrBlank()) {
            listOfAnswers.add(answerEditText3.text.toString())
        }
    }
}
