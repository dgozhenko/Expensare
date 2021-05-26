package com.example.expensare.ui.auth.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.expensare.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
  private val _userLiveData = MutableLiveData<FirebaseUser>()
  val userLiveData: LiveData<FirebaseUser>
    get() = _userLiveData

  fun loginUser(email: String, password: String) {
    viewModelScope.launch(Dispatchers.IO) {
      FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
          if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/")
            var userIsExisted: Boolean = false
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                  snapshot.children.forEach{
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                      if (user.uid == uid) {
                        userIsExisted = true
                      }
                    }
                  }

                  if (userIsExisted) {
                    _userLiveData.postValue(FirebaseAuth.getInstance().currentUser)
                  } else {
                    _userLiveData.postValue(null)
                  }

                }
              }

              override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
              }

            })

          } else {
            Toast.makeText(
                    getApplication<Application>().baseContext,
                    "Verify your e-mail",
                    Toast.LENGTH_SHORT)
                .show()
          }
        } else {
          return@addOnCompleteListener
        }
      }
        .addOnFailureListener {
          Toast.makeText(getApplication<Application>().applicationContext,
            "Failed to login: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
  }
  fun loginComplete() {
    _userLiveData.value = null
  }
}
