package com.hfad.mycomposeapplication.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hfad.mycomposeapplication.domain.entity.Friend
import com.hfad.mycomposeapplication.domain.repository.AccountRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : AccountRepository {

    override fun listenForUserChanges(): Flow<List<Friend>> = callbackFlow {
        val userCollection = firestore.collection("users")

        val listener = userCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("MY_TAG", "Error listening to Firestore changes", exception)
                close(exception)
                return@addSnapshotListener
            }

            snapshot?.let {
                launch {
                    val currentUser = auth.currentUser?.email?.substringBefore("@") ?: ""
                    val friends = getFriends().map { it.name }.toSet()

                    val list = it.documents.mapNotNull { doc ->
                        val name = doc.getString("name") ?: "Unknown"
                        if (name != currentUser) {
                            val isFriend = friends.contains(name)
                            Friend(name = name, subscription = !isFriend)
                        } else null
                    }
                    trySend(list)
                }
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    private suspend fun getFriends(): List<Friend>{
        val userId = auth.currentUser?.uid ?: ""
        val result = firestore.collection("users").document(userId)
            .collection("friends").get().await()
        val list = mutableListOf<Friend>()

        for (item in result){
            Log.d("MY_TAG", item.id)
            list.add(Friend(name = item.id))
        }
        return list
    }

    override suspend fun addFriend(friend: Friend) {
        val userId = auth.currentUser?.uid ?: ""
        val user = hashMapOf(
            "subscription" to true,
            "name" to friend.name
        )
        firestore.collection("users").document(userId).collection("friends")
            .document(friend.name).set(
                user,
                SetOptions.merge()
            ).await()

    }

    override suspend fun deleteFriend(friend: Friend){
        val userId = auth.currentUser?.uid ?: ""
        firestore.collection("users").document(userId).collection("friends")
            .document(friend.name).delete().await()
    }
}

/*

class AccountRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : AccountRepository {

    override suspend fun getUsers(): List<Friend> {
        val currentUser = auth.currentUser?.email?.substringBefore("@") ?: ""
        val result = firestore.collection("users").get().await()

        val friends = getFriends().map { it.name }.toSet()
        val list = mutableListOf<Friend>()

        for (item in result){
            val name = item.getString("name") ?: "Unknown"
            if (name != currentUser) {

                val isFriend = friends.contains(name)
                list.add(Friend(name = name, subscription = !isFriend))
            }
        }
        return list
    }


    private suspend fun getFriends(): List<Friend>{
        val userId = auth.currentUser?.uid ?: ""
        val result = firestore.collection("users").document(userId)
            .collection("friends").get().await()
        val list = mutableListOf<Friend>()

        for (item in result){
            Log.d("MY_TAG", item.id)
            list.add(Friend(name = item.id))
        }
        return list
    }


    override suspend fun addFriend(friend: Friend) {
        val userId = auth.currentUser?.uid ?: ""
        val user = hashMapOf(
            "subscription" to true,
            "name" to friend.name
        )
        firestore.collection("users").document(userId).collection("friends")
            .document(friend.name).set(
                user,
                SetOptions.merge()
            ).await()

    }

    override suspend fun deleteFriend(friend: Friend){
        val userId = auth.currentUser?.uid ?: ""
        firestore.collection("users").document(userId).collection("friends")
            .document(friend.name).delete().await()
    }
}
 */