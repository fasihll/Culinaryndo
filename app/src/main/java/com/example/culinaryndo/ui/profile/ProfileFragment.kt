package com.example.culinaryndo.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.culinaryndo.R
import com.example.culinaryndo.ViewModelFactory
import com.example.culinaryndo.data.Result
import com.example.culinaryndo.databinding.FragmentProfileBinding
import com.example.culinaryndo.ui.profile.setting.SettingActivity
import com.example.culinaryndo.ui.profile.term.TermsActivity

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSetting.setOnClickListener(this@ProfileFragment)
            btnTerms.setOnClickListener(this@ProfileFragment)
            btnLogout.setOnClickListener(this@ProfileFragment)
        }

       viewModel.getSession().observe(viewLifecycleOwner){
           if (it.userId.isNotEmpty()){
               viewModel.getUserData(it.userId).observe(viewLifecycleOwner){result ->
                   if (result != null){
                       when(result){
                           is Result.Loading -> {
                               setLoading(true)
                           }
                           is Result.Success -> {
                               val data = result.data.data
                               if (data != null){
                                   Glide.with(requireContext())
                                       .load(data.urlImage)
                                       .placeholder(R.drawable.frame4)
                                       .into(binding.profileImage)

                                   binding.tvUsername.text = data.username
                                   binding.tvEmail.text = data.email
                               }else{
                                   Toast.makeText(requireContext(),result.data.message,Toast.LENGTH_SHORT)
                                       .show()
                               }
                               setLoading(false)
                           }
                           is Result.Error -> {
                               setLoading(false)
                               Toast.makeText(requireContext(),result.error,Toast.LENGTH_SHORT).show()
                           }
                       }
                   }
               }

               viewModel.getUserBookmark(it.userId).observe(viewLifecycleOwner){ result ->
                   if (result != null) {
                       when (result) {
                           is Result.Loading -> {
                               setLoading(true)
                           }

                           is Result.Success -> {
                               val data = result.data.data
                               val bookmarkCount = data?.size ?: 0
                               binding.tvBookmarkCount.text = bookmarkCount.toString()
                               setLoading(false)
                           }

                           is Result.Error -> {
                               setLoading(false)
                               Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                           }
                       }
                   }
               }

               viewModel.getUserHisotry(it.userId).observe(viewLifecycleOwner){ result ->
                   if (result != null) {
                       when (result) {
                           is Result.Loading -> {
                               setLoading(true)
                           }

                           is Result.Success -> {
                               val data = result.data.data
                               val historyCount = data?.size ?: 0
                               binding.tvHistoryCount.text = historyCount.toString()
                               setLoading(false)
                           }

                           is Result.Error -> {
                               setLoading(false)
                               Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                           }
                       }
                   }
               }
           }else{
               Log.d("ProfileFragemnt","Session is Empty")
           }
       }



    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_setting -> {
                startActivity(Intent(requireActivity(),SettingActivity::class.java))
            }
            R.id.btn_terms -> {
                startActivity(Intent(requireActivity(),TermsActivity::class.java))
            }
            R.id.btn_logout -> {
                viewModel.logout()
                requireActivity().finish()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}