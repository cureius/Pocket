package com.cureius.pocket.feature_dashboard.presentation.dashboard

import android.content.ContentResolver
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cureius.pocket.feature_sms_sync.util.SyncUtils
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val contentResolver: ContentResolver
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

    private val _totalBalance = mutableStateOf("------")
    val totalBalance: State<String> = _totalBalance

    private val _incomeMtd = mutableStateOf("------")
    val incomeMtd: State<String> = _incomeMtd

    private val _spentMtd = mutableStateOf("------")
    val spentMtd: State<String> = _spentMtd

    private val _allSms = mutableStateOf(listOf<Transaction>())
    val allSms: State<List<Transaction>> = _allSms

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
                _allSms.value = getAllSms()
            }
        }
    }

    private fun getAllSms(): List<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        val cursor = contentResolver.query(
            android.provider.Telephony.Sms.CONTENT_URI, null, null, null, null
        )

        if (cursor?.moveToFirst() == true) {
            do {
                val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow("date"))
                val type = cursor.getInt(cursor.getColumnIndexOrThrow("type"))

                if (type == 1
                    && (body.lowercase().contains("a/c") || body.lowercase().contains("card"))
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
        return transactionList.reversed()
    }
}