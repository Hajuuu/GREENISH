package com.example.greenish

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.databinding.FragmentDetailBinding
import com.example.greenish.memo.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var plantViewModel : PlantViewModel
    private lateinit var memoViewModel : MemoViewModel
    private lateinit var place: String
    lateinit var currentImageUri: String
    private lateinit var memos: List<Memo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(true)
    }


    fun Activity.setStatusBarTransparent() {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        if(Build.VERSION.SDK_INT >= 30) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setStatusBarTransparent()
        val intent = Intent(context, DetailFragment::class.java)
        val fragmentDetailBinding = FragmentDetailBinding.bind(view)
        binding = fragmentDetailBinding
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(true)
        val name = arguments?.getString("name")
        val image = arguments?.getString("image")
        place = arguments?.getString("place").toString()
        val date = arguments?.getString("date")
        var water = arguments?.getString("water")
        val waterCycle = arguments?.getString("waterCycle")

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        var sf = SimpleDateFormat("yyyy/MM/dd")
        var startDate = sf.parse(date).time
        var calcuDate = (today - startDate) / (60 * 60 * 24 * 1000)
        binding.textdDay.text=name+"이랑 함께한 지 "+calcuDate+"일 째"
        var water2 = sf.parse(water).time
        when {
            ((today-water2)/ (60 * 60 * 24 * 1000)) >= waterCycle!!.toLong() -> binding.waterCycle.text = "물을 줘야 합니다"
            today > water2 -> binding.waterCycle.text = ((waterCycle!!.toLong()-(today-water2)/ (60 * 60 * 24 * 1000))).toString()+"일 뒤에 물을 줘야 합니다"
            else -> binding.waterCycle.text = "물을 줬습니다"
        }
        binding.recentWater.text = water
        binding.imageDetail.setImageURI(Uri.parse(image))
        binding.textDetail.text=name
        // val data = intent.getParcelableExtra<Plant>("data")
        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        if (image != null) {
            currentImageUri = image
        }
        binding.imageDetail.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            getContent.launch(intent)
        }

        binding.fab.setOnClickListener {
            val activity=requireView().context as AppCompatActivity
            val memoFragment = MemoFragment()
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putString("image", image)
            bundle.putString("place", place)
            bundle.putString("date", date)
            bundle.putString("water", water)
            bundle.putString("waterCycle", waterCycle)

            memoFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().add(R.id.main_nav_host, memoFragment).addToBackStack(null).commit()
        }

        val adapter = MemoAdapter()
        val recyclerView = binding.rcview


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        memoViewModel = ViewModelProvider(this)[MemoViewModel::class.java]



        context?.let { MemoRoomDatabase.getDatabase(it).memoDao().loadMemoByName(name!!) }
            ?.observe(viewLifecycleOwner, Observer{
                memo->adapter.submitList(memo)
            })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.bindingAdapterPosition
                val memo = adapter.currentList[pos]

                memoViewModel.deleteMemo(memo)

                Snackbar.make(view, "삭제되었습니다", Snackbar.LENGTH_LONG).apply{
                    setAction("취소"){
                        memoViewModel.addMemo(memo)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rcview)
        }



    }
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            binding.imageDetail.setImageURI(result.data?.data)
            currentImageUri = result.data?.data.toString()
        }


    private fun updateItem(){
        val updateName = binding.textDetail.text.toString()

        val date = arguments?.getString("date")
        val water = arguments?.getString("water")
        val waterCycle = arguments?.getString("waterCycle")
        if (inputCheck(updateName)){
            val plant = Plant(updateName,currentImageUri,place, date, water, waterCycle)

            plantViewModel.updatePlant(plant)

            Toast.makeText(requireContext(),"UpdatedSuccessfully",Toast.LENGTH_SHORT).show()

            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)
                ?.commit()

        } else{
            Toast.makeText(requireContext(),"Please fill out all field",Toast.LENGTH_SHORT).show()
        }

    }


    private fun inputCheck(updateName:String):Boolean{
        return !(TextUtils.isEmpty(updateName))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(false)
    }
}