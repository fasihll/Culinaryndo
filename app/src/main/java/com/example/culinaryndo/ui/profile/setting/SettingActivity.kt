package com.example.culinaryndo.ui.profile.setting

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.culinaryndo.R
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.databinding.ActivitySettingBinding
import com.example.culinaryndo.utils.uriToFile

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<SettingViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnEditImage.setOnClickListener{
            startGalery()
        }
        
        viewModel.getSession().observe(this){
            viewModel.getUserData(it.userId).observe(this){ result ->
                if (result != null){
                    when(result){
                        is Result.Loading -> {
                            setLoading(true)
                        }
                        is Result.Success -> {
                            val data = result.data.data
                            if (data != null){
                                if (data.urlImage != null){
                                    Glide.with(this)
                                        .load(data.urlImage)
                                        .into(binding.profileImage)
                                }

                                binding.edFullname.setText(data.fullname ?: "")
                                binding.edUsername.setText(data.username ?: "")
                                binding.edEmail.setText(data.email ?: "")
                            }
                            setLoading(false)
                        }
                        is Result.Error -> {
                            setLoading(false)
                            Toast.makeText(this@SettingActivity,result.error,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.btnUpdate.setOnClickListener {

                val fullname = binding.edFullname.text.toString()
                val username = binding.edUsername.text.toString()
                val email = binding.edEmail.text.toString()
                val oldPassword = binding.edOldPassword.text.toString()
                val newPassword = binding.edNewPassword.text.toString()
                when{
                    fullname.isEmpty() -> {
                        binding.edFullnameLayout.error = "Fullname is Empty"
                    }
                    username.isEmpty() -> {
                        binding.edLayoutUsername.error = "Username is Empty"
                    }
                    email.isEmpty() -> {
                        binding.edLayoutEmail.error = "Email is Empty"
                    }
                    else -> {
                        val imageFile = currentImageUri?.let {  uriToFile(it,this) }

                        viewModel.getSession().observe(this){
                                viewModel.updateProfile(
                                    id = it.userId,
                                    file =  imageFile,
                                    email = email,
                                    fullname = fullname,
                                    username = username,
                                    oldPassword = oldPassword,
                                    newPassword = newPassword
                                ).observe(this){res ->
                                    if (res != null){
                                        when(res){
                                            is Result.Loading -> {
                                                setLoading(true)
                                            }
                                            is Result.Success -> {
                                                Toast.makeText(this@SettingActivity,res.data.message,Toast
                                                    .LENGTH_SHORT).show()
                                                finish()
                                                setLoading(false)
                                            }
                                            is Result.Error -> {
                                                setLoading(false)
                                                Toast.makeText(this@SettingActivity,res.error,Toast
                                                    .LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                }

                        }
                    }

            }
        }
    }

    private fun startGalery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this,"No media selected",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.profileImage.setImageURI(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}