package com.example.culinaryndo.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.culinaryndo.data.model.DataItem
import com.example.culinaryndo.data.model.HistoryItem
import com.example.culinaryndo.databinding.HistoryItemBinding
import com.example.culinaryndo.ui.home.DetailFoodActivity
import com.example.culinaryndo.utils.convertTimeStamp
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter: ListAdapter<HistoryItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bookmark = getItem(position)
        holder.bind(bookmark)
    }

    class MyViewHolder(val binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root) {

        private lateinit var history: HistoryItem
        fun bind(history: HistoryItem){
            this.history = history
            binding.tvFoodName.text = history.foods?.title

            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = format.format(convertTimeStamp(history.createdAt.toString()))
            binding.tvCreatedAt.text = date

            itemView.setOnClickListener{

                val data = DataItem(
                    image = history.foods?.image,
                    createdAt = history.foods?.createdAt,
                    latitude = history.foods?.latitude,
                    description = history.foods?.description,
                    id = history.foods?.id,
                    title = history.foods?.title,
                    longitude = history.foods?.longitude,
                    updatedAt = history.foods?.updatedAt,
                )

                val intent = Intent(itemView.context, DetailFoodActivity::class.java)
                intent.putExtra(DetailFoodActivity.FOODS,data)
                itemView.context.startActivity(intent)
            }
        }

        fun getBookmark(): HistoryItem = history
    }
    companion object{
        val DIFF_CALLBACK= object : DiffUtil.ItemCallback<HistoryItem>(){
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return  oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
