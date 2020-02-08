package alejandro.ibague.studyapp.infrastucture.commons

import android.view.View

interface InteractionListener<M>: View.OnClickListener {
    fun onListenerFragmentInteraction(item: M?)
}