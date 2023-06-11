package com.example.greenish

import android.app.ActionBar
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.databinding.FragmentSearchBinding
import com.example.greenish.retrofit.Item
import com.example.greenish.retrofit.PlantResult
import com.example.greenish.retrofit.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchAdapter:SearchAdapter
    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter = SearchAdapter()
        val fragmentSearchBinding = FragmentSearchBinding.bind(view)
        binding = fragmentSearchBinding


        val key = "api key"

        binding.searchView.setOnQueryTextListener(searchViewTextListener)

        binding.recyclerview.apply{
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        var call : Call<PlantResult> = Retrofit.getRetrofit().plantResult(key, "217")

        call.enqueue(object : Callback<PlantResult> {
            override fun onResponse(call: Call<PlantResult>, response: Response<PlantResult>) {
                val plantResult: PlantResult? = response.body()
                val itemList = plantResult?.body?.items?.item!!
                if (itemList != null) {
                    searchAdapter.setList(itemList as ArrayList<Item>)
                }
                searchAdapter.notifyDataSetChanged()
                Log.i("plantItem", "itemList = ${itemList.toString()}")
            }

            override fun onFailure(call: Call<PlantResult>, t: Throwable) {
                Log.i("plantItem", "onFailure t = ${t.message}")

            }
        })



        searchAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }

            fun checkEmpty() {
                binding.emptyView.visibility = (if (searchAdapter.itemCount == 0) View.VISIBLE else View.GONE)
            }
        })






    }

    private var searchViewTextListener: SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                searchAdapter.filter.filter(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                searchAdapter.filter.filter(s)
                Log.d(TAG, "SearchVies Text is changed : $s")
                return false
            }
        }

    

}

