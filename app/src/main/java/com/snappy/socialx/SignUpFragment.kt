package com.snappy.socialx

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import com.snappy.socialx.databinding.FragmentLoginBinding
import com.snappy.socialx.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater,container,false)

        mAuth = FirebaseAuth.getInstance()

        binding.Register.setOnClickListener{
            val name = binding.UserName.text.toString().trim()
            val email = binding.emailId.text.toString().trim()
            val password = binding.password.text.toString().trim()

            signUp(name,email, password)
        }
        return binding.root
    }
    private fun signUp(name: String ,email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val intent = Intent(requireContext(), NewActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(),"Some Error Occured", Toast.LENGTH_LONG).show()
                }
            }
    }

//    private fun addUserToDatabase(name: String, email: String, uid: String?) {
//        mDbRef = FirebaseDatabase.getInstance().reference
//        mDbRef.child("user").child(uid!!).setValue((User(name,email, uid)))
//    }
}