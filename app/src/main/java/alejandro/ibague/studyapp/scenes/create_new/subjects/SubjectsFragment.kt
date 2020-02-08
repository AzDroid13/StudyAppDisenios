package alejandro.ibague.studyapp.scenes.create_new.subjects

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.SubjectEntity
import alejandro.ibague.studyapp.scenes.login.LoginViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.android.synthetic.main.top_header_quiz.*

class SubjectsFragment : Fragment(), View.OnClickListener {
    private val subjectsViewModel by activityViewModels<SubjectsViewModel>()
    private var select = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subjects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        header.layoutResource = R.layout.top_header_quiz
        header.inflate()
        textView1.setText(R.string.subjects_title)
        top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))

        selectColorRedButton.setOnClickListener(this)
        selectColorOrangeButton.setOnClickListener(this)
        selectColorYellowButton.setOnClickListener(this)
        selectColorGreenButton.setOnClickListener(this)
        selectColorTurquoiseLightButton.setOnClickListener(this)
        selectColorPurpleButton.setOnClickListener(this)
        selectColorVioletButton.setOnClickListener(this)
        createSubject.setOnClickListener {
            //Get the color selected

            //Create the Subject
            val subjectEntity = SubjectEntity(
                name = subjectNameEditText.text.toString(),
                color = select,
                guess = 0,
                packages = 0,
                qAndA = 0,
                active = true,
                identifier = "",
                userId = LoginViewModel.userToken!!
            )
            //I need the persistence
            Toast.makeText(context, R.string.file_upload_success, Toast.LENGTH_SHORT).show()
            subjectsViewModel.createSubject(subjectEntity)
            //Report if bad work
            //Next fragment
            findNavController().navigate(R.id.subjectsDetailFragment)

        }
    }


    override fun onClick(v: View?) {
        select = v?.getTag().toString().toInt()
        when(select){
            1 -> {
                top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_dark))
                createSubject.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color1))
            }
            2 -> {
                top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                createSubject.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color2))
            }
            3 -> {
                top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                createSubject.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color3))
            }
            4 -> {
                top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_light))
                createSubject.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color4))
            }
            5 -> {
                top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.violet))
                createSubject.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color5))
            }
            6 -> {
                top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
                createSubject.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color6))
            }
            7 -> {
                top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fuchsia))
                createSubject.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color7))
            }
        }
    }
}