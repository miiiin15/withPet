package com.withpet.mobile

import android.content.*
import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.withpet.mobile.databinding.CustomAlertBinding
import com.withpet.mobile.ui.custom.CustomDialog
import com.withpet.mobile.ui.custom.LoadingDialog

//import com.save.protect.app.data.Constants.context
//import com.save.protect.esbank.ui.view13_etc.ErrorPageActivity
//import com.save.protect.utils.KeyboardVisibilityUtils
//import com.save.protect.utils.OnSingleClickListener
//import com.save.protect.app.data.DataProvider

abstract class BaseActivity : AppCompatActivity() {

    lateinit var loadingDialog: LoadingDialog
//    lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils   //키보드 유틸

    // 하단 팝업
    private var bottomSheetDialog: BottomSheetDialog? = null
    lateinit var bottomSheetView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
//			window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        loadingDialog = LoadingDialog()
        initBottomSheet()
//        DataProvider.unCaughtException(this, ErrorPageActivity())   //Global Exception
    }

    // EditText 밖으로 터치됐을 때 키보드 숨기기
    // 스크롤 할땐 무시,
    // 단순 ActionUp 일 때만 키보드 숨기기
    val editTextActionMoveCount = HashMap<EditText, Int>()

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        currentFocus?.let {
            if (it is EditText) {

//				Logcat.e("MotionEvent: ${ev?.action?.let { MotionEvent.actionToString(it) }?: "노액션" }")

                // 터치 뗄 때
                if (ev?.action === MotionEvent.ACTION_UP) {

                    // ACTION_MOVE 이력이 없는 경우
                    if (editTextActionMoveCount.get(it) == null) {

                        // EditText 바깥쪽 터치 일 때 키보드 숨기기
                        val outRect = Rect()
                        it.getGlobalVisibleRect(outRect)
                        if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                            it.clearFocus()

                            val imm =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(it.getWindowToken(), 0)
                        }
                    }

                    // ACTION_MOVE 이력이 있는 경우
                    else {

                        // N번 미만으로 ACTION_MOVE가 호출되었을 때만 키보드 숨기기
                        // ACTION_MOVE 이력이 조금만 움직여도 바로 쌓인다.
                        editTextActionMoveCount.get(it)?.let { count ->

                            if (count < 5) {

                                // EditText 바깥쪽 터치 일 때 키보드 숨기기
                                val outRect = Rect()
                                it.getGlobalVisibleRect(outRect)
                                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                                    it.clearFocus()

                                    val imm =
                                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.hideSoftInputFromWindow(it.getWindowToken(), 0)
                                }
                            }
                        }
                    }

                    editTextActionMoveCount.clear()

                }

                // 스크롤링
                else if (ev?.action === MotionEvent.ACTION_MOVE) {

                    // ACTION_MOVE 이력이 없는 경우
                    if (editTextActionMoveCount.get(it) == null) {
                        editTextActionMoveCount.put(it, 1)

                    }

                    // ACTION_MOVE 이력이 있는 경우
                    else {
                        editTextActionMoveCount.get(it)?.let { count ->
                            editTextActionMoveCount.put(it, count + 1)
                        }
                    }
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    //중복클릭방지
//    fun View.setOnSingleClickListener(onSingleClick: (View) -> Unit) {
//        val oneClick = OnSingleClickListener {
//            onSingleClick(it)
//        }
//        setOnClickListener(oneClick)
//    }

    // 2버튼 알림 대화상자
    protected fun showAlert(
        msg: String,
        title: String? = null,
        positiveText: String? = null,
        negativeText: String? = null,
        onPress: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null
    ) {
        val binding = CustomAlertBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(binding.root)

        val dialog = builder.create()

        title?.let {
            binding.llDlgTitleLayout.visibility = View.VISIBLE
            binding.txvDlgTitle.text = it
        }

        binding.txvDlgContent.text = msg
        binding.btnDlgPositive.text = positiveText ?: "확인"
        binding.btnDlgNegative.text = negativeText ?: "취소"

        binding.btnDlgPositive.setOnClickListener {
            onPress?.invoke()
            dialog.dismiss()
        }

        binding.btnDlgNegative.setOnClickListener {
            onCancel?.invoke()
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            val window = dialog.window
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialog.show()
    }

    // 1버튼 알림 대화상자
    protected fun showAlert(
        msg: String,
        title: String? = null,
        buttonText: String? = null,
        onPress: (() -> Unit)? = null
    ) {
        val binding = CustomAlertBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(binding.root)

        val dialog = builder.create()

        title?.let {
            binding.llDlgTitleLayout.visibility = View.VISIBLE
            binding.txvDlgTitle.text = it
        }

        binding.txvDlgContent.text = msg
        binding.btnDlgPositive.text = buttonText ?: "확인"
        binding.btnDlgNegative.visibility = View.GONE

        binding.btnDlgPositive.setOnClickListener {
            onPress?.invoke()
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            val window = dialog.window
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }


        dialog.show()
    }

    private fun initBottomSheet() {
        bottomSheetView =
            LayoutInflater.from(this).inflate(R.layout.custom_bottom_sheet_container, null)

        // Bottom sheet dialog 설정
        bottomSheetDialog = BottomSheetDialog(this).apply {
            setContentView(bottomSheetView)
            setOnShowListener {
                val parentLayout = bottomSheetView.parent as ViewGroup
                parentLayout.setBackgroundResource(android.R.color.transparent)
            }
        }

        // Bottom sheet behavior 초기화
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hidePopup()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // TODO : 슬라이드 이벤트 처리 필요시 추가
            }
        })
    }

    protected fun showPopup() {
        bottomSheetDialog?.show()
    }

    protected fun hidePopup() {
        bottomSheetDialog?.dismiss()
    }


    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    //키보드 내리기
    fun closeKeyboard() {
        var view = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

}
