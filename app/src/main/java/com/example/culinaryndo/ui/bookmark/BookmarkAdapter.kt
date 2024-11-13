package com.example.culinaryndo.ui.bookmark

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.culinaryndo.data.model.BookmarkItem
import com.example.culinaryndo.data.model.DataItem
import com.example.culinaryndo.databinding.BookmarkItemBinding
import com.example.culinaryndo.ui.home.DetailFoodActivity

class BookmarkAdapter: ListAdapter<BookmarkItem, BookmarkAdapter.MyViewHolder>(DIFF_CALLBACK)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BookmarkItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bookmark = getItem(position)
        holder.bind(bookmark)
    }

    class MyViewHolder(val binding: BookmarkItemBinding): RecyclerView.ViewHolder(binding.root) {

        private lateinit var bookmark: BookmarkItem
        fun bind(bookmark: BookmarkItem){
            this.bookmark = bookmark
            Glide.with(itemView.context)
                .load(bookmark.foods?.image)
                .into(binding.ivImageFood)

            binding.tvFoodName.text = bookmark.foods?.title
            binding.tvDescription.text = bookmark.foods?.description

            itemView.setOnClickListener{

                val data = DataItem(
                    image = bookmark.foods?.image,
                    createdAt = bookmark.foods?.createdAt,
                    latitude = bookmark.foods?.latitude,
                    description = bookmark.foods?.description,
                    id = bookmark.foods?.id,
                    title = bookmark.foods?.title,
                    longitude = bookmark.foods?.longitude,
                    updatedAt = bookmark.foods?.updatedAt,
                )

                val intent = Intent(itemView.context, DetailFoodActivity::class.java)
                intent.putExtra(DetailFoodActivity.FOODS,data)
                itemView.context.startActivity(intent)
            }
        }

        fun getBookmark(): BookmarkItem = bookmark
    }
    companion object{
        val DIFF_CALLBACK= object : DiffUtil.ItemCallback<BookmarkItem>(){
            override fun areItemsTheSame(oldItem: BookmarkItem, newItem: BookmarkItem): Boolean {
                return  oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: BookmarkItem, newItem: BookmarkItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
