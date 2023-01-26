package com.snappy.socialx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.snappy.socialx.ui.activity.MainActivity
import com.snappy.socialx.databinding.FragmentLogInBinding

class LogInFragment: Fragment() {
    private lateinit var binding: FragmentLogInBinding
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
    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
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
