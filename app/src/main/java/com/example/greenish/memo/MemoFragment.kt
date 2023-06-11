package com.example.greenish.memo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.greenish.DetailFragment
import com.example.greenish.MainActivity
import com.example.greenish.R
import com.example.greenish.databinding.FragmentMemoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MemoFragment : Fragment() {
    private lateinit var memoViewModel : MemoViewModel
    private lateinit var binding: FragmentMemoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigation(true)
        return inflater.inflate(R.layout.fragment_memo, container, false)
    }

    lateinit var currentImageUri: String


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(true)
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            binding.imageBtn.setImageURI(result.data?.data)
            currentImageUri = result.data?.data.toString()
        }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                intent.type = "image/*"
                getContent.launch(intent)


            } else {
                Log.i("Permission: ",  "Denied")
            }
        }


    private fun showPermissionContextPopup(){
        AlertDialog.Builder(mainActivity)
            .setTitle("권한이 필요합니다")
            .setMessage("전자액자에서 사진을 선택하려면 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA)
            }
            .setNegativeButton("취소하기",{ _,_ ->})
            .create()
            .show()
    }
    fun Activity.setStatusBarTransparent() {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().setStatusBarTransparent()
        val fragmentMemoBinding = FragmentMemoBinding.bind(view)
        binding = fragmentMemoBinding
        val name = arguments?.getString("name")
        mainActivity = context as MainActivity
        memoViewModel = ViewModelProvider(this)[MemoViewModel::class.java]
        binding.btnMemo.setOnClickListener {
            addItem()
        }


        binding.imageBtn.setOnClickListener {
            lateinit var intent: Intent
            when {
                ContextCompat.checkSelfPermission(
                    mainActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    intent = Intent(Intent.ACTION_PICK)
                    intent.type = MediaStore.Images.Media.CONTENT_TYPE
                    intent.type = "image/*"
                    getContent.launch(intent)
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    mainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                    showPermissionContextPopup()
                }
                ContextCompat.checkSelfPermission(
                    mainActivity,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    val extras = intent?.extras
                    val bitmap = extras?.get("data") as Bitmap?
                    binding.imageBtn.setImageBitmap(bitmap)
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    mainActivity,
                    Manifest.permission.CAMERA
                ) -> {
                    showPermissionContextPopup()
                }
                else -> {
                    requestPermissionLauncher.launch(
                        android.Manifest.permission.CAMERA)
                }
            }

        }


    }

    private fun addItem(){
        val addMemo = binding.memo.text.toString()
        val name = arguments?.getString("name")
        val image = arguments?.getString("image")
        if (inputCheck(addMemo)){
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DATE)

            val memo = Memo(name!!,addMemo, currentImageUri, year, month, day)
            memoViewModel.addMemo(memo)
            Toast.makeText(requireContext(),"메모가 등록되었습니다", Toast.LENGTH_SHORT).show()
            val detailFragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString("image", image)
            bundle.putString("name", name)
            detailFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .commit()


        } else{
            Toast.makeText(requireContext(),"Please fill out all field", Toast.LENGTH_SHORT).show()
        }

    }

    fun hideBottomNavigation(bool: Boolean) {
        val bottomNavigation: BottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        if (bool == true) bottomNavigation.visibility = View.GONE else bottomNavigation.visibility =
            View.VISIBLE
    }
    private fun inputCheck(updateName: String):Boolean{
        return !(TextUtils.isEmpty(updateName))
    }

    override fun onDestroy() {
        super.onDestroy()

        val mainActivity = activity as MainActivity
    }

}