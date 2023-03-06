package com.kaibeu.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kaibeu.board.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private var mBinding: ActivitySignupBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignupBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.txtSignUp.setOnClickListener {
            signup()
        }
    }

    override fun onDestroy() {

        if(mBinding != null) mBinding = null

        super.onDestroy()
    }

    private fun signup() {
        if(edtValidation()) {

            val data = mutableMapOf<String, Any>()
            data["id"] = binding.edtUserId.text.toString()
            data["password"] = binding.edtUserPswd.text.toString()
            data["name"] = binding.edtName.text.toString()

            FirebaseManager().updateUser(data, object : FirebaseManager.OnCompleteListener {
                override fun onSuccess(isSuccess: Boolean) {
                    if(isSuccess) {
                        Util().toast(this@SignupActivity, "회원가입이 완료되었습니다.", true)
                        finish()
                    } else {
                        Util().toast(this@SignupActivity, "회원가입에 실패했습니다.", true)
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
        if(binding.edtName.text.isBlank()) {
            Util().toast(this, "이름을 입력해주세요.", true)
            return false
        }
        return true
    }
}