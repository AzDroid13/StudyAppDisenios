package alejandro.ibague.studyapp.scenes.profile

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.MyInterestsRecyclerViewAdapter
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.MyPackageRecyclerViewAdapter
import alejandro.ibague.studyapp.infrastucture.bindingListeners.MyPackageInteractionListener
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.ArrayList


class ProfileFragment: Fragment() {
    private val viewModel: ProfileViewModel by activityViewModels()

    val packageList = ArrayList<String>()
    var file: Uri? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        header.layoutResource = R.layout.top_header_profile
        header.inflate()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val data = sharedPref.getString("userToken", null)

        val userId = data.toString() // No more provided
        loadInformation(userId)
        loadPackageFor(userId)
        loadInterestFor(userId)

        floatingActionButton.setOnClickListener {
            spinnerSetup()
        }
       // val userId = arguments?.get("userId") // No more provided
        //loadInformation(userId as Int)
       // loadPackageFor(userId)
    }

    private fun loadInformation(userId: String) {

        viewModel.fetchPersonalProfileInfo(userId).observe(this, Observer { user ->
            nameTextView.text = user.name
            emailTextView.text = user.email
            titleTextView.text = "Student"

            profileImageView.setImageURI(Uri.parse(user.thumbnailImage))
        })
    }

    private fun loadPackageFor(userId: String) {
        viewModel.fetchPackageOwner(userId).observe(this, Observer { packageList ->
            val viewAdapter =
                MyPackageRecyclerViewAdapter(packageList, MyPackageInteractionListener(this), activity!!.baseContext)
            showOnScreen(viewAdapter, packageContentRecyclerView)
        })
    }

    private fun loadInterestFor(userId: String) {

          //  val packageList = listOf("Economy", "Administracion", "Diferential Calculus")
        //    this.packageList.add("Economy")
        //this.packageList.add("Administracion")
            val viewAdapter =
                MyInterestsRecyclerViewAdapter(this.packageList,  activity!!.baseContext)
            showOnScreen(viewAdapter, interestContentRecyclerView)

    }

    //floatingActionButton,  this.activity!!.baseContext, R.array.subject_array, R.layout.item_spinner

    private fun spinnerSetup() {
        val professionalNameList = ArrayList<String>()

        professionalNameList.add("Accountancy")
        professionalNameList.add("Administration")
        professionalNameList.add("Architecture")
        professionalNameList.add("Art")
        professionalNameList.add("Art and Fine Arts")
        professionalNameList.add("Biological Sciences")

        val listItems = arrayOf("Accountancy", "Administration", "Architecture")

        val array = resources.getStringArray(R.array.subject_array)
        val mBuilder = AlertDialog.Builder(activity)
        mBuilder.setTitle("Select category")
        mBuilder.setSingleChoiceItems(array, -1) { dialogInterface, i ->
           // txtView.text = listItems[i]
            this.packageList.add(array[i])
            loadInterestFor("")
            dialogInterface.dismiss()

        }
        // Set the neutral/cancel button click listener
        mBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Do something when click the neutral button
            dialog.cancel()
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }



    private fun showOnScreen(viewAdapter2: RecyclerView.Adapter<*>, recycleView: RecyclerView,
                             layoutOrientation: Int = RecyclerView.VERTICAL) {
        val linearLayoutManager =
            LinearLayoutManager(activity!!.baseContext, layoutOrientation, false)
        val viewManager: RecyclerView.LayoutManager = linearLayoutManager
        val viewAdapter: RecyclerView.Adapter<*> = viewAdapter2

        recycleView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}