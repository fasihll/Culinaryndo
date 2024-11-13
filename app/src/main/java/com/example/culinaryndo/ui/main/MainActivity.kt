package com.example.culinaryndo.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.culinaryndo.R
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.component.LoadingDialog
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.databinding.ActivityMainBinding
import com.example.culinaryndo.ui.home.DetailFoodActivity
import com.example.culinaryndo.ui.home.DetailFoodActivity.Companion.FOODS
import com.example.culinaryndo.ui.login.LoginActivity
import com.example.culinaryndo.ui.scan.ScanActivity
import com.example.culinaryndo.ui.scan.ScanActivity.Companion.CAMERAX_RESULT
import com.example.culinaryndo.ui.scan.ScanActivity.Companion.FROM_SCAN
import com.example.culinaryndo.utils.uriToFile
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private lateinit var loadingDialog: LoadingDialog

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

//        startCameraX()


        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //cek session
        viewModel.getSession().observe(this){ session ->
            if(session.isLogin == false){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        loadingDialog = LoadingDialog(this@MainActivity)

        binding.apply {
            navView.background = null
            navView.menu.getItem(2).isEnabled = false

            btnScan.setOnClickListener{
               startCameraX()
            }
        }

        setBottomNavigationView()
    }

    private fun startCameraX() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }else{
            launcherIntentCameraX.launch(Intent(this@MainActivity,ScanActivity::class.java))
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(ScanActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            val foodName =  it.data?.getStringExtra(ScanActivity.FOOD_NAME)

            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri,this)
                Log.d("Result Akhir",foodName.toString())

                viewModel.scanFood(foodName.toString()).observe(this@MainActivity){ result ->
                    if (result != null){
                        when(result){
                            is Result.Loading -> {
                                loadingDialog.startLoadingDialog(uri)
                            }
                            is Result.Success -> {
                                CoroutineScope(Dispatchers.Main).launch {

                                    delay(1000) // Delay in milliseconds (1000 ms = 1 second)

                                    loadingDialog.dismisDialog()

                                    viewModel.getSession().observe(this@MainActivity){
                                        if (it != null){
                                            viewModel.addHistory(it.userId,result.data.data?.first()?.id
                                                .toString()).observe(this@MainActivity,{})
                                        }else{
                                            Toast.makeText(this@MainActivity,"Session is Empty", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }

                                    val intent = Intent(this@MainActivity, DetailFoodActivity::class.java)
                                    intent.putExtra(FOODS,result.data.data?.first())
                                    intent.putExtra(FROM_SCAN,"scanActivity")
                                    startActivity(intent)
                                }

                            }
                            is Result.Error -> {
                                loadingDialog.dismisDialog()
                                Toast.makeText(this,result.error,Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    fun setBottomNavigationView(){
        setSupportActionBar(binding.bottomAppBar)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment,
            R.id.bookmarkFragment,
            R.id.none,
            R.id.historyFragment,
            R.id.profileFragment
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}