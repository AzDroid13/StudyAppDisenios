package alejandro.ibague.studyapp.infrastucture.commons

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun hideKeyboard(view: View, context: Context?) {
    //Hide keyboard.
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
}