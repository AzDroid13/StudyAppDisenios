package alejandro.ibague.studyapp.scenes.board.tabs.news

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.NewsRecyclerViewAdapter
import alejandro.ibague.studyapp.models.Subject
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfItems = arrayListOf<Subject>()
        listOfItems.add(Subject("Inflacion", "Dr. Roberth"))
        listOfItems.add(Subject("Inflacion", "Prof. Martiza"))
        listOfItems.add(Subject("Inflacion", "Julio Profe"))


        val viewAdapter =
            NewsRecyclerViewAdapter(
                listOfItems,
                null,
                activity!!.baseContext
            )
        showOnScreen(viewAdapter, economyContentRecyclerView)
        showOnScreen(viewAdapter, administrationContentRecyclerView)
        showOnScreen(viewAdapter, calculusContentRecyclerView)
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
