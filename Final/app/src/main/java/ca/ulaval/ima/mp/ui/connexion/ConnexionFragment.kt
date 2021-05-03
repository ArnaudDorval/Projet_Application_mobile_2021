package ca.ulaval.ima.mp.ui.connexion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import ca.ulaval.ima.mp.MainActivity
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.AccountLogin
import ca.ulaval.ima.mp.model.TokenOutput
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import ca.ulaval.ima.mp.ui.RestaurantDetailsActivity
import ca.ulaval.ima.mp.ui.inscription.InscriptionFragment
import ca.ulaval.ima.mp.ui.moncompte.MonCompteFragment
import ca.ulaval.ima.mp.ui.parcelables.ParcelDataAPI
import ca.ulaval.ima.mp.utils.Token

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class ConnexionFragment : Fragment() {
    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)
    lateinit var myToken : TokenOutput
    lateinit var pLoginEmail: EditText
    lateinit var pLoginPassword:EditText
    private  var pSignupFirstName:EditText? = null
    private  var pSignupLastName:EditText? = null
    private  var pSignupEmail:EditText? = null
    private  var pSignupPassword:EditText? = null
    lateinit var pLoginButton: Button
    private  var pSignupButton:android.widget.Button? = null
    private  var pSignupToggle:android.widget.Button? = null
    lateinit var pLoginToggle : Button
    private var currentActivity: Activity? = null
    lateinit var accountLogin : AccountLogin
    lateinit var client_id : String
    lateinit var client_secret : String

    private val token: Token ?= null
    private lateinit var root : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_connexion, container, false)
        pLoginToggle = root.findViewById(R.id.loginToggle)
        pLoginButton = root.findViewById(R.id.loginButton)

        pLoginEmail = root.findViewById(R.id.loginEmail)
        pLoginPassword = root.findViewById(R.id.loginPassword)
        pLoginButton = root.findViewById<Button>(R.id.loginButton)
        pLoginToggle = root.findViewById<Button>(R.id.loginToggle)

        client_id = (activity as MainActivity).getClientId()
        client_secret = (activity as MainActivity).getClientSecret()

        currentActivity = activity

        pLoginToggle.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_host_fragment, InscriptionFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }

        pLoginButton.setOnClickListener {
            accountLogin = AccountLogin(client_id, client_secret, pLoginEmail.text.toString(), pLoginPassword.text.toString())
            getToken(accountLogin)
        }



        return root
    }

    fun getToken(userInfo: AccountLogin) {
        imaNetworkCenter.postAccountLogin(userInfo).enqueue(object :
                Callback<KungryAPI.ContentResponse<TokenOutput>> {
            override fun onResponse(
                    call: Call<KungryAPI.ContentResponse<TokenOutput>>,
                    response: Response<KungryAPI.ContentResponse<TokenOutput>>
            ) {
                if (response.isSuccessful){
                    response.body()?.content?.let {
                        myToken = it
                        Log.d("Token", myToken.access_token)
                        if (currentActivity is MainActivity) {
                            if(myToken!= null){
                                (currentActivity as MainActivity).setCurrentToken(myToken)
                            }
                        }
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.nav_host_fragment, MonCompteFragment())
                        transaction?.commit()
                    }
                }
                Log.d("ima-demo", "postReviewPhoto onResponse $token")

            }

            override fun onFailure(
                    call: Call<KungryAPI.ContentResponse<TokenOutput>>,
                    t: Throwable
            ) {
                Log.d("ima-demo", "postReviewPhoto onFailure $t")
            }
        })
    }
}