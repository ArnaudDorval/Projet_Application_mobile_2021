package ca.ulaval.ima.mp.ui.inscription

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import ca.ulaval.ima.mp.MainActivity
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.CreateAccountCreate
import ca.ulaval.ima.mp.ui.connexion.ConnexionFragment

class InscriptionFragment : Fragment() {

    lateinit var nomEditText: EditText
    lateinit var prenomEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var pwdEditText: EditText
    lateinit var loginButton : Button
    lateinit var client_id : String
    lateinit var client_secret : String
    lateinit var userInscription : CreateAccountCreate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_inscription, container, false)
        loginButton = root.findViewById(R.id.loginButton)
        nomEditText = root.findViewById(R.id.inscription_nomEditText)
        prenomEditText =root.findViewById(R.id.inscription_prenomEditText)
        emailEditText = root.findViewById(R.id.loginEmail)
        pwdEditText = root.findViewById(R.id.loginPassword)

        client_id = (activity as MainActivity).getClientId()
        client_secret = (activity as MainActivity).getClientSecret()



        loginButton.setOnClickListener {
            userInscription = CreateAccountCreate(client_id, client_secret, prenomEditText.text.toString(),
                nomEditText.text.toString(), emailEditText.text.toString(), pwdEditText.text.toString())

        }

        var pLoginToggle = root.findViewById<Button>(R.id.loginToggle)
        pLoginToggle.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_host_fragment, ConnexionFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }
        return root
    }

}