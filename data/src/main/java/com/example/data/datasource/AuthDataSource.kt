package com.example.data.datasource

import com.example.data.interfaces.AuthInterface
import com.example.domain.database.entities.UserEntity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthDataSource @Inject constructor(): AuthInterface {
    override suspend fun login(email: String, password: String) {

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val uid = FirebaseAuth.getInstance().uid
                if (auth.currentUser!!.isEmailVerified) {
                    val users =
                        FirebaseDatabase.getInstance(
                            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                        )
                            .getReference("/users/")
                    val user = users.get()
                    Tasks.await(user)
                    if (user.result.exists()) {
                        var userIsExisted = false
                        user.result.children.forEach{ dataUser ->
                            val localUser = dataUser.getValue(UserEntity::class.java)
                            if (localUser != null) {
                                if (localUser.userUidId == uid) {
                                    userIsExisted = true
                                }
                            }

                        }

                        if (userIsExisted) {
                            // go to app
                        } else {
                            // suck
                        }
                    } else {
                        // suck
                    }
                } else {
                    // verification suck
                }

            }
        }
            .addOnFailureListener {
            // ultra suck
        }
    }
}