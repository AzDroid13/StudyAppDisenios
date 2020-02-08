package alejandro.ibague.studyapp.scenes.register

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.UserEntity
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import alejandro.ibague.studyapp.scenes.commons.isValidEmail
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_registration_email.*

class RegistrationEmailFragment: Fragment() {
    private val registrationViewModel by activityViewModels<RegistrationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.OVERLAY)

        val navController = findNavController()

        nextButton.setOnClickListener {
            val userInformation = UserEntity("", "", emailEditText.text.toString(), "",
                "","", "","")
            if (isValid(view)) { // Validate if had email.
                val action = RegistrationEmailFragmentDirections.actionRegistrationEmailFragmentToRegistrationNameFragment(userInformation)
                navController.navigate(action)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            registrationViewModel.userCancelledRegistration()
            navController.popBackStack(R.id.login_fragment, false)
        }
    }

    private fun isValid(view: View): Boolean {
        return if(emailEditText.text.toString().isNotEmpty() && emailEditText.text.toString().isValidEmail()) {
            //TODO: Validate if no previous exist. (remote)
            true
        } else {
            hideKeyboard(view, this.context)
            emailEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.OVERLAY)
            false
        }
    }
}