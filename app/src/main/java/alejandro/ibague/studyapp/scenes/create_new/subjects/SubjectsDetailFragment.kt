package alejandro.ibague.studyapp.scenes.create_new.subjects

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.MyPackageRecyclerViewAdapter
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.MySubjectsRecyclerViewAdapter
import alejandro.ibague.studyapp.infrastucture.bindingListeners.MyPackageInteractionListener
import alejandro.ibague.studyapp.scenes.login.LoginViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_my_packages.*
import kotlinx.android.synthetic.main.fragment_subjects_detail.*
import kotlinx.android.synthetic.main.top_header_subjects_detail.*

class SubjectsDetailFragment:  Fragment() {
    private val viewModel: SubjectsDetailViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subjects_detail, container, false)
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header.layoutResource = R.layout.top_header_subjects_detail
        header.inflate()
        //get the subject created

        //set the title and color from the subject created
        //textView1.setText("Languajes")
        //top_header_creator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        //set the button color from the subject createdp........................................................................................../;..

        //inviteFriendsButton.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color1))
        //addPackagesButton.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.rounded_subject_color1))

        viewModel.loadData()
        viewModel.packageList.observe(this, Observer<List<PackageEntity>> { packages ->
            val justMyPackages = packages.filter{
                it.userId == LoginViewModel.userToken!!
            }
            val viewAdapter = MySubjectsRecyclerViewAdapter(justMyPackages,
                null, activity!!.baseContext)
            showOnScreen(viewAdapter, subjectsDetailRecyclerView)
        })
    }

    private fun showOnScreen(viewAdapter2: RecyclerView.Adapter<*>, recycleView: RecyclerView,
                             layoutOrientation: Int = RecyclerView.VERTICAL ){
        val linearLayoutManager = LinearLayoutManager(activity!!.baseContext, layoutOrientation, false)
        val viewManager: RecyclerView.LayoutManager = linearLayoutManager
        val viewAdapter: RecyclerView.Adapter<*> = viewAdapter2

        recycleView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

}
