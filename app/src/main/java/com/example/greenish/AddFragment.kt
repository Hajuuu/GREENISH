package com.example.greenish

import android.R.attr.button
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import android.widget.NumberPicker
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.greenish.databinding.FragmentAddBinding
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*


class AddFragment : DialogFragment() {
    lateinit var navController: NavController
    private lateinit var binding: FragmentAddBinding
    private lateinit var plantViewModel: PlantViewModel
    lateinit var mainActivity: MainActivity
    var dataList:MutableList<Plant> = mutableListOf()


    lateinit var currentImageUri: String

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            binding.imageBtn.setImageURI(result.data?.data)
            currentImageUri = result.data?.data.toString()
            if(currentImageUri == null)
                Toast.makeText(requireContext(),"사진을 입력하세요",Toast.LENGTH_SHORT).show()

        }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return view


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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val fragmentAddBinding = FragmentAddBinding.bind(view)
        binding = fragmentAddBinding

        plantViewModel = ViewModelProvider(this)[PlantViewModel::class.java]

        binding.spinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.itemList, android.R.layout.simple_spinner_item)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               binding.spinText.text = "${binding.spinner.getItemAtPosition(position)}"
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 60
        binding.numberPicker.wrapSelectorWheel = false
        binding.numberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS


        binding.numberPicker.setOnValueChangedListener { numberPicker, i1, i2 ->

        }

        binding.imageBtn.setOnClickListener {

            when {
                ContextCompat.checkSelfPermission(
                    mainActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = MediaStore.Images.Media.CONTENT_TYPE
                    intent.type = "image/*"
                    getContent.launch(intent)
                }
                shouldShowRequestPermissionRationale(mainActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else -> {
                     requestPermissionLauncher.launch(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

        }

        binding.editDate.setOnClickListener{


            val datePickerDialog = DatePickerDialog(
                mainActivity,
                { _, year, monthOfYear, dayOfMonth ->
                    val dat = (year.toString()+"/"+(monthOfYear + 1)+"/"+dayOfMonth.toString())
                    binding.editDate.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.reWater.setOnClickListener{


            val datePickerDialog = DatePickerDialog(
                mainActivity,
                { _, year2, monthOfYear2, dayOfMonth2->
                    val dat = (year2.toString()+"/"+(monthOfYear2 + 1)+"/"+dayOfMonth2.toString())
                    binding.reWater.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.saveBtn.setOnClickListener {
            val name = binding.editName.text.toString()
            val place = binding.spinText.text.toString()
            val date = binding.editDate.text.toString()
            val water = binding.reWater.text.toString()
            val waterCycle = binding.numberPicker.value.toString()
            val plant = Plant(name,currentImageUri, place, date, water, waterCycle)

            if(binding.editName.text.toString() == "" ){
                Toast.makeText(requireContext(),"이름을 입력하세요",Toast.LENGTH_SHORT).show()
            }
            if(currentImageUri == null)
                Toast.makeText(requireContext(),"사진을 입력하세요",Toast.LENGTH_SHORT).show()
            else if(binding.editName.text.toString() != "" && binding.spinText.text.toString() != ""&& currentImageUri != null){

                plantViewModel.addPlant(plant)
                this.dismiss()
            }

        }

    }

    private fun showPermissionContextPopup(){
        AlertDialog.Builder(mainActivity)
            .setTitle("권한이 필요합니다")
            .setMessage("전자액자에서 사진을 선택하려면 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton("취소하기",{ _,_ ->})
            .create()
            .show()
    }
    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = windowManager.currentWindowMetricsPointCompat()
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }



    fun WindowManager.currentWindowMetricsPointCompat(): Point {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
            val windowInsets = currentWindowMetrics.windowInsets
            var insets: Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
            windowInsets.displayCutout?.run{
                insets = Insets.max(
                    insets, Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
                )
            }
            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top+insets.bottom
            Point(currentWindowMetrics.bounds.width() - insetsWidth,
                currentWindowMetrics.bounds.height() - insetsHeight)


        } else{
            Point().apply {
                defaultDisplay.getSize(this)
            }
        }
    }

}


