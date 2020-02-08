package alejandro.ibague.studyapp.scenes.board

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.bindingAdapters.CollectionPagerAdapter
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import alejandro.ibague.studyapp.models.QuestionEntityList
import alejandro.ibague.studyapp.scenes.login.LoginViewModel
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_board.*
import kotlinx.android.synthetic.main.top_header.*


class MainBoardFragment: Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var collectionPagerAdapter: CollectionPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard(view, context)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

        collectionPagerAdapter = CollectionPagerAdapter(fragmentManager!!, this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.setCurrentItem(1, true)
        viewPager.adapter = collectionPagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
        header.layoutResource = R.layout.top_header
        header.inflate()

        leftMenuImageView.setOnClickListener {
            val drawer = activity!!.findViewById<DrawerLayout>(R.id.drawer_layout)
            // If the navigation drawer is not open then open it, if its already open then close it.
            drawer.showContextMenu()
            if (!drawer.isDrawerOpen(GravityCompat.START))
                drawer.openDrawer(GravityCompat.START)
            else
                drawer.closeDrawer(GravityCompat.END)
        }

        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            if (authenticationState == LoginViewModel.AuthenticationState.UNAUTHENTICATED ) {
                val userIdentifier: String? = if (null != arguments?.get("user_identifier")) {
                    arguments?.get("user_identifier") as String
                } else {
                    sharedPref.getString("userToken", null)
                }
                if (userIdentifier != null) {
                    loginViewModel.authenticate(userIdentifier)
                } else {
                    findNavController().navigate(R.id.login_fragment)
                }
            } else if (authenticationState == LoginViewModel.AuthenticationState.AUTHENTICATED) {
                rightMenuImageView.setOnClickListener {
                    logoutMenuOption(it)
                }
            }
        })

        createSubjectsActionButton.setOnClickListener {
            //findNavController().navigate(R.id.action_main_board_fragment_to_SubjectFragment)
            val action = MainBoardFragmentDirections.actionMainBoardFragmentToQuestionCreatorFragment(QuestionEntityList())
                //RegistrationNameFragmentDirections.actionRegistrationNameFragmentToRegistrationUserNameFragment(userInformation)
            findNavController().navigate(action)
        }
    }

    private fun logoutMenuOption(it: View) {
        val popup = PopupMenu(this.context!!, it)
        popup.gravity = Gravity.END
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.close_session -> {
                    arguments?.clear()
                    loginViewModel.refuseAuthentication()

                    findNavController().navigate(R.id.login_fragment)
                    true
                }
                else ->
                    false
            }
        }
        val inflater = popup.menuInflater

        inflater.inflate(R.menu.actions, popup.menu)
        popup.show()
    }

    override fun onResume() {
        viewPager.adapter = CollectionPagerAdapter(childFragmentManager, this@MainBoardFragment)
        viewPager.setCurrentItem(1, true)
        collectionPagerAdapter.notifyDataSetChanged()

        super.onResume()
    }
}