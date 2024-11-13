package com.example.culinaryndo.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel by viewModels<HistoryViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHisotry.layoutManager = layoutManager
        binding.rvHisotry.addItemDecoration(DividerItemDecoration(requireContext(),layoutManager.orientation))

        setHisotoryData()
        val adapter = HistoryAdapter()

        adapter.notifyDataSetChanged()

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val history = (viewHolder as HistoryAdapter.MyViewHolder).getBookmark()
                viewModel.getSession().observe(viewLifecycleOwner){
                    viewModel.deleteHistory(it.userId,history.historyId.toString())
                        .observe(viewLifecycleOwner){ res ->
                            if (res != null){
                                when(res){
                                    is Result.Loading -> {}
                                    is Result.Success -> {
                                        Toast.makeText(requireContext(),res.data.message, Toast
                                            .LENGTH_SHORT).show()
                                    }
                                    is Result.Error -> {
                                        Toast.makeText(requireContext(),res.error, Toast
                                            .LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                }
            }

        }).attachToRecyclerView(binding.rvHisotry)
    }

    private fun setHisotoryData() {
        viewModel.getSession().observe(requireActivity()){
            if (it.isLogin){
                viewModel.getHistoryByUser(it.userId).observe(requireActivity()){ result ->
                    if (result != null){
                        when(result){
                            is Result.Loading -> {
                                setLoading(true)
                            }
                            is Result.Success -> {
                                val adapter = HistoryAdapter()
                                adapter.submitList(result.data.data)
                                binding.rvHisotry.adapter = adapter
                                setLoading(false)
                            }
                            is Result.Error -> {
                                setLoading(false)
                                Toast.makeText(requireContext(),result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Session Not Found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}