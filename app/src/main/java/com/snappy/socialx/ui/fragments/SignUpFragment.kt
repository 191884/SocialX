package com.snappy.socialx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.snappy.socialx.databinding.FragmentSignUpBinding
import com.snappy.socialx.ui.activity.MainActivity

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater,container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.Register.setOnClickListener {
            when {
                // Checking if any field is not left empty

                TextUtils.isEmpty(binding.UserName.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.emailId.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.phoneNo.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "Please enter your contact", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email: String = binding.emailId.text.toString().trim{ it <= ' ' }
                    val password: String = binding.password.text.toString().trim{ it <= ' ' }

                    //Create an instance and register a user with email and password.
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            //If the registration is successful
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                //Firebase registered user
                                Toast.makeText(context, "You're registered successfully!", Toast.LENGTH_SHORT).show()

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