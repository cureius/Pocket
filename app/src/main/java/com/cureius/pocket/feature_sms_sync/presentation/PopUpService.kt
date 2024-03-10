package com.cureius.pocket.feature_sms_sync.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.Build
import android.os.IBinder
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.OnTouchListener
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cureius.pocket.R
import com.cureius.pocket.feature_category.domain.model.Category
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.use_case.PotUseCases
import com.cureius.pocket.feature_sms_sync.util.SyncUtils
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PopUpService : Service(), PotAdapter.OnItemSelectedListener {
    @Inject
    lateinit var potUseCases: PotUseCases

    @Inject
    lateinit var transactionUseCases: TransactionUseCases
    private var mWindowManager: WindowManager? = null
    private var mFloatingView: View? = null
    private var pots: List<Pot>? = null
    private var transaction: Transaction? = null
    private lateinit var params: WindowManager.LayoutParams
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private var currentPosition: Int? = null
    private var selectedPot: Pot? = null
    private var instructionText: String? = null
    private var textView: TextView? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val date = intent?.getLongExtra("detected-transaction-date", 0)
        val address = intent?.getStringExtra("detected-transaction-address")
        val body = intent?.getStringExtra("detected-transaction-body")

        if (date != null && address != null && body != null) {
            SyncUtils.extractTransactionalDetails(
                date, address, body,
            ).let {
                Log.d("Pop Up Transaction", "onStartCommand: $it")
                transaction = it
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        val context = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
            1,
            Notification()
        )

        val categories = listOf(
            Category(
                icon = "food",
                title = "Food",
            ),
            Category(
                icon = "entertainment",
                title = "Fun",
            ),
            Category(
                icon = "travel",
                title = "Travel",
            ),
            Category(
                icon = "house",
                title = "House",
            ),
            Category(
                icon = "fuel",
                title = "Fuel",
            ),
            Category(
                icon = "health",
                title = "Health",
            ),
            Category(
                icon = "shopping",
                title = "Shopping",
            ),
            Category(
                icon = "grocery",
                title = "Grocery",
            ),
        )
        // Create the floating view
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.pop_up_window, null)

        val imageView = mFloatingView!!.findViewById<ImageView>(R.id.imageView)
        val amount = mFloatingView!!.findViewById<TextView>(R.id.detected_amount)
        val account = mFloatingView!!.findViewById<TextView>(R.id.detected_account)
        val type = mFloatingView!!.findViewById<TextView>(R.id.category_title)
        val body = mFloatingView!!.findViewById<TextView>(R.id.detection_message)
        val categoryRecyclerView =
            mFloatingView!!.findViewById<RecyclerView>(R.id.categoryRecyclerView)
        val potRecyclerView = mFloatingView!!.findViewById<RecyclerView>(R.id.potRecyclerView)
        val confirmButton = mFloatingView!!.findViewById<Button>(R.id.confirm_button)
        categoryRecyclerView.visibility = GONE

        textView =  mFloatingView!!.findViewById<TextView>(R.id.textView)
        instructionText = "No POT selected for this Transaction yet"
        val text = instructionText

        // Create a SpannableString to apply different styles
        val spannableString = SpannableString(text)

        // Find the index of the word "POT" and apply styles
        val potIndex = text?.indexOf("POT", ignoreCase = true)
        if (potIndex != null) {
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.black)),
                potIndex,
                potIndex + 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (potIndex != null) {
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                potIndex,
                potIndex + 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Find the index of the word "Transaction" and apply styles
        val transactionIndex = text?.indexOf("Transaction", ignoreCase = true)
        if (transactionIndex != null) {
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.black)),
                transactionIndex,
                transactionIndex + 11,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (transactionIndex != null) {
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                transactionIndex,
                transactionIndex + 11,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Set the SpannableString to the TextView
        textView!!.text = spannableString

        var dataList: List<Pot> = listOf()
        val layoutFlag: Int
        val potAdapter = PotAdapter(dataList, currentPosition, this)
        val categoryAdapter = CategoryAdapter(categories, currentPosition)
        scope.launch {
            potUseCases.getPots().collect {
                pots = it
                potAdapter.dataList = it
            }
        }
        scope.launch {
            potRecyclerView.adapter = potAdapter
            potRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            categoryRecyclerView.adapter = categoryAdapter
            categoryRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }


        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.LEFT
                x = 0
                y = 100
            }
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.LEFT
                x = 0
                y = 100
            }
        }
        mWindowManager!!.addView(mFloatingView, params)

        //Set the close button
        val closeButtonCollapsed =
            mFloatingView!!.findViewById<View>(R.id.close_btn) as ImageView
        closeButtonCollapsed.setOnClickListener { //close the service and remove the from from the window
            stopSelf()
        }
        val dashCard = mFloatingView!!.findViewById<View>(R.id.dash_card) as ConstraintLayout

        closeButtonCollapsed.visibility = GONE
        dashCard.visibility = GONE

        val rootContainer =
            mFloatingView!!.findViewById<View>(R.id.root_container) as RelativeLayout

        confirmButton.setOnClickListener {
            scope.launch {
                try {
                    if (selectedPot != null && transaction != null) {
                        println("transaction $transaction")
                        println("selectedPot $selectedPot")
                        transaction!!.event_timestamp?.let { timestamp ->
                            var transactionToUpdate =
                                transactionUseCases.getTransactionByEventTimestamp(
                                    timestamp
                                )
                            if (transactionToUpdate != null) {
                                transactionUseCases.updateTransaction(
                                    transactionToUpdate.copy(pot = selectedPot!!.title)
                                )
                                stopSelf()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("PopUpService", "onCreate: ", e)
                }

            }
        }
        mFloatingView!!.findViewById<View>(R.id.root_container)
            .setOnTouchListener(object : OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {

                            //remember the initial position.
                            initialX = params.x
                            initialY = params.y

                            //get the touch location
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }

                        MotionEvent.ACTION_UP -> {
                            val Xdiff = (event.rawX - initialTouchX).toInt()
                            val Ydiff = (event.rawY - initialTouchY).toInt()

                            //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                            //So that is click event.
                            if (Xdiff < 10 && Ydiff < 10) {
                                if (closeButtonCollapsed.visibility == VISIBLE) {
                                    closeButtonCollapsed.visibility = GONE
                                } else {
                                    closeButtonCollapsed.visibility = VISIBLE
                                }
                                if (dashCard.visibility == VISIBLE) {
                                    dashCard.visibility = GONE
                                } else {
                                    if(transaction!=null){
                                        dashCard.visibility = VISIBLE
                                    }
                                }
                                // Change the text of the TextView element when it is clicked
                                if(transaction?.amount != null){
                                    amount.visibility = VISIBLE
                                    amount.text = transaction?.amount.toString()
                                }else{
                                    amount.visibility = GONE
                                    imageView.visibility = GONE
                                }
                                if(transaction?.account != null){
                                    account.visibility = VISIBLE
                                    account.text = "Account: " + transaction?.account.toString()
                                }else{
                                    account.visibility = GONE
                                }
                                if(transaction?.type != null){
                                    type.visibility = VISIBLE
                                    type.text = transaction?.type.toString()
                                }else{
                                    type.visibility = GONE
                                }
                                if(transaction?.body != null){
                                    body.visibility = VISIBLE
                                    body.text = transaction?.body.toString()
                                }else{
                                    body.visibility = GONE
                                }
                                Log.d("OnClick", "onTouch: $dataList")

                                mWindowManager!!.updateViewLayout(mFloatingView, params)
                            }
                            return true
                        }

                        MotionEvent.ACTION_MOVE -> {
                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + (event.rawX - initialTouchX).toInt()
                            params.y = initialY + (event.rawY - initialTouchY).toInt()

                            //Update the layout with new X & Y coordinate
                            mWindowManager!!.updateViewLayout(mFloatingView, params)
                            return true
                        }
                    }
                    return false
                }
            })
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        if (mFloatingView != null) {
            mWindowManager?.removeView(mFloatingView)
        }
        // Remove the view from the WindowManager
    }

    private fun startMyOwnForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val NOTIFICATION_CHANNEL_ID = "com.example.simpleapp"
            val channelName = "My Background Service"
            val chan = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE
            )
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            manager.createNotificationChannel(chan)
            val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            val notification =
                notificationBuilder.setOngoing(true).setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE).build()
            startForeground(2, notification)
        }
    }

    override fun onItemSelected(pot: Pot) {
        Toast.makeText(this, "Pot Selected ${pot.title}", Toast.LENGTH_SHORT).show()
        selectedPot = pot
        textView!!.text = "Selected pot: ${pot.title}"
    }

}
