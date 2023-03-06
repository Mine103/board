package com.kaibeu.board

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.firestore.*
import com.kaibeu.board.model.BoardData
import com.kaibeu.board.model.BoardNumber
import com.kaibeu.board.model.User

class FirebaseManager {

    interface OnCompleteListener {
        fun onSuccess(isSuccess: Boolean)
    }

    interface OnResult<T> {
        fun onSuccess(result: T, isSuccess: Boolean)
    }

    private val TAG = "Board.FirebaseManager"

    private var firestore: FirebaseFirestore? = null
    private val db get() = firestore!!

    @SuppressLint("LongLogTag")
    fun getBoardNumber(listener: OnResult<BoardNumber>) {

        firestore = FirebaseFirestore.getInstance()

        db.collection("boardNumber")
            .document("bno")
            .get()
            .addOnCompleteListener { task ->

                var boardNumber = BoardNumber()

                if(task.isSuccessful) {
                    boardNumber = task.result.toObject(BoardNumber::class.java)!!
                    listener.onSuccess(boardNumber, true)
                } else {
                    task.exception?.message?.let { Log.e("$TAG/getBno/error", it) }
                    listener.onSuccess(boardNumber, false)
                }
            }
    }

    @SuppressLint("LongLogTag")
    fun updateBoardNumber(data: Map<String, Any>, listener: OnCompleteListener) {

        firestore = FirebaseFirestore.getInstance()

        db.collection("boardNumber")
            .document("bno")
            .set(data)
            .addOnSuccessListener {
                listener.onSuccess(true)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.e(TAG, it) }
                listener.onSuccess(false)
            }
    }

    @SuppressLint("LongLogTag")
    fun getAllBoard(listener: OnResult<java.util.ArrayList<BoardData>>) {

        firestore = FirebaseFirestore.getInstance()

        db.collection("board")
            .get()
            .addOnCompleteListener { task->
                val datas = ArrayList<BoardData>()
                if(task.isSuccessful) {
                    for(document in task.result.documents) {
                        document.toObject(BoardData::class.java)?.let { datas.add(it) }
                    }
                    listener.onSuccess(datas, true)
                } else {
                    task.exception?.message?.let { Log.e(TAG, it) }
                    listener.onSuccess(datas, false)
                }
            }
    }

    fun updateBoard(data: Map<String, Any>, listener: OnCompleteListener) {

        firestore = FirebaseFirestore.getInstance()

        db.collection("board")
            .document(data["bno"].toString())
            .set(data)
            .addOnSuccessListener {
                listener.onSuccess(true)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.e(TAG, it) }
                listener.onSuccess(false)
            }
    }

    fun getUser(id: String, listener: OnResult<User>) {

        firestore = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(id)
            .get()
            .addOnCompleteListener { task ->
                var user = User()
                if(task.isSuccessful) {
                    user = task.result.toObject(User::class.java)!!
                    listener.onSuccess(user, true)
                } else {
                    listener.onSuccess(user, false)
                }
            }
    }

    fun updateUser(data: Map<String, Any>, listener: OnCompleteListener) {

        firestore = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(data["id"].toString())
            .set(data)
            .addOnSuccessListener {
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                listener.onSuccess(false)
            }
    }
}