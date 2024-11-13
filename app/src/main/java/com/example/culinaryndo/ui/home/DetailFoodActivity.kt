package com.example.culinaryndo.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.culinaryndo.R
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.data.model.DataItem
import com.example.culinaryndo.databinding.ActivityDetailFoodBinding
import com.example.culinaryndo.ui.main.MainActivity
import com.example.culinaryndo.ui.maps.MapsActivity
import com.example.culinaryndo.ui.scan.ScanActivity.Companion.FROM_SCAN
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class DetailFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFoodBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var foodName: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<DataItem>(FOODS) as DataItem

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnBack.setOnClickListener {
            if (intent.getStringExtra(FROM_SCAN) != null){
                startActivity(Intent(this@DetailFoodActivity,MainActivity::class.java))
            }else{
                finish()
            }
        }

        Log.d("RES_ID", data.id.toString())

        viewModel.getFoodById(data.id!!).observe(this){
            if (it != null){
                when(it){
                    is Result.Loading -> {
                        setLoading(true)
                    }
                    is Result.Success -> {
                        val result = it.data.data
                        if (result != null){
                            Glide.with(this)
                                .load(result.image)
                                .into(binding.ivImageFood)

                            binding.tvFoodName.text = result.title
                            foodName = result.title.toString()

                            binding.tvDescription.text = result.description
                            setLoading(false)
                        }
                    }
                    is Result.Error -> {
                        setLoading(false)
                        Toast.makeText(this@DetailFoodActivity,it.error,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.getSession().observe(this){user ->
            viewModel.getSpesificBookmark(user.userId,data.id.toString()).observe(this){ bookmark ->
                if (bookmark != null){
                    when(bookmark){
                        is Result.Loading -> {
                            binding.btnBookmark.text = "Loading.."
                        }
                        is Result.Success -> {
                            binding.btnBookmark.text = getString(R.string.btn_remove_bookmark)
                            viewModel.isBookmarked.value = true
                            viewModel.bookmarkId.value = bookmark.data.bookmarkId.toString()
                        }
                        is Result.Error -> {
                            binding.btnBookmark.text = getString(R.string.btn_bookmark_now)
                            viewModel.isBookmarked.value = false
                        }
                    }
                }else{
                    Toast.makeText(this@DetailFoodActivity,"kosong",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBookmark.setOnClickListener { bookmarkButton(data.id.toString()) }
        binding.btnMaps.setOnClickListener{
            getMyLastLocation()
        }

    }

    private fun bookmarkButton(foodId: String) {
        viewModel.getSession().observe(this){
            if (viewModel.isBookmarked.value == true){
                deleteBookmark(it.userId,viewModel.bookmarkId.value!!)
            }else{
                addbookmark(it.userId,foodId)
            }
        }
    }

    private fun addbookmark(userId: String, foodId: String) {
        viewModel.addbookmark(userId, foodId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.btnBookmark.text = "Loading..."
                }

                is Result.Success -> {
                    viewModel.isBookmarked.value = true
                    binding.btnBookmark.text = getString(R.string.btn_remove_bookmark)
                    viewModel.bookmarkId.value = result.data.data?.id.toString()
                }
                is Result.Error -> {
                    Toast.makeText(this@DetailFoodActivity, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteBookmark(userId: String, bookmarkId: String) {
        viewModel.deleteBookmark(userId, bookmarkId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.btnBookmark.text = "Loading..."
                }

                is Result.Success -> {
                    viewModel.isBookmarked.value = false
                    binding.btnBookmark.text = getString(R.string.btn_bookmark_now)
                }

                is Result.Error -> {
                    Toast.makeText(this@DetailFoodActivity, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {}
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
//                    Log.d("MYLOCATION_DETAIL",location.toString())
                    val intent = Intent(this@DetailFoodActivity,MapsActivity::class.java)
                    intent.putExtra(MapsActivity.FOOD_NAME,foodName)
                    intent.putExtra(MapsActivity.MY_LOCATION_LATITUDE,location.latitude)
                    intent.putExtra(MapsActivity.MY_LOCATION_LONGTITUDE,location.longitude)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@DetailFoodActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val FOODS = "foods"
    }
}