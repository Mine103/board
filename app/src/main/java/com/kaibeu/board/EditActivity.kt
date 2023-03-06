package com.kaibeu.board

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kaibeu.board.databinding.ActivityEditBinding
import org.checkerframework.checker.units.qual.m

class EditActivity : AppCompatActivity() {

    private var mBinding: ActivityEditBinding? = null
    private val binding get() = mBinding!!

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityEditBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val date = intent.getStringExtra("date")
        val creator = intent.getStringExtra("creator")

        binding.title.setText(title)
        binding.content.setText(content)
        binding.info.text = "$creator | $date"

        binding.save.setOnClickListener {
            val data = mutableMapOf<String, Any>()
            data["title"] = binding.title.text.toString()
            data["content"] = binding.content.text.toString()
            data["date"] = date.toString()
            data["creator"] = creator.toString()

            FirebaseManager().updateBoard(data, object : FirebaseManager.OnCompleteListener {
                override fun onSuccess(isSuccess: Boolean) {
                    if(isSuccess) {
                        Intent(this@EditActivity, BoardActivity::class.java).apply {
                            putExtra("bno", intent.getIntExtra("bno", 0))
                            putExtra("title", binding.title.text.toString())
                            putExtra("content", binding.content.text.toString())
                            putExtra("date", "${intent.getStringExtra("date")}")
                            putExtra("creator", "${intent.getStringExtra("creator")}")
                        }.run { startActivity(this) }

                        Util().toast(this@EditActivity, "글이 수정되었습니다.", true)

                        MainActivity().getBoardData()
                        MainActivity().refreshRecycler()

                        finish()
                    } else {
                        val sendIntent = Intent(this@EditActivity, BoardActivity::class.java)

                        sendIntent.putExtra("title", intent.getStringExtra("title"))
                        sendIntent.putExtra("content", intent.getStringExtra("content"))
                        sendIntent.putExtra("date", "${intent.getStringExtra("date")}")
                        sendIntent.putExtra("creator", "${intent.getStringExtra("creator")}")

                        startActivity(sendIntent)

                        Util().toast(this@EditActivity, "글이 수정에 실패했습니다.", true)

                        finish()
                    }
                }
            })
        }

    }

    override fun onDestroy() {
        if(mBinding != null) {
            mBinding = null
        }
        super.onDestroy()
    }
}