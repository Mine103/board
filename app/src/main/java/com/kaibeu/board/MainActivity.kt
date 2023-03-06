package com.kaibeu.board

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kaibeu.board.databinding.ActivityMainBinding
import com.kaibeu.board.model.BoardData
import com.kaibeu.board.model.BoardNumber
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    lateinit var boardAdapter: BoardAdapter
    private var datas = mutableListOf<BoardData>()

    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getBoardData()
        initRecycler()

        pref = getSharedPreferences("login", MODE_PRIVATE)

        binding.write.setOnClickListener {
            write()
        }
    }

    override fun onDestroy() {
        if(mBinding != null) {
            mBinding = null
        }

        Log.i("Main LOGIN SESSION", "${pref.getBoolean("session", false)} | ${pref.getString("email", "")}")

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meun, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.refresh -> {
                getBoardData()
                refreshRecycler()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toast(msg: String, short: Boolean) {
        if(short) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun getBoardData() {
        FirebaseManager().getAllBoard(object : FirebaseManager.OnResult<ArrayList<BoardData>> {
            override fun onSuccess(result: ArrayList<BoardData>, isSuccess: Boolean) {
                if (isSuccess) {
                    datas.clear()
                    for(document in result) {
                        datas.apply {
                            add(document)
                        }
                    }
                    refreshRecycler()
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        boardAdapter = BoardAdapter(this)
        binding.board.adapter = boardAdapter

        refreshRecycler()

        boardAdapter.setOnItemClickListener(object : BoardAdapter.OnClickListener {
            override fun onItemClick(v: View, data: BoardData, pos: Int) {
                Intent(this@MainActivity, BoardActivity::class.java).apply {
                    putExtra("bno", data.bno)
                    putExtra("title", data.title)
                    putExtra("content", data.content)
                    putExtra("creator", data.creator)
                    putExtra("date", data.date)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshRecycler() {
        boardAdapter.datas = datas
        boardAdapter.notifyDataSetChanged()
    }

    @SuppressLint("InflateParams", "NotifyDataSetChanged", "SimpleDateFormat")
    private fun write() {
        val dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_write, null, false)

        val edtTitle = view.findViewById<EditText>(R.id.edtTitle)
        val edtContent = view.findViewById<EditText>(R.id.edtContent)

        dialog.setPositiveButton("닫기", null)
        dialog.setNegativeButton("글 저장") { dialogInterface: DialogInterface, i: Int ->

            var bno = 0

            FirebaseManager().getBoardNumber(object : FirebaseManager.OnResult<BoardNumber> {
                override fun onSuccess(result: BoardNumber, isSuccess: Boolean) {
                    if(isSuccess) {
                        bno = (result.bno + 1)
                    } else {
                        toast("글 작성에 실패했습니다.", false)
                        return
                    }
                }
            })

            Log.i("write.bno", bno.toString())

            val bnoMap = mutableMapOf<String, Any>()
            bnoMap["bno"] = bno

            FirebaseManager().updateBoardNumber(bnoMap, object : FirebaseManager.OnCompleteListener {
                override fun onSuccess(isSuccess: Boolean) {
                    if(isSuccess) {

                        val data = mutableMapOf<String, Any>()
                        data["bno"] = bno
                        data["title"] = edtTitle.text.toString()
                        data["content"] = edtContent.text.toString()
                        data["creator"] = DataManager.user.name
                        val format = SimpleDateFormat("yyyy/MM/dd")
                        val date = Date()
                        data["date"] = format.format(date)

                        FirebaseManager().updateBoard(data, object : FirebaseManager.OnCompleteListener {
                            override fun onSuccess(isSuccess: Boolean) {
                                if(isSuccess) {
                                    getBoardData()
                                    refreshRecycler()
                                    toast("글이 작성되었습니다.", true)
                                } else {
                                    toast("글 작성에 실패했습니다.", false)
                                }
                            }
                        })

                    } else {
                        toast("글 작성에 실패했습니다.", false)
                        return
                    }
                }
            })
        }
        dialog.setView(view)
        dialog.show()
    }
}