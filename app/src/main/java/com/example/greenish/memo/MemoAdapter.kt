package com.example.greenish.memo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.DetailFragment
import com.example.greenish.Plant
import com.example.greenish.R
import com.example.greenish.databinding.MemoItemBinding

class MemoAdapter : ListAdapter<Memo, MemoAdapter.PlantContentHolder>(MemoDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAdapter.PlantContentHolder {
        return PlantContentHolder(
            MemoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: PlantContentHolder, position: Int) {
        when (holder) {

            is PlantContentHolder -> {
                holder.bind(currentList[position])

            }
            else -> {
                throw Exception("You should not attach here.")
            }
        }


    }

    inner class PlantContentHolder(private val binding: MemoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(memo: Memo) {
            binding.name.text = memo.name
            binding.memo.text = memo.memo
            binding.image.setImageURI(Uri.parse(memo.image))

            binding.year.text = memo.year.toString()
            binding.month.text = memo.month.toString()
            binding.day.text = memo.day.toString()
        }





    }


    object MemoDiffCallback : DiffUtil.ItemCallback<Memo>() {
        override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem == newItem
        }
    }
}
