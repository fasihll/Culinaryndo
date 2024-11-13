package com.example.culinaryndo.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.databinding.ActivitySearchBinding
import com.example.culinaryndo.ui.home.FoodsListAdapter
import com.example.culinaryndo.ui.home.HomeViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<HomeViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var keyword = viewModel.keyword.value
        if (keyword == null){
            keyword = intent.getStringExtra(KEYWORD).toString()
        }

        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rvFoods.layoutManager = layoutManager

        binding.btnBack.setOnClickListener { finish() }

        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView
            .editText
            .setOnEditorActionListener{textView, actionId, event ->
                binding.searchBar.setText(binding.searchView.text)
                setData(binding.searchView.text.toString())
                binding.searchView.hide()
                false
            }


        Log.d(KEYWORD,"$keyword")
        setData(keyword)
        binding.searchBar.setText(keyword)


    }

    fun setData(keyword: String){
        viewModel.getFoodByKeyword(keyword).observe(this){result ->
            if (result != null){
                when(result){
                    is Result.Loading -> {
                        setLoading(true)
                    }
                    is Result.Success -> {
                        val adapter = FoodsListAdapter()
                        adapter.submitList(result.data.data)
                        binding.rvFoods.adapter = adapter
                        setLoading(false)
                    }
                    is Result.Error -> {
                        setLoading(false)
                        Toast.makeText(this,result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val KEYWORD = "keyword"
    }
}