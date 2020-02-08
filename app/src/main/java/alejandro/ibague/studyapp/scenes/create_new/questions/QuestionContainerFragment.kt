package alejandro.ibague.studyapp.scenes.create_new.questions

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.infrastucture.commons.Constants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_question_container.*
import kotlinx.android.synthetic.main.top_header_creator.*

class QuestionContainerFragment : Fragment() {
    lateinit var fragmentTransaction: FragmentTransaction
    private val args by navArgs<QuestionContainerFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val packageId = Constants.SANDBOX_PACKAGE_ID
        var userInformation = args.questionList

        header.layoutResource = R.layout.top_header_creator
        header.inflate()

        var numberOfQuestion = if (userInformation!!.count() > 0) userInformation.count()  else  1

        questionTopCounter.text = "${resources.getText(R.string.question)} $numberOfQuestion"
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_questionContainerFragment_to_main_board_fragment)
            }
        })

        ArrayAdapter.createFromResource( this.activity!!.baseContext,  R.array.question_types_array,
            R.layout.item_spinner ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            questionSpinner.adapter = adapter
        }

        finishButton.setOnClickListener {
            //TODO: Display an alert with warning if the last question no click next, no will be saved. :( .

            findNavController().navigate(R.id.action_questionContainerFragment_to_packageCreatorFragment,
                bundleOf("packageId" to packageId)
            )
        }

        rightSideImageView.setOnClickListener {
            findNavController().navigate(R.id.action_questionContainerFragment_to_questionInboxFragment,
                bundleOf("packageId" to packageId))
        }


        questionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                questionSpinner.setSelection(pos)
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

                when(pos) {
                    1 -> { //ABC
                        fragmentTransaction.replace(R.id.fragmentLayout, ABCQuestionFragment(packageId,
                            fragmentLayout, questionSpinner, userInformation, this@QuestionContainerFragment, null)).commit()
                        fragmentLayout.visibility = View.VISIBLE
                    }
                    2 -> {  //True / False
                        fragmentTransaction.replace(R.id.fragmentLayout, TrueFalseQuestionFragment(packageId,
                            fragmentLayout, questionSpinner, userInformation, this@QuestionContainerFragment, null)).commit()
                        fragmentLayout.visibility = View.VISIBLE
                    }
                    3 -> {  //Exact
                        fragmentTransaction.replace(R.id.fragmentLayout, ExactQuestionFragment(packageId,
                            fragmentLayout, questionSpinner, userInformation, this@QuestionContainerFragment, null)).commit()
                        fragmentLayout.visibility = View.VISIBLE
                    }
                    else ->  { //None
                        fragmentLayout.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                fragmentLayout.visibility = View.GONE
            }
        }
    }

    /*fun navigateToBackPosition(listOfQuestions: List<QuestionEntity>, position: Int) {
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
    }*/
}
