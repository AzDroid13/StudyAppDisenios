package alejandro.ibague.studyapp.scenes.answers

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.database.entities.UserEntity
import alejandro.ibague.studyapp.infrastucture.commons.Constants.Companion.DelayTimeForTransition
import alejandro.ibague.studyapp.infrastucture.commons.QuestionType
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_quiz_container.*

class QuizContainerFragment: Fragment() {
    lateinit var fragmentTransaction: FragmentTransaction
    private val viewModel by activityViewModels<QuizContainerViewModel>()

    companion object {
        var cantTrueAnswers = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        header.layoutResource = R.layout.top_header_quiz
        header.inflate()

        val packageId = arguments?.get("packageId") as String
        setupHeaderInformation(packageId)
        searchQuestionsOf()
    }

    private fun setupHeaderInformation(packageId: String) {
        viewModel.getPackageInformation(packageId)
        viewModel.packageInformation.observe(this, Observer<PackageEntity> { packageInfo ->
            packageTitleTextView.text = packageInfo.name
            viewModel.getAuthorInformation(packageInfo.userId).observe(this, Observer<UserEntity> { userInfo ->
                packageAuthorTextView.text = userInfo.name
            })

            viewModel.packageInformation.removeObservers(this)
        })
    }

    private fun searchQuestionsOf() {
        //viewModel.getAllQuestionOf(packageId)
        viewModel.questionList.observe(this, Observer<List<QuestionEntity>> { listOfQuestions ->
            if (!listOfQuestions.isNullOrEmpty()) {
                progressBar.max = listOfQuestions.count()
                navigateToNextPosition(listOfQuestions, 0)
            }
        })
    }

    fun navigateToNextPosition(listOfQuestions: List<QuestionEntity>, position: Int) {
        if (position > listOfQuestions.count() - 1) {
            Handler().postDelayed( {
                findNavController().navigate(R.id.action_quizContainerFragment_to_questionInboxFragment,
                    bundleOf("packageId" to arguments?.get("packageId") as String)
                )
            }, DelayTimeForTransition / 3)
        } else {
            when (listOfQuestions[position].type) {
                QuestionType.MULTIPLE.name -> {
                    fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    val displayFragment =
                        ABCAnswerFragment(this, listOfQuestions, position, progressBar)
                    fragmentTransaction.replace(R.id.fragmentLayout, displayFragment).commit()
                    fragmentLayout.visibility = View.VISIBLE
                }

                QuestionType.TRUE_FALSE.name -> {
                    fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    val displayFragment =
                        TrueFalseAnswerFragment(this, listOfQuestions, position, progressBar)
                    fragmentTransaction.replace(R.id.fragmentLayout, displayFragment).commit()
                    fragmentLayout.visibility = View.VISIBLE
                }

                QuestionType.EXACT.name -> {
                    fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    val displayFragment =
                        ExactAnswerFragment(this, listOfQuestions, position, progressBar)
                    fragmentTransaction.replace(R.id.fragmentLayout, displayFragment).commit()
                    fragmentLayout.visibility = View.VISIBLE
                }
            }
        }
    }
}