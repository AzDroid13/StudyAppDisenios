package alejandro.ibague.studyapp.scenes.login

import alejandro.ibague.studyapp.R
import alejandro.ibague.studyapp.infrastucture.commons.Constants
import alejandro.ibague.studyapp.infrastucture.commons.hideKeyboard
import alejandro.ibague.studyapp.scenes.commons.StudyService
import alejandro.ibague.studyapp.scenes.login.LoginViewModel.AuthenticationState.AUTHENTICATED
import alejandro.ibague.studyapp.scenes.login.LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: Fragment() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel: LoginViewModel by activityViewModels()

    private val CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE = 100
    private val CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE = 101

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        callbackManager = CallbackManager.Factory.create()
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build())
        view.clearFocus()

        //showFloatingView(activity, isShowOverlayPermission = true, isCustomFloatingView = false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val navController = findNavController()

        configGoogleSignInButton()
        performFacebookLogin()

        usernameEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.OVERLAY)
        passwordEditText.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.gray), PorterDuff.Mode.OVERLAY)

        //TODO: BUG-Crash: Cuando se intenta acceder luego de un primer intento fallido y esta es success.
        registerButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.to_registerNewAccountsActivity))
        normalLoginButton.setOnClickListener {
            hideKeyboard(view, this@LoginFragment.context)
            viewModel.authenticate(usernameEditText.text.toString(), passwordEditText.text.toString())
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseAuthentication()
            findNavController().popBackStack(R.id.main_board_fragment, false)
        }

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            if (authenticationState == AUTHENTICATED) {
                hideKeyboard(view, this@LoginFragment.context)
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPref.edit().putString("userToken",LoginViewModel.userToken).apply()
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainBoardFragment(LoginViewModel.userToken!!))
                Snackbar.make(this@LoginFragment.view!!, "Welcome", Snackbar.LENGTH_SHORT).show()
            }
            else if (authenticationState == INVALID_AUTHENTICATION) {
                showInvalidAuthenticationMessage()
            }
        })
    }

    private fun showInvalidAuthenticationMessage() {
        Snackbar.make(this@LoginFragment.view!!, "Credentials are invalids", Snackbar.LENGTH_SHORT).show()
        passwordEditText.text.clear()
    }

    // Signed in successfully, show authenticated UI.
    private fun performLogin(account: GoogleSignInAccount?) {
        if (account != null) {
            Toast.makeText(activity?.baseContext, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            performLogin(account)
        } catch (e: ApiException) {
            Log.w("GoogleLogin:", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun configGoogleSignInButton() {
        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, Constants.GOOGLE_INTENT_ID)
        }
    }

    private fun performFacebookLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()

        loginFacebookButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this,
                listOf("email", "public_profile", "user_friends"))
        }

        loginFacebookButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Toast.makeText(activity?.baseContext, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                Log.d("Cancel", "Cancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("Error", error?.message!!)
            }
        })

        if (accessToken != null && !accessToken.isExpired) { //isLoggedIn
            Toast.makeText(activity?.baseContext, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Constants.GOOGLE_INTENT_ID -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
            CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE -> showFloatingView(activity,
                isShowOverlayPermission = false, isCustomFloatingView = false)
            CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE -> showFloatingView(activity,
                isShowOverlayPermission = false, isCustomFloatingView = true)
        }
    }

    private fun showFloatingView(context: Context?, isShowOverlayPermission: Boolean, isCustomFloatingView: Boolean) {
        if (Settings.canDrawOverlays(context)) {
            startFloatingViewService(activity)
            return
        }

        if (isShowOverlayPermission) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context!!.packageName)
            )
            startActivityForResult(
                intent,
                if (isCustomFloatingView) CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE else CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startFloatingViewService(activity: Activity?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (activity!!.window.attributes.layoutInDisplayCutoutMode == WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER) {
                throw RuntimeException("'windowLayoutInDisplayCutoutMode' do not be set to 'never'")
            }
            // 2. Do not set Activity to landscape
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                throw RuntimeException("Do not set Activity to landscape")
            }
        }

        val service: Class<out Service>
        val key: String = StudyService.EXTRA_CUTOUT_SAFE_AREA

        service = StudyService::class.java

        val intent = Intent(activity, service)
        intent.putExtra(key, FloatingViewManager.findCutoutSafeArea(activity!!))
        ContextCompat.startForegroundService(activity, intent)
    }
}
