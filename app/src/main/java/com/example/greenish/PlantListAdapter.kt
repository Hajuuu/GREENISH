package com.example.greenish

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.databinding.ListItemBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class PlantListAdapter : ListAdapter<Plant, PlantListAdapter.PlantContentHolder>(PlantDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantListAdapter.PlantContentHolder {
        return PlantContentHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    inner class PlantContentHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val pos = bindingAdapterPosition
        private val context = binding.root.context

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(plant: Plant) {
            binding.nameTxt.text = plant.name
            binding.photo.setImageURI(Uri.parse(plant.image))

            binding.place.text = plant.place.toString()

            binding.root.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    val activity = v!!.context as AppCompatActivity
                    val detailFragment = DetailFragment()
                    val bundle = Bundle()
                    bundle.putString("name", plant.name)
                    bundle.putString("image", plant.image)
                    bundle.putString("place", plant.place)
                    bundle.putString("date", plant.date)
                    bundle.putString("water", plant.water)
                    bundle.putString("waterCycle", plant.waterCycle)
                    detailFragment.arguments = bundle
                    activity.supportFragmentManager.beginTransaction()
                        .add(R.id.main_nav_host, detailFragment).addToBackStack(null).commit()


                }
            })

            binding.wateringBtn.setOnClickListener {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                val formatted = current.format(formatter)
                plant.water = formatted.toString()
            }

        }
    }


    object PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {
        override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem == newItem
        }
    }
}
