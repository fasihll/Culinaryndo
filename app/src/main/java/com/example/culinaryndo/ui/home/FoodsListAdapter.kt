package com.example.culinaryndo.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.culinaryndo.data.model.DataItem
import com.example.culinaryndo.databinding.FoodsItemBinding
import com.example.culinaryndo.ui.home.DetailFoodActivity.Companion.FOODS

class FoodsListAdapter: ListAdapter<DataItem, FoodsListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FoodsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val users = getItem(position)
        holder.bind(users)
    }

    class MyViewHolder(val binding: FoodsItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(foods: DataItem){
            binding.apply {
                Glide.with(itemView.context)
                    .load(foods.image)
                    .into(image)

                name.text = foods.title

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context,DetailFoodActivity::class.java)
                    intent.putExtra(FOODS,foods)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    companion object{
        val DIFF_CALLBACK= object : DiffUtil.ItemCallback<DataItem>(){
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return  oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}