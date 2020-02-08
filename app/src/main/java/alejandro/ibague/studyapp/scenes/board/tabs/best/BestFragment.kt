package alejandro.ibague.studyapp.scenes.board.tabs.best

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.BestRecyclerViewAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_best.*

class BestFragment: Fragment() {
    private val viewModel: BestViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_best, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        economyTextView.visibility = View.GONE
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val data = sharedPref.getString("userToken", null)
        viewModel.loadDataUserIdPublic(data)
        viewModel.packageList.observe(this, Observer { elementList ->
            if (elementList.count() > 0) {
                economyTextView.visibility = View.VISIBLE
                val justMyPackages = elementList.filter {
                    it.isPublicAccess
                }.takeLast(5)

                val viewAdapter = BestRecyclerViewAdapter(justMyPackages,null, activity!!.baseContext)
                showOnScreen(viewAdapter, economyContentRecyclerView)
            }
        })
    }

    private fun showOnScreen(viewAdapter2: RecyclerView.Adapter<*>, recycleView: RecyclerView,
                             layoutOrientation: Int = RecyclerView.HORIZONTAL ){
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
