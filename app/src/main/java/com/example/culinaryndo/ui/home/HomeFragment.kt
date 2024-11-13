package com.example.culinaryndo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.culinaryndo.R
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.databinding.FragmentHomeBinding
import com.example.culinaryndo.ui.search.SearchActivity

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
       binding = FragmentHomeBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            searchBar.setOnClickListener(this@HomeFragment)
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener{textView, actionId, event ->
                    val intent = Intent(requireContext(), SearchActivity::class.java)
                    intent.putExtra(SearchActivity.KEYWORD,searchView.text.toString())
                    startActivity(intent)
                    searchView.hide()
                    false
                }

            val layoutManager = GridLayoutManager(requireContext(),2,)
            rvFoods.layoutManager = layoutManager
        }


        setWelcomeText()
        setFoodData()
    }

    private fun setFoodData() {
        viewModel.getAllFoods().observe(viewLifecycleOwner){result ->
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
                        Toast.makeText(requireContext(),result.error,Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Log.d("SESSION","Session not Found")
                Toast.makeText(requireContext(),"Tidak ada data", Toast
                    .LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setWelcomeText() {
        viewModel.getSession().observe(viewLifecycleOwner){ session ->
            if (session.userId.isNotEmpty()){
                viewModel.getUserById(session.userId).observe(viewLifecycleOwner){ result->
                    if (result != null){
                        when(result){
                            is Result.Loading ->{
                                binding.welcome.text  = getString(R.string.welcome,"Loading...")
                            }
                            is Result.Success ->{
                                val firstname = result.data.data?.fullname?.split(" ")?.firstOrNull()
                                binding.welcome.text  = getString(R.string.welcome,"${firstname}")
                            }
                            is Result.Error -> {
                                Toast.makeText(requireContext(),result.error, Toast
                                    .LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }else{
                Log.d("SESSION","Session not Found")
                Toast.makeText(requireContext(),"Tidak ada session", Toast
                    .LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onClick(v: View) {
        when(v.id){

        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}