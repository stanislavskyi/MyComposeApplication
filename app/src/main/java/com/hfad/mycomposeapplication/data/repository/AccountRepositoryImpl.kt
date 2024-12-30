package com.hfad.mycomposeapplication.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hfad.mycomposeapplication.domain.entity.Friend
import com.hfad.mycomposeapplication.domain.repository.AccountRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

                    val friendList = it.documents.mapNotNull { doc ->
                        val name = doc.getString("name") ?: "Unknown"
                        if (name != currentUser) {
                            val isFriend = friends.contains(name)
                            Friend(name = name, subscription = !isFriend)
                        } else null
                    }
                    trySend(friendList)
                }
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    private suspend fun getFriends(): List<Friend>{
        val userId = auth.currentUser?.uid ?: return emptyList()
        return firestore.collection("users")
            .document(userId)
            .collection("friends")
            .get()
            .await()
            .map { Friend(name = it.id) }

    }

    override suspend fun addFriend(friend: Friend) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val user = hashMapOf("subscription" to true, "name" to friend.name)

        firestore.collection("users")
            .document(userId)
            .collection("friends")
            .document(friend.name)
            .set(user, SetOptions.merge())
            .await()
    }

    override suspend fun deleteFriend(friend: Friend){
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        firestore.collection("users")
            .document(userId)
            .collection("friends")
            .document(friend.name)
            .delete()
            .await()
    }
}