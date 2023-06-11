package com.example.greenish

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenish.databinding.ItemListBinding
import com.example.greenish.retrofit.Item
import com.example.greenish.retrofit.PlantResult
import com.google.android.material.internal.ContextUtils.getActivity

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.MyView>(), Filterable {
    private var plantList = ArrayList<Item>()
    private var filteredPlant = ArrayList<Item>()
    private var itemFilter = ItemFilter()
    inner class MyView(private val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            var images = filteredPlant[pos].cntntsNo
            var name = filteredPlant[pos].cntntsSj
            binding.name.text = name
            Glide.with(binding.image.context)
                .load("http://www.nongsaro.go.kr/cms_contents/301/"+images+"_MF_ATTACH_01_TMB.jpg")
                .into(binding.image)

            binding.root.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    val activity=v!!.context as AppCompatActivity
                    val infoFragment = InfoFragment()
                    val bundle = Bundle()
                    bundle.putString("id", images)
                    bundle.putString("name", name)
                    infoFragment.arguments = bundle
                    activity.supportFragmentManager.beginTransaction().add(R.id.main_nav_host, infoFragment).addToBackStack(null).commit()

                }
            })
        }


    }

    init {
        filteredPlant.addAll(plantList)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val view = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return filteredPlant.size
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    fun setList(list: ArrayList<Item>) {
        plantList = list
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()
            Log.d(TAG, "charSequence : $charSequence")
            val filteredList: ArrayList<Item> = ArrayList<Item>()
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = plantList
                results.count = plantList.size

                return results
                //공백제외 2글자 이인 경우 -> 이름으로만 검색
            }else{
                for (plant in plantList) {
                    if (plant.cntntsSj.contains(filterString)) {
                        filteredList.add(plant)
                    }
                }
            }


            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            filteredPlant.clear()
            filteredPlant.addAll(filterResults.values as ArrayList<Item>)
            notifyDataSetChanged()
        }
    }


}