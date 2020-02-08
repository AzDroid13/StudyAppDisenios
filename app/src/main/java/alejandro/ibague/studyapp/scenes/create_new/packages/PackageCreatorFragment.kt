package alejandro.ibague.studyapp.scenes.create_new.packages

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.infrastucture.commons.Constants
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import alejandro.ibague.studyapp.scenes.dialogs.InformationAlertDialog
import alejandro.ibague.studyapp.scenes.login.LoginViewModel
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Switch
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_package_creator.*
import kotlinx.android.synthetic.main.top_header_creator.*

class PackageCreatorFragment : Fragment() {
    private val packageViewModel by activityViewModels<PackageCreatorViewModel>()
    private var accessPublic: Boolean = false
    private var categorySelected: String = ""

    /*var file: Uri? = null

    companion object {
        private const val PERMISSION_CODE = 1001
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_package_creator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        header.layoutResource = R.layout.top_header_creator
        header.inflate()

        rightSideImageView.visibility = View.INVISIBLE
        packageCostEditText.visibility = View.GONE

        nextButton.setTextColor(resources.getColor(R.color.aqua))

        spinnerSetup()
        setPackageConfigurationButtonListener()
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val data = sharedPref.getString("userToken", null)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_packageCreatorFragment_to_questionInboxFragment,
                bundleOf("packageId" to Constants.SANDBOX_PACKAGE_ID))
            }
        })

        nextButton.setOnClickListener {
            if (!packageNameEditText.text.toString().isBlank() && !categorySelected.isBlank()) {

                val packagePrince = if(!packageCostEditText.text.toString().isBlank()){
                      packageCostEditText.text.toString().toFloat()
                } else {
                    0.0f
                }

                packageViewModel.verifyNamePackage(packageNameEditText.text.toString(), data).observe(this, Observer { packageList ->
                    val cant = packageList.count()
                    if (cant >= 1){
                        InformationAlertDialog(getString(R.string.repeated_name)).show(fragmentManager!!, "confirmationDialog")
                    } else {
                        saveChanges(packagePrince)
                    }
                })

                /*if (!file?.path.isNullOrBlank()) {
                    packageViewModel.uploadPhoto(file!!).observe(this, Observer { imageURL ->
                        packageEntity.thumbnailImage = imageURL

                    })
                } */
            } else {
                displayInvalidMessage()
            }
        }

        showDropDownOnClickOnFocusEventOccur()

        /*
        pickPackageImageView.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(this@PackageCreatorFragment.context!!, READ_EXTERNAL_STORAGE)
            if (permission != PackageManager.PERMISSION_GRANTED) { //permission denied
                val permissions = arrayOf(READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE) //show popup to request runtime permission
            }
            else{ //permission already granted
                pickImageFromGallery()
            }
        }*/
    }

    private fun saveChanges(price: Float) {
        val packageEntity = PackageEntity(name = packageNameEditText.text.toString(),
            isPublicAccess = accessPublic,
            category = categorySelected,
            description = descriptionEditText.text.toString(),
            thumbnailImage = "https://firebasestorage.googleapis.com/v0/b/studyapp-1569758172421.appspot.com/o/images%2F241143216?alt=media",
            identifier = "",
            price = price, userId = LoginViewModel.userToken!!)

        Toast.makeText(context, R.string.file_upload_success, Toast.LENGTH_SHORT).show()
        packageViewModel.createPackage(packageEntity)

        PackageCreatorViewModel.registrationState.observe(this, Observer { packageId ->
            packageViewModel.updateSandboxQuestionWith(packageId)

            PackageCreatorViewModel.registrationState.removeObservers(this)
            findNavController().navigate(R.id.action_packageCreatorFragment_to_main_board_fragment)
        })
    }

    private fun showDropDownOnClickOnFocusEventOccur() {
        // Display the auto complete if the focus is on it.
        categoryAutoCompleteTextView.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                categoryAutoCompleteTextView.hint = ""
                (v as AutoCompleteTextView).showDropDown()
                v.postDelayed({hideKeyboard(v, context)}, 1000)
            } else {
                categoryAutoCompleteTextView.hint = resources.getString(R.string.select_category)
            }
        }
    }

    private fun spinnerSetup() {
        val adapter = ArrayAdapter.createFromResource(this.activity!!.baseContext, R.array.subject_array, R.layout.support_simple_spinner_dropdown_item)
        val arrayList: ArrayList<String> = arrayListOf(*resources.getStringArray(R.array.subject_array))

        categoryAutoCompleteTextView.setAdapter(adapter)

        categoryAutoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            //Cleaning hing //Select category.
            categoryAutoCompleteTextView.hint = ""
            categorySelected = if (arrayList.contains(parent.getItemAtPosition(position).toString())) {
                parent.getItemAtPosition(position).toString()
            }  else {
                ""
            }
        }
    }

    private fun displayInvalidMessage() {
        Snackbar.make(this@PackageCreatorFragment.view!!, getString(R.string.complete_info_message),
                      Snackbar.LENGTH_SHORT).show()
    }

    private fun setPackageConfigurationButtonListener() {
        accessModifierSwitch2.setOnClickListener {
            if (!accessModifierSwitch2.isChecked) {
                accessPublic = false
                accessModifierSwitch2.trackDrawable.setColorFilter(ContextCompat.getColor(this.context!!, R.color.khaky), PorterDuff.Mode.SRC_IN)
                accessModifierSwitch2.thumbDrawable.setColorFilter(ContextCompat.getColor(this.context!!, R.color.darkGray), PorterDuff.Mode.SRC_IN)
            } else {
                accessPublic = true
                accessModifierSwitch2.thumbDrawable.setColorFilter(ContextCompat.getColor(this.context!!, R.color.olive), PorterDuff.Mode.SRC_IN)
                accessModifierSwitch2.trackDrawable.setColorFilter(ContextCompat.getColor(this.context!!, R.color.green), PorterDuff.Mode.SRC_IN)
            }
        }

        accessModifierSwitch3.setOnClickListener { it as Switch
            if (!it.isChecked) {
                it.trackDrawable.setColorFilter(ContextCompat.getColor(this.context!!, R.color.khaky), PorterDuff.Mode.SRC_IN)
                it.thumbDrawable.setColorFilter(ContextCompat.getColor(this.context!!, R.color.darkGray), PorterDuff.Mode.SRC_IN)
            } else {
                it.thumbDrawable.setColorFilter(ContextCompat.getColor(this.context!!, R.color.olive), PorterDuff.Mode.SRC_IN)
                it.trackDrawable.setColorFilter(ContextCompat.getColor(this.context!!, R.color.green), PorterDuff.Mode.SRC_IN)
            }

            if (accessModifierSwitch3.isChecked) {
                packageCostEditText.visibility = View.VISIBLE
            } else {
                packageCostEditText.visibility = View.GONE
            }
        }
    }

    /*
    private fun pickImageFromGallery() {
        CropImage.activity().setAutoZoomEnabled(true)
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(context!!, this)
    }
    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                file = result.uri
                pickPackageImageView.setImageURI(file)
            }
        }
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery() //permission from popup granted
                }
            }
        }
    } */
}