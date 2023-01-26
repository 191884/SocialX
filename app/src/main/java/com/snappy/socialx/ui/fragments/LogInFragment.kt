package com.snappy.socialx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.snappy.socialx.R
import com.snappy.socialx.databinding.FragmentLogInBinding
import com.snappy.socialx.ui.activity.MainActivity

class LogInFragment: Fragment() {

    private lateinit var binding: FragmentLogInBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(layoutInflater,container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(requireContext(),gso)

        firebaseAuth= FirebaseAuth.getInstance()

        binding.googleSignIn.setOnClickListener{ view: View? ->
            Toast.makeText(requireContext(),"Logging In",Toast.LENGTH_SHORT).show()
            signInGoogle()
        }


        binding.loginButton.setOnClickListener {
            when {
                //Making sure if any field is not left empty

                TextUtils.isEmpty(binding.emailId.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(binding.password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email: String = binding.emailId.text.toString().trim{ it <= ' ' }
                    val password: String = binding.password.text.toString().trim{ it <= ' ' }

                    //LogIn using FirebaseAuth
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            //If the LogIn is successful
                            if (task.isSuccessful) {

                                //Firebase registered user
                                Toast.makeText(context, "You've logged in successfully!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                            }else {
                                //If the registration is not successful then show error message
                                Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            }

        }
    }

    private  fun signInGoogle(){

        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
//            firebaseAuthWithGoogle(account!!)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(requireContext(),e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if(GoogleSignIn.getLastSignedInAccount(requireContext())!=null){
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }
        updateUI(currentUser)
    }
    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null){
            val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(mainActivityIntent)
            activity?.finish()
        }
    }

}
