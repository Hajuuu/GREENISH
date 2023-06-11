package com.example.greenish


import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import com.example.greenish.databinding.FragmentBluetoothBinding
import java.util.*


class BluetoothFragment : Fragment() {

    private val CHANNEL_ID = "plantwater"   // Channel for notification
    private var notificationManager: NotificationManager? = null
    private lateinit var bt: BluetoothSPP
    private lateinit var binding: FragmentBluetoothBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bluetooth, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotificationChannel(CHANNEL_ID, "GREENISH", "식물의 수분이 부족합니다")
        val fragmentBluetoothBinding = FragmentBluetoothBinding.bind(view)
        binding = fragmentBluetoothBinding
        bt = BluetoothSPP(context)
        if (bt.isBluetoothAvailable) {
            Toast.makeText(requireContext(),
                "블루투스 사용이 불가합니다",
                Toast.LENGTH_SHORT
            ).show()
        }


        bt.setOnDataReceivedListener { _, message ->
            val array: List<String> = message.split(",")
            when {
                 array[1].toInt() in 541..800 -> binding.progressbar.progress = 20
                 array[1].toInt() in 351..540 -> binding.progressbar.progress = 70
                 array[1].toInt() in 260..350 -> binding.progressbar.progress = 100
            }
            binding.water.text = array[0]
            binding.temp.text = array[2].plus("C")
            binding.humidity.text = array[3].plus("%")
            if(array[0] == "수분이 부족합니다")
                displayNotification(


                )


        }

        bt.setBluetoothConnectionListener(object : BluetoothConnectionListener {

            override fun onDeviceConnected(name: String, address: String) {
                Toast.makeText(
                    mainActivity.applicationContext,
                    "$name\n$address 에 연결되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeviceDisconnected() { //연결해제
                Toast.makeText(
                    mainActivity.applicationContext,
                    "연결이 해제되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeviceConnectionFailed() { //연결실패
                Toast.makeText(
                    mainActivity.applicationContext,
                    "연결에 실패하였습니다",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.btnConnect.setOnClickListener {
            if (bt.serviceState == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
                bt.disconnect()
            } else {

                val intent = Intent(
                    mainActivity.applicationContext,
                    DeviceList::class.java
                )
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bt.disconnect()
    }


    override fun onStart() {
        super.onStart()


        if (bt.isBluetoothEnabled) {

            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT)



        } else {
            if (bt.isServiceAvailable) {
                bt.setupService()

                bt.startService(BluetoothState.DEVICE_OTHER)
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data)
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService()
                bt.startService(BluetoothState.DEVICE_OTHER)

            } else {
                Toast.makeText(
                    mainActivity.applicationContext,
                    "블루투스 사용이 불가합니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createNotificationChannel(channelId: String, name: String, channelDescription: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT // set importance
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = channelDescription
            }
            notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }


    private fun displayNotification() {
        val notificationId = 66

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(mainActivity.applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.flower)
                .setContentTitle("물주기 알림")
                .setContentText("화분의 수분이 부족합니다!")
                .build()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        notificationManager?.notify(notificationId, notification)
    }
}
