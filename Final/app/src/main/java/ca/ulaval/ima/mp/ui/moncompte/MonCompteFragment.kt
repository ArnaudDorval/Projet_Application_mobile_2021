package ca.ulaval.ima.mp.ui.moncompte

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.ulaval.ima.mp.MainActivity
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.AccountLogin
import ca.ulaval.ima.mp.model.CreateAccountCreate
import ca.ulaval.ima.mp.model.TokenOutput
import ca.ulaval.ima.mp.networking.KungryAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonCompteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var userAccount : AccountLogin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_mon_compte, container, false)

        return root
    }

   /* fun createAccount(userInfo : CreateAccountCreate){
        imaNetworkCenter.getAccountMe(userInfo).enqueue(object :
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
                        transaction?.disallowAddToBackStack()
                        transaction?.commit()
                    }
                }
            }

            override fun onFailure(
                call: Call<KungryAPI.ContentResponse<TokenOutput>>,
                t: Throwable
            ) {
                Log.d("ima-demo", "postReviewPhoto onFailure $t")
            }
        })


    }*/

}