package com.reve.abroady.presentation.login.activity

import com.reve.abroady.R
import com.reve.abroady.databinding.ActivityMainBinding
import com.reve.abroady.presentation.base.BaseActivity
import com.reve.abroady.presentation.login.loginviewmodel.FireBaseLoginViewModel
import com.reve.abroady.util.ActivityList
import com.reve.abroady.util.DialogAlert
import com.reve.abroady.util.DialogSelect
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_login

    val fireBaseLoginViewModel : FireBaseLoginViewModel by inject()
    val selectDialog by lazy { DialogSelect(this) }

    // Login에 관련된 Fragment들이 선택지가 2개인 다이얼로그를 필요로 할 수 있으므로 Fragment가 종속된 Activity에 다이얼로그 버튼 클릭 리스너 선언
    private lateinit var dialogLeftBtnClickListener : (() -> Unit)
    private lateinit var dialogRightBtnClickListener : (() -> Unit)

    override fun initStartView() { }

    fun showAlertDialog(title : String, content : String) {
        val dialog = DialogAlert(this)
        dialog.start(title, content)
    }

    fun setDialogLeftBtnClickListener(listener : () -> Unit) {
        this.dialogLeftBtnClickListener = listener
    }

    fun setDialogRightBtnClickListener(listener : () -> Unit) {
        this.dialogRightBtnClickListener = listener
    }

    fun showSelectDialog(title : String, content : String) {
        selectDialog.setLeftBtnClickListener {
            dialogLeftBtnClickListener.invoke()
        }
        selectDialog.setRightBtnClickListener {
            dialogRightBtnClickListener.invoke()
        }
        selectDialog.start(title, content)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityList.actList.remove(this)
    }
}