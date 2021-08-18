package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.AuthInterface
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class AuthDataSource @Inject constructor() : AuthInterface {

  override suspend fun login(email: String, password: String): SingleLiveEvent<Response<String>> {
    val response: SingleLiveEvent<Response<String>> = SingleLiveEvent()
    response.value = Response.loading(null)
    val auth = FirebaseAuth.getInstance()
    auth
      .signInWithEmailAndPassword(email, password)
      .addOnCompleteListener {
        if (it.isSuccessful) {
          val uid = FirebaseAuth.getInstance().uid ?: "def"
          if (auth.currentUser!!.isEmailVerified) {

            val users =
              FirebaseDatabase.getInstance(
                  "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                )
                .getReference("/users/$uid")

            users.addValueEventListener(
              object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                  if (snapshot.exists()) {
                    val result = snapshot.getValue(UserEntity::class.java)
                    if (result != null) {
                      response.value = Response.success("Successfully logged in")
                    } else {
                      response.value = Response.error("No user found", null)
                    }
                  } else {
                    response.value = Response.error("No user found", null)
                  }
                }

                override fun onCancelled(error: DatabaseError) {
                  response.value = Response.error(error.message, null)
                }
              }
            )

          } else {
            response.value = Response.error("Verify yor account from email", null)
          }
        } else if (it.isCanceled) {
          response.value = Response.error("Login Canceled", null)
        }
      }
      .addOnFailureListener {
        response.value = Response.error(it.message!!, null)
      }
      .addOnCanceledListener {
        response.value = Response.error("login error", null)
      }
      return response
  }
}
