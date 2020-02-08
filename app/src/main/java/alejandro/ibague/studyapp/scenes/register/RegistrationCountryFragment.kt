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
import kotlinx.android.synthetic.main.fragment_registration_country.*

class RegistrationCountryFragment: Fragment() {
    private val args by navArgs<RegistrationCountryFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration_country, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        progressBar.progress = 2
        progressBar.max = 5

        countryEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.OVERLAY)

        nextButton.setOnClickListener {
            val userInformation = args.userInformation
            if (countryEditText.text.toString().isNotEmpty()) {
                userInformation?.country = countryEditText.text.toString()
                val action = RegistrationCountryFragmentDirections.actionRegistrationCountryFragmentToRegistrationRolFragment(userInformation)
                navController.navigate(action)
            } else {
                hideKeyboard(view, this.context)
                countryEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.OVERLAY)
            }
        }
    }
}