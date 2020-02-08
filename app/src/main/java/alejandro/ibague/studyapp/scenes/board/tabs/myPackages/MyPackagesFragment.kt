package alejandro.ibague.studyapp.scenes.board.tabs.myPackages

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.MyPackageRecyclerViewAdapter
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

class MyPackagesFragment(private val hostFragment: Fragment): Fragment() {
    private val viewModel: MyPackagesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_my_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        viewModel.packageList.observe(this, Observer<List<PackageEntity>> { packages ->
            val justMyPackages = packages.filter {
                it.userId == LoginViewModel.userToken!!
            }

            val viewAdapter = MyPackageRecyclerViewAdapter(justMyPackages,
                MyPackageInteractionListener(hostFragment), activity!!.baseContext)
            showOnScreen(viewAdapter, packageContentRecyclerView)
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
