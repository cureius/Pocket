package com.cureius.pocket.feature_sms_sync.presentation

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cureius.pocket.R
import com.cureius.pocket.feature_sms_sync.util.SyncUtils
import com.cureius.pocket.feature_transaction.domain.model.Transaction

class PopUpService : Service() {
    private var mWindowManager: WindowManager? = null
    private var mFloatingView: View? = null

    private var transaction: Transaction? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val date = intent?.getLongExtra("detected-transaction-date", 0)
        val address = intent?.getStringExtra("detected-transaction-address")
        val body = intent?.getStringExtra("detected-transaction-body")

        // Do something with the data here
        if (date != null && address != null && body != null) {
            SyncUtils.extractTransactionalDetails(
                date, address, body,
            ).let {
                Log.d("Pop Up Start", "onStartCommand: $it")
                transaction = it
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        // Create the floating view
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.pop_up_window, null)

//        // Get a reference to the TextView element
        val amount = mFloatingView!!.findViewById<TextView>(R.id.detected_amount)
        val account = mFloatingView!!.findViewById<TextView>(R.id.detected_account)
        val type = mFloatingView!!.findViewById<TextView>(R.id.detection_type)
        val icon = mFloatingView!!.findViewById<ImageView>(R.id.imageView)
//        val categoryRecyclerView = mFloatingView!!.findViewById<RecyclerView>(R.id.categoryRecyclerView)
//        val potRecyclerView = mFloatingView!!.findViewById<RecyclerView>(R.id.potRecyclerView)

        // Set a click listener on the TextView element

        icon.setOnClickListener {
            // Change the text of the TextView element when it is clicked
            amount.text = transaction?.amount.toString()
            account.text = transaction?.account.toString()
            type.text = transaction?.type.toString()
        }

        // Add the view to the WindowManager
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            null
        }
        mWindowManager!!.addView(mFloatingView, params)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove the view from the WindowManager
        mWindowManager?.removeView(mFloatingView)
    }
}
