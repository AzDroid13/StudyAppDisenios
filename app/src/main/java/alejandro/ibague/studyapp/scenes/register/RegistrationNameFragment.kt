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
import kotlinx.android.synthetic.main.fragment_registration_name.*

class RegistrationNameFragment: Fragment() {
    private val args by navArgs<RegistrationNameFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        nameEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.OVERLAY)
        nextButton.setOnClickListener {
            val userInformation = args.userInformation
            userInformation?.name = nameEditText.text.toString()
            if(nameEditText.text.toString().isNotEmpty()) {
                val action = RegistrationNameFragmentDirections.actionRegistrationNameFragmentToRegistrationUserNameFragment(userInformation)
                navController.navigate(action)
            } else {
                hideKeyboard(view, this.context)
                nameEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.OVERLAY)
            }
        }
    }
}
