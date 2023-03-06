package com.kaibeu.board

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kaibeu.board.databinding.ActivityLoginBinding
import com.kaibeu.board.model.User

class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!

    private lateinit var pref: SharedPreferences
    private lateinit var editor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        pref = getSharedPreferences("login", MODE_PRIVATE)
        editor = pref.edit()

        Log.i("LOGIN SESSION", "${pref.getBoolean("session", false)} | ${pref.getString("email", null)}")

        if(pref.getBoolean("session", false)) {
            pref.getString("email", "")
                ?.let { FirebaseManager().getUser(it, object : FirebaseManager.OnResult<User> {
                    override fun onSuccess(result: User, isSuccess: Boolean) {
                        DataManager.user = result
                        Log.i("result", "$result")
                        Log.i("DataManager.user", "${DataManager.user}")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }) }
        }

        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.txtLogin.setOnClickListener {
            login()
        }
    }

    override fun onDestroy() {

        if(mBinding != null) mBinding = null

        super.onDestroy()
    }

    private fun login() {
        if(edtValidation()) {
            val email = binding.edtUserId.text.toString()
            FirebaseManager().getUser(email, object : FirebaseManager.OnResult<User> {
                override fun onSuccess(result: User, isSuccess: Boolean) {
                    if(isSuccess) {
                        DataManager.user = result
                        Log.i("result", "$result")
                        Log.i("DataManager.user", "${DataManager.user}")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)

                        editor.putBoolean("session", true)
                        editor.putString("email", email)
                        editor.commit()

                        Log.i("LOGIN SESSION SAVE", "${pref.getBoolean("session", false)} | ${pref.getString("email", "")}")

                        finish()
                    } else {
                        Util().toast(this@LoginActivity, "계정이 존재하지 않습니다.", true)
                    }
                }
            })
        }
    }

    private fun edtValidation(): Boolean {
        if(binding.edtUserId.text.isBlank()) {
            Util().toast(this, "아이디를 입력해주세요.", true)
            return false
        }
        if(binding.edtUserPswd.text.isBlank()) {
            Util().toast(this, "비밀번호를 입력해주세요.", true)
            return false
        }
        return true
    }
}
