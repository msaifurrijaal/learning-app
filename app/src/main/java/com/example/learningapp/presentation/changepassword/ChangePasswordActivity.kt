package com.example.learningapp.presentation.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.example.learningapp.R
import com.example.learningapp.databinding.ActivityChangePasswordBinding
import com.example.learningapp.utils.hideSoftKeyboard
import com.example.learningapp.utils.showDialogError
import com.example.learningapp.utils.showDialogLoading
import com.example.learningapp.utils.showDialogSuccess
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.toast

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChangePasswordBinding
    private lateinit var dialogLoading: AlertDialog
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init
        currentUser = FirebaseAuth.getInstance().currentUser
        dialogLoading = showDialogLoading(this)

        onAction()

    }
    private fun onAction() {
        binding.apply {
            btnChangePassword.setOnClickListener {
                val oldPass = etOldPasswordChangePassword.text.toString().trim()
                val newPass = etNewPasswordChangePassword.text.toString().trim()
                val confirmNewPass = etConfirmNewPasswordChangePassword.text.toString().trim()

                if (checkValidation(oldPass, newPass, confirmNewPass)){
                    hideSoftKeyboard(this@ChangePasswordActivity, binding.root)
                    changePasswordToServer(oldPass, newPass)
                }
            }

            btnCloseChangePassword.setOnClickListener { finish() }
        }
    }

    private fun changePasswordToServer(oldPass: String, newPass: String) {
        dialogLoading.show()
        currentUser?.let { mCurrentUser ->
            val credential = EmailAuthProvider.getCredential(mCurrentUser.email.toString(), oldPass)

            mCurrentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    mCurrentUser.updatePassword(newPass)
                        .addOnSuccessListener {
                            dialogLoading.dismiss()
                            val dialogSuccess = showDialogSuccess(this, getString(R.string.success_change_pass))
                            dialogSuccess.show()

                            Handler(Looper.getMainLooper())
                                .postDelayed({
                                    dialogSuccess.dismiss()
                                    finish()
                                }, 1500)
                        }
                        .addOnFailureListener {
                            dialogLoading.dismiss()
                            showDialogError(this, it.message.toString())
                        }
                }
                .addOnFailureListener {
                    dialogLoading.dismiss()
                    showDialogError(this, it.message.toString())
                }
        }
    }

    private fun checkValidation(oldPass: String, newPass: String, confirmNewPass: String): Boolean {
        binding.apply {
            when{
                oldPass.isEmpty() -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_old_password)
                    etOldPasswordChangePassword.requestFocus()
                }
                oldPass.length < 8 -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etOldPasswordChangePassword.requestFocus()
                }
                newPass.isEmpty() -> {
                    etNewPasswordChangePassword.error = getString(R.string.please_field_your_new_password)
                    etNewPasswordChangePassword.requestFocus()
                }
                newPass.length < 8 -> {
                    etNewPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etNewPasswordChangePassword.requestFocus()
                }
                confirmNewPass.isEmpty() -> {
                    etConfirmNewPasswordChangePassword.error = getString(R.string.please_field_your_confirm_new_password)
                    etConfirmNewPasswordChangePassword.requestFocus()
                }
                confirmNewPass.length < 8 -> {
                    etConfirmNewPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etConfirmNewPasswordChangePassword.requestFocus()
                }
                newPass != confirmNewPass -> {
                    etNewPasswordChangePassword.error = getString(R.string.your_password_didnt_match)
                    etNewPasswordChangePassword.requestFocus()
                    etConfirmNewPasswordChangePassword.error = getString(R.string.your_password_didnt_match)
                    etConfirmNewPasswordChangePassword.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}