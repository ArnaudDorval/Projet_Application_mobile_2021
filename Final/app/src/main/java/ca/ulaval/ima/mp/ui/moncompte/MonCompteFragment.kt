package ca.ulaval.ima.mp.ui.moncompte

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ca.ulaval.ima.mp.MainActivity
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.Account
import ca.ulaval.ima.mp.model.AccountLogin
import ca.ulaval.ima.mp.model.CreateAccountCreate
import ca.ulaval.ima.mp.model.TokenOutput
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonCompteFragment : Fragment() {

    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)
    lateinit var userAccount : Account
    var tokenOutput: TokenOutput? = null
    lateinit var nameTextview : TextView
    lateinit var emailTextview : TextView
    lateinit var monCompte_nbreEvalTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_mon_compte, container, false)
        tokenOutput = (activity as MainActivity).getCurrentToken()
        nameTextview = root.findViewById(R.id.monCompte_nomProfilTextView)
        emailTextview = root.findViewById(R.id.monCompte_EmailTextView)
        monCompte_nbreEvalTextView = root.findViewById(R.id.monCompte_nbreEvalTextView)
        createAccount()

        return root
    }

   fun createAccount(){
        imaNetworkCenter.getAccountMe("Bearer " + tokenOutput?.access_token ).enqueue(object :
            Callback<KungryAPI.ContentResponse<Account>> {
            override fun onResponse(
                call: Call<KungryAPI.ContentResponse<Account>>,
                response: Response<KungryAPI.ContentResponse<Account>>
            ) {
                if (response.isSuccessful){
                    response.body()?.content?.let {
                        userAccount = it
                        nameTextview.text = userAccount.first_name + " " + userAccount.last_name
                        emailTextview.text = userAccount.email
                        monCompte_nbreEvalTextView.text = userAccount.total_review_count.toString()
                    }
                }
            }

            override fun onFailure(
                call: Call<KungryAPI.ContentResponse<Account>>,
                t: Throwable
            ) {
                Log.d("ima-demo", "postReviewPhoto onFailure $t")
            }
        })
    }

}