package alejandro.ibague.studyapp.scenes.register

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.scenes.login.LoginViewModel
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_registration_password.*

class RegistrationPasswordFragment: Fragment() {
    private val registrationViewModel by activityViewModels<RegistrationViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private val args by navArgs<RegistrationPasswordFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        passwordEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.OVERLAY)
        confirmationEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.OVERLAY)
        nextButton.setTextColor(resources.getColor( R.color.white))
        nextButton.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.MULTIPLY)

        progressBar.progress = 4
        progressBar.max = 5

        confirmationEditText.doOnTextChanged { text, _, _, _ ->
            if (passwordEditText.text.toString().isNotEmpty() && text.toString() == passwordEditText.text.toString()) {
                nextButton.visibility = View.VISIBLE
            } else {
                nextButton.visibility = View.GONE
            }
        }

        nextButton.setOnClickListener {
            val userInformation = args.userInformation
            userInformation?.password = confirmationEditText.text.toString()

            if (null != userInformation) {
                registrationViewModel.createAccountAndLogin(userInformation)
            }
        }

        registrationViewModel.registrationState.observe ( viewLifecycleOwner, Observer { state ->
            if (state == RegistrationViewModel.RegistrationState.REGISTRATION_COMPLETED) {
                loginViewModel.authenticate(registrationViewModel.authToken) //TODO: Auth token ???
                registrationViewModel.userCancelledRegistration() //Cleaning the process.
                navController.navigate(R.id.action_newAccountFragment_to_mainBoardFragment)

            }
        })
    }
}