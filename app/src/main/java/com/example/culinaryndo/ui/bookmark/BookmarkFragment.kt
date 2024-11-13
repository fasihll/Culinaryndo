package com.example.culinaryndo.ui.bookmark

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.databinding.FragmentBookmarkBinding


class BookmarkFragment : Fragment(){

    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel by viewModels<BookmarkViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.rvBookmark.layoutManager = layoutManager

        setBookmarkData()
        val adapter = BookmarkAdapter()

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
               val bookamrk = (viewHolder as BookmarkAdapter.MyViewHolder).getBookmark()
                viewModel.getSession().observe(viewLifecycleOwner){
                    viewModel.deleteBookmark(it.userId,bookamrk.bookmarkId.toString())
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

        }).attachToRecyclerView(binding.rvBookmark)
    }

    private fun setBookmarkData() {
        viewModel.getSession().observe(requireActivity()){
            if (it.isLogin){
                viewModel.getBookmarkByUser(it.userId).observe(requireActivity()){ result ->
                    if (result != null){
                        when(result){
                            is Result.Loading -> {
                                setLoading(true)
                            }
                            is Result.Success -> {
                                val adapter = BookmarkAdapter()
                                adapter.submitList(result.data.data)
                                binding.rvBookmark.adapter = adapter
                                setLoading(false)
                            }
                            is Result.Error -> {
                                setLoading(false)
                                Toast.makeText(requireContext(),result.error,Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Session Not Found",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}