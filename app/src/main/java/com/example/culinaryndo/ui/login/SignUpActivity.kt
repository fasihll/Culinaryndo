package com.example.culinaryndo.ui.login

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.culinaryndo.R
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance (this@SignUpActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSignup.setOnClickListener { singup() }
    }

    private fun singup() {
        val firstname = binding.edFirstname.text.toString()
        val lastname = binding.edLastname.text.toString()
        val fullname = "$firstname $lastname"
        val username = binding.edUsername.text.toString()
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()
        val password_verif = binding.edPasswordVerif.text.toString()

        when{
            firstname.isEmpty() -> {
                binding.edLayoutFirstname.error = getString(R.string.warning_firstname_empty)
            }
            firstname.length < 3 -> {
                binding.edLayoutFirstname.error = getString(R.string.warning_3_character)
            }
            username.isEmpty() -> {
                binding.edLayoutUsername.error = getString(R.string.warning_username_empty)
            }
            username.length < 5 -> {
                binding.edLayoutUsername.error = getString(R.string.warning_5_character)
            }
            email.isEmpty() -> {
                binding.edLayoutEmail.error = getString(R.string.warning_email_empty)
            }
            password.isEmpty() -> {
                binding.edLayoutPasswordVerif.error = getString(R.string.warning_password_empty)
            }
            password.length < 8 -> {
                binding.edLayoutPasswordVerif.error = getString(R.string.warning_passsword_minimun)
            }
            !password.equals(password_verif) -> {
                binding.edLayoutPasswordVerif.error = getString(R.string.warning_password_verif)
            }
            else -> {
                viewModel.register(fullname, username, email, password).observe(this){ result ->
                    if (result != null){
                        when(result){
                            is Result.Loading -> {
                                setLoading(true)
                            }
                            is Result.Success -> {
                                Toast.makeText(this,result.data.message,Toast.LENGTH_SHORT).show()
                                if (result.data.status == true){
                                    finish()
                                    setLoading(false)
                                }
                            }
                            is Result.Error -> {
                                setLoading(false)
                                Toast.makeText(this@SignUpActivity,result.error, Toast.LENGTH_SHORT).show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }
}