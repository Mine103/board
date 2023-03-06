package com.kaibeu.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.kaibeu.board.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    var mBinding: ActivityBoardBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val creator = intent.getStringExtra("creator")
        val date = intent.getStringExtra("date")

        val info = "$creator | $date"

        binding.title.text = title
        binding.content.text = content
        binding.info.text = info
    }

    override fun onDestroy() {
        if(mBinding != null) {
            mBinding = null
        }
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.board_meun, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.edit -> {

                Util().toast(this, "edit", true)

                Intent(this@BoardActivity, EditActivity::class.java).apply {
                    putExtra("bno", intent.getIntExtra("bno", 0))
                    putExtra("title", intent.getStringExtra("title"))
                    putExtra("content", intent.getStringExtra("content"))
                    putExtra("date", "${intent.getStringExtra("date")}")
                    putExtra("creator", "${intent.getStringExtra("creator")}")
                }.run { startActivity(this) }

                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}