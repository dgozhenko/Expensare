package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.UserInterface
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class UserDataSource @Inject constructor(private val database: ExpensareDatabase) : UserInterface {

  override suspend fun create(user: User) {
    //database.userDao().createUser(user)
  }

  override suspend fun getAll(): ArrayList<User> {
    return database.userDao().getAllUsers() as ArrayList<User>
  }

  override suspend fun downloadUser(): LiveData<Response<User>> {
    val response = MutableLiveData<Response<User>>()
    response.value = Response.loading(null)

    var userId = ""

    if (FirebaseAuth.getInstance().uid != null) {
      userId = FirebaseAuth.getInstance().uid.toString()
    } else {
      userId = "def"
    }
    val users =
      FirebaseDatabase.getInstance(
          "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
        )
        .getReference("/users/")
        .child(userId)

    users.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            val result = snapshot.getValue(User::class.java)
            response.value = Response.success(result)
          } else {
                    response.value = Response.error("No user was found", User())
          }
        }

        override fun onCancelled(error: DatabaseError) {
          response.value = Response.error(error.message, null)
        }
      }
    )

    return response
  }
}
