package alejandro.ibague.studyapp.infrastucture.bindingAdapters

import alejandro.ibague.studyapp.scenes.board.tabs.best.BestFragment
import alejandro.ibague.studyapp.scenes.board.tabs.myPackages.MyPackagesFragment
import alejandro.ibague.studyapp.scenes.board.tabs.news.NewsFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.os.Parcelable

class CollectionPagerAdapter(fm: FragmentManager, private val host: Fragment) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val tabs = arrayOf("Best", "My package", "News")

    override fun getCount(): Int  = tabs.count()

    override fun getItem(i: Int): Fragment {
        return when (i) {
            0 -> BestFragment()
            1 -> MyPackagesFragment(host)
            else -> NewsFragment()
        }

    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabs[position].capitalize()
    }

    override fun saveState(): Parcelable? {
        return null
    }
}