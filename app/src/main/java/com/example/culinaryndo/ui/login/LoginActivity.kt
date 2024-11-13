package com.example.culinaryndo.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.culinaryndo.R
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.data.model.LoginResult
import com.example.culinaryndo.databinding.ActivityLoginBinding
import com.example.culinaryndo.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance (this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSignIn.setOnClickListener { login() }
        binding.btnSignup.setOnClickListener { startActivity(Intent(this,SignUpActivity::class.java)) }
    }

    private fun login() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        when {
            email.isEmpty() -> {
                binding.signupEmailInputLayout.error = getString(R.string.warning_email_empty)
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.signupEmailInputLayout.error = getString(R.string.warning_email_not_valid)
            }
            password.isEmpty() -> {
                binding.signupPasswordtextInputLayout.error = getString(R.string.warning_password_empty)
            }
            password.length < 8 -> {
                binding.signupPasswordtextInputLayout.error = getString(R.string.warning_passsword_minimun)
            }
            else -> {
               viewModel.login(email, password).observe(this){ result ->
                   if (result != null){
                       when(result){
                           is Result.Loading -> {
                                setLoading(true)
                           }
                           is Result.Success -> {
                               val data = result.data.data
                               viewModel.saveSession(
                                   LoginResult("${data?.fullname}","${data?.id}", "${result.data.token}",true)
                               )
                               setLoading(false)
                               val intent = Intent(this, MainActivity::class.java)
                               Toast.makeText(this@LoginActivity,"Login Success", Toast
                                   .LENGTH_SHORT).show()
                               startActivity(intent)
                               finish()
                           }
                           is Result.Error -> {
                               setLoading(false)
                               Toast.makeText(this@LoginActivity,result.error, Toast.LENGTH_SHORT).show()
                           }
                       }
                   }
               }
            }
        }
    }


    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}