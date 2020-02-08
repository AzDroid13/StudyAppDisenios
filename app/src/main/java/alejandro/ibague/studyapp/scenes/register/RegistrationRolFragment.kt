package alejandro.ibague.studyapp.scenes.register

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_registration_rol.*

class RegistrationRolFragment: Fragment() {
    private val args by navArgs<RegistrationRolFragmentArgs>()
    private var rol: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration_rol, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        progressBar.progress = 3
        progressBar.max = 5

        hideKeyboard(view, this.context)

        studentButton.setOnClickListener { it as Button
            rol = "S"
            nextButton.visibility = View.VISIBLE
            it.setTextColor(resources.getColor( R.color.white))
            it.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.MULTIPLY)
            teacherButton.setTextColor(resources.getColor(R.color.khaky))
            teacherButton.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.MULTIPLY)
        }

        teacherButton.setOnClickListener { it as Button
            rol = "T"
            nextButton.visibility = View.VISIBLE
            it.setTextColor(resources.getColor( R.color.white))
            it.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.MULTIPLY)
            studentButton.setTextColor(resources.getColor(R.color.khaky))
            studentButton.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.MULTIPLY)
        }

        nextButton.setOnClickListener {
            val userInformation = args.userInformation
            userInformation?.rol = this.rol
            val action = RegistrationRolFragmentDirections.actionRegistrationRolFragmentToRegistrationPasswordFragment(userInformation)
            navController.navigate(action)
        }
    }
}