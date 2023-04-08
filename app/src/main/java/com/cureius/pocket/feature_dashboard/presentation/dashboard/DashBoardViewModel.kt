package com.cureius.pocket.feature_dashboard.presentation.dashboard

import android.content.ContentResolver
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_sms_sync.util.SyncUtils
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val contentResolver: ContentResolver,
    private val transactionUseCases: TransactionUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tag: String = "DashViewModel"

    private val _infoSectionVisibility = mutableStateOf(false)
    val infoSectionVisibility: State<Boolean> = _infoSectionVisibility

    private val _addAccountPromptVisibility = mutableStateOf(false)
    val addAccountPromptVisibility: State<Boolean> = _addAccountPromptVisibility

    private val _addPotPromptVisibility = mutableStateOf(false)
    val addPotPromptVisibility: State<Boolean> = _addPotPromptVisibility

    private val _startSyncPromptVisibility = mutableStateOf(true)
    val startSyncPromptVisibility: State<Boolean> = _startSyncPromptVisibility

    private val _permissionPrompt = mutableStateOf(false)
    val permissionPrompt: State<Boolean> = _permissionPrompt

    private val _creditDetectionPromptVisibility = mutableStateOf(false)
    val creditDetectionPromptVisibility: State<Boolean> = _creditDetectionPromptVisibility

    private val _isSmsReadComplete = mutableStateOf(false)
    val isSmsReadComplete: State<Boolean> = _isSmsReadComplete

    private val _totalBalance = mutableStateOf("------")
    val totalBalance: State<String> = _totalBalance

    private val _incomeMtd = mutableStateOf("------")
    val incomeMtd: State<String> = _incomeMtd

    private val _spentMtd = mutableStateOf("------")
    val spentMtd: State<String> = _spentMtd

    private val _allSms = mutableStateOf(listOf<Transaction>())
    val allSms: State<List<Transaction>> = _allSms

    private var readSMSJob: Job? = null

    fun onEvent(event: DashBoardEvent) {
        when (event) {
            is DashBoardEvent.ToggleInfoSectionVisibility -> {
                _infoSectionVisibility.value = !infoSectionVisibility.value
            }

            is DashBoardEvent.ToggleAskPermission -> {
                _permissionPrompt.value = !permissionPrompt.value
            }

            is DashBoardEvent.ChangeTotalBalance -> {
                _totalBalance.value = event.balance
            }

            is DashBoardEvent.ChangeIncomeMtd -> {
                _incomeMtd.value = event.value
            }

            is DashBoardEvent.ChangeSpentMtd -> {
                _spentMtd.value = event.value
            }

            is DashBoardEvent.ReadAllSMS -> {
                val longTaskListener = object : LongTaskListener {
                    override fun onTaskComplete(result: List<Transaction>) {
                        Log.d(tag, "Sync Complete")
                        _isSmsReadComplete.value = true
                    }

                    override fun onError(throwable: Throwable) {
                        Log.d(tag, "Sync Failed")
                    }
                }
                _allSms.value = getAllSms(longTaskListener)
            }
            is DashBoardEvent.SaveAllTransactions -> {
                viewModelScope.launch {
                    try {
                        transactionUseCases.addTransactions(
                            allSms.value
                        )
                    } catch (e: InvalidTransactionException) {

                    }
                }
            }
        }
    }

    interface LongTaskListener {
        fun onTaskComplete(result: List<Transaction>)
        fun onError(throwable: Throwable)
    }


//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun initialSmsSync(activity: ComponentActivity) {
//        val myViewModel = ViewModelProvider(activity)[AddTransactionViewModel::class.java]
//        val isFirstLoad: Boolean =
//            SharedPreferencesUtil.getBooleanValueToSharedPreferences(activity, "initial_sync")
//        if (isFirstLoad) {
//            myViewModel.onEvent(AddTransactionEvent.EnteredTransactionsList(readSMS()))
//            myViewModel.onEvent(AddTransactionEvent.SaveAllTransactions)
//            SharedPreferencesUtil.setBooleanValueToSharedPreferences(
//                activity,
//                "initial_sync",
//                false
//            )
//        }
//    }

    private fun getAllSms(listener: LongTaskListener): List<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        val cursor = contentResolver.query(
            android.provider.Telephony.Sms.CONTENT_URI, null, null, null, null
        )
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    if (cursor?.moveToFirst() == true) {
                        do {
                            val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                            val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                            val date = cursor.getLong(cursor.getColumnIndexOrThrow("date"))
                            val type = cursor.getInt(cursor.getColumnIndexOrThrow("type"))

                            if (type == 1
                                && (body.lowercase().contains("a/c") || body.lowercase()
                                    .contains("card"))
                                && (body.contains("credited") || body.contains("debited"))
                            ) {
                                Log.d(
                                    tag, "format:  $address, ${
                                        SyncUtils.extractTransactionalDetails(
                                            date, address, body
                                        )
                                    }"
                                )
                                SyncUtils.extractTransactionalDetails(
                                    date, address, body
                                ).let { transactionList.add(it) }
                            }
                        } while (cursor.moveToNext())
                    }
                    cursor?.close()
                    listener.onTaskComplete(transactionList.reversed())
                }
                transactionUseCases.addTransactions(
                    transactionList.reversed()
                )
            } catch (e: Exception) {
                listener.onError(e)
            }
        }
        return transactionList.reversed()
    }
}