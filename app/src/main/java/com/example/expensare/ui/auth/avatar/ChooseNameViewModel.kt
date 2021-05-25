package com.example.expensare.ui.auth.avatar

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.expensare.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChooseNameViewModel: ViewModel() {

        fun createUserInDatabase(username: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val user = User(uid, username)
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")
        reference.setValue(user).addOnSuccessListener {
            Log.d("Registration", "User created in database")
        }
            .addOnFailureListener {
                Log.d("Registration", it.message!!)
            }
    }

}