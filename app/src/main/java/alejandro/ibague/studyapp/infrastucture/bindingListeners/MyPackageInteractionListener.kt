package alejandro.ibague.studyapp.infrastucture.bindingListeners

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.InteractionListener
import alejandro.ibague.studyapp.scenes.answers.QuizContainerViewModel
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment

class MyPackageInteractionListener(private val fragment: Fragment): InteractionListener<PackageEntity> {
    override fun onClick(p0: View?) {
        val packageId = p0?.tag as String
        val viewModel by fragment.activityViewModels<QuizContainerViewModel>()
        viewModel.getAllQuestionOf(packageId)
        viewModel.questionList.observe(fragment,  Observer { packageInfo ->
            viewModel.questionList.removeObservers(fragment)

            if (packageInfo.count() > 0) {
                NavHostFragment.findNavController(fragment).navigate(R.id.action_main_board_fragment_to_quizContainerFragment,
                    bundleOf("packageId" to packageId)
                )
            } else {
                Toast.makeText(fragment.context, fragment.resources.getText(R.string.no_questions_found), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onListenerFragmentInteraction(item: PackageEntity?) {
        Log.d("N", "FYI")
    }
}