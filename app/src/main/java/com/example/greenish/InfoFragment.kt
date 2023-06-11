package com.example.greenish

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.greenish.databinding.FragmentInfoBinding
import com.example.greenish.retrofit.Retrofit
import com.example.greenish.retrofit2.PlantInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InfoFragment: Fragment() {

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val number = arguments?.getString("id")
        val name = arguments?.getString("name")
        val fragmentInfoBinding = FragmentInfoBinding.bind(view)
        binding = fragmentInfoBinding

        Glide.with(binding.imgInfo.context)
            .load("http://www.nongsaro.go.kr/cms_contents/301/"+number+"_MF_ATTACH_01_TMB.jpg")
            .into(binding.imgInfo)
        binding.nameInfo.text = name
        //binding.textPlant.text = "${String(Character.toChars(0x1F331))} 식물 정보"
        val key = "20220322DOUECFCSKEWEKYXGOMOFLA"
        var call: Call<PlantInfo> = Retrofit.getRetrofit2().plantInfo(key, number!!)


        call.enqueue(object : Callback<PlantInfo> {

            override fun onResponse(call: Call<PlantInfo>, response: Response<PlantInfo>) {
                val plantInfo: PlantInfo? = response.body()
                val itemList = plantInfo?.body
                binding.text1.text = itemList?.item?.get(0)?.speclmanageInfo
                if(itemList?.item?.get(0)?.speclmanageInfo == "") binding.text1.text = "정보가 없습니다"
                binding.text2.text = itemList?.item?.get(0)?.postngplaceCodeNm
                if(itemList?.item?.get(0)?.postngplaceCodeNm == "") binding.text2.text = "정보가 없습니다"
                binding.text3.text = itemList?.item?.get(0)?.prpgtEraInfo
                if(itemList?.item?.get(0)?.prpgtEraInfo == "") binding.text3.text = "정보가 없습니다"
                binding.text4.text = itemList?.item?.get(0)?.soilInfo
                if(itemList?.item?.get(0)?.soilInfo == "") binding.text4.text = "정보가 없습니다"
                binding.text5.text = itemList?.item?.get(0)?.watercycleAutumnCodeNm
                if(itemList?.item?.get(0)?.watercycleAutumnCodeNm == "") binding.text5.text = "정보가 없습니다"
                Log.i("plantItem", "itemList = ${itemList.toString()}")
            }

            override fun onFailure(call: Call<PlantInfo>, t: Throwable) {
                Log.i("plantItem", "onFailure t = ${t.message}")

            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(true)
        hideKeyboard()

    }

    private fun hideKeyboard() {
        if (activity != null && activity?.currentFocus != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity?.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(false)
    }


}