package alejandro.ibague.studyapp.scenes.register

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_registration_username.*

class RegistrationUserNameFragment : Fragment() {
    private val args by navArgs<RegistrationUserNameFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration_username, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        nameEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.OVERLAY)

        progressBar.progress = 1
        progressBar.max = 5

        nextButton.setOnClickListener {
            val userInformation = args.userInformation
            userInformation?.username = nameEditText.text.toString()
            if (nameEditText.text.toString().isNotEmpty()) {
                val action = RegistrationUserNameFragmentDirections.actionRegistrationUserNameFragmentToRegistrationCountryFragment(userInformation)
                navController.navigate(action)
            } else {
                hideKeyboard(view, this.context)
                nameEditText.background.colorFilter =
                    PorterDuffColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.OVERLAY)
            }
        }
    }
}