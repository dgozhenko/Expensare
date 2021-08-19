package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.ListInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.models.ListItem
import com.example.domain.models.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class ListDataSource
@Inject
constructor(private val database: ExpensareDatabase, private val storage: Storage) : ListInterface {

    override suspend fun create(listItem: ListItem): LiveData<Response<String>> {
        val response = MutableLiveData<Response<String>>()
        response.value = Response.loading(null)

        val groupId = storage.groupId
        val list =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("grocery_list/$groupId")
                .push()

        list.setValue(listItem)
            .addOnSuccessListener { response.value = Response.success("List Item created") }
            .addOnFailureListener { response.value = Response.error(it.message!!, null) }
            .addOnCanceledListener { response.value = Response.error("Item creation canceled", null) }
        return response
    }

    override suspend fun getAll(): LiveData<Response<ArrayList<ListItem>>> {
        val response = MutableLiveData<Response<ArrayList<ListItem>>>()
        response.value = Response.loading(null)
        val groupId = storage.groupId
        val groceryListArray = arrayListOf<ListItem>()
        val list =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/grocery_list/$groupId/")
        list.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val groceryListItem = it.getValue(ListItem::class.java)
                            if (groceryListItem != null) {
                                groceryListArray.add(groceryListItem)
                            } else {
                                response.value = Response.error("Item error", null)
                            }
                        }
                        response.value = Response.success(groceryListArray)
                    } else {
                        response.value = Response.error("There no list", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = Response.error(error.message, null)
                }
            })
        return response
    }

    override suspend fun delete(listItem: ListItem): LiveData<Response<String>> {
        val response = MutableLiveData<Response<String>>()
        response.value = Response.loading(null)
        val groupId = storage.groupId
        val list =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/grocery_list/$groupId/")
        val deleteReference =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/grocery_list/$groupId/")
        list.addListenerForSingleValueEvent(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val item = it.getValue(ListItem::class.java)
                            if (item == listItem) {
                                val key = it.key
                                deleteReference
                                    .child("${key!!}/")
                                    .removeValue()
                                    .addOnSuccessListener {
                                        response.value = Response.success("Item deleted")
                                    }
                                    .addOnFailureListener {
                                        response.value = Response.error(it.message!!, null)
                                    }
                                    .addOnCanceledListener {
                                        response.value = Response.error("Delete canceled", null)
                                    }
                            }
                        }
                    } else {
                        response.value = Response.error("There no such item", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = Response.error(error.message, null)
                }
            })
        return response
    }
}
