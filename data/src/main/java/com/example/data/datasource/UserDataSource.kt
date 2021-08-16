package com.example.data.datasource

import com.example.data.interfaces.UserInterface
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.UserEntity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UserDataSource @Inject constructor(private val database: ExpensareDatabase) : UserInterface {

    override suspend fun create(user: UserEntity) {
        database.userDao().createUser(user)
    }

    override suspend fun getAll(): ArrayList<UserEntity> {
        return database.userDao().getAllUsers() as ArrayList<UserEntity>
    }

    override suspend fun downloadUser(): UserEntity  {
        var userId = ""
        if (FirebaseAuth.getInstance().uid != null) {
            userId = FirebaseAuth.getInstance().uid.toString()
        } else {
            userId = ""
        }
        val users =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                )
                .getReference("/users/").child(userId)
        val user = users.get()
        Tasks.await(user)

        return user.result?.getValue(UserEntity::class.java) ?: UserEntity.EMPTY

    }
}
