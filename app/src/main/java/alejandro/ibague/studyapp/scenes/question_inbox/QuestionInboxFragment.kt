package alejandro.ibague.studyapp.scenes.question_inbox

import alejandro.ibague.studyapp.infrastucture.bindingListeners.SwipeToDeleteCallback
import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.QuestionInboxRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_question_inbox.*
import kotlinx.android.synthetic.main.top_header_creator.*

class QuestionInboxFragment : Fragment() {
    private val viewModel: QuestionInboxViewModel by activityViewModels()

    lateinit var listOfQuestions: List<QuestionEntity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val packageId = arguments?.get("packageId") as String

        header.layoutResource = R.layout.top_header_editor
        header.inflate()

        rightSideImageView.setImageResource(R.drawable.checkmark_icon)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_questionInboxFragment_to_main_board_fragment)
            }
        })

        //Navigate to main board fragment.
        rightSideImageView.setOnClickListener {
            findNavController().navigate(R.id.action_questionInboxFragment_to_main_board_fragment)
        }

        createQuestionActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_questionInboxFragment_to_questionContainerFragment,
                bundleOf("packageId" to packageId))
        }

        viewModel.fetchQuestionOfPackage(packageId).observe(this, Observer { questionList ->
            listOfQuestions = questionList.toMutableList()
            val viewAdapter = QuestionInboxRecyclerViewAdapter(questionList.toMutableList(), null, activity!!.baseContext)
            showOnScreen(viewAdapter, questionInboxRecyclerView)
        })
    }

    private fun showOnScreen(viewAdapter2: QuestionInboxRecyclerViewAdapter, recycleView: RecyclerView,
                             layoutOrientation: Int = RecyclerView.VERTICAL ){
        val linearLayoutManager = LinearLayoutManager(activity!!.baseContext, layoutOrientation, false)
        val viewManager: RecyclerView.LayoutManager = linearLayoutManager
        val viewAdapter: QuestionInboxRecyclerViewAdapter = viewAdapter2

        recycleView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //val confirmationDialog = ConfirmationDialog(viewAdapter)
                //confirmationDialog.show(fragmentManager!!, "LoginFragment")

                //TODO: Confirmation dialog appear and receive result.
                val adapter = recycleView.adapter
                val position = viewHolder.adapterPosition

                viewModel.removeQuestionId(listOfQuestions[position], position)

                adapter!!.notifyItemRemoved(position)
             }

        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycleView)
    }
}
