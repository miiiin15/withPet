package com.withpet.mobile

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.withpet.mobile.databinding.CustomAlertBinding
import com.withpet.mobile.databinding.CustomSnackbarBinding
import com.withpet.mobile.ui.activity.Location.LocationSearchActivity
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

    // 1버튼 알림 대화상자
    fun showAlert(
        message: String,
        title: String? = null,
        buttonText: String? = null,
        onPress: (() -> Unit)? = null
    ) {
        if (!isFinishing && !isDestroyed) {
            try {
                val binding = CustomAlertBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(this)
                builder.setView(binding.root)

                val dialog = builder.create()

                title?.let {
                    binding.llDlgTitleLayout.visibility = View.VISIBLE
                    binding.txvDlgTitle.text = it
                }

                binding.txvDlgContent.text = message
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 2버튼 알림 대화상자
    protected fun showAlert(
        message: String,
        title: String? = null,
        positiveText: String? = null,
        negativeText: String? = null,
        onPress: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null
    ) {
        if (!isFinishing && !isDestroyed) {
            try {
                val binding = CustomAlertBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(this)
                builder.setView(binding.root)

                val dialog = builder.create()

                title?.let {
                    binding.llDlgTitleLayout.visibility = View.VISIBLE
                    binding.txvDlgTitle.text = it
                }

                binding.txvDlgContent.text = message
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    protected fun showExitAlert() {
        if (!isFinishing && !isDestroyed) {
            try {
                val binding = CustomAlertBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(this)
                builder.setView(binding.root)

                val dialog = builder.create()

                title?.let {
                    binding.llDlgTitleLayout.visibility = View.VISIBLE
                    binding.txvDlgTitle.text = it
                }

                binding.txvDlgContent.text = "앱을 종료하시겠습니까?"
                binding.btnDlgPositive.text = "종료"
                binding.btnDlgNegative.text = "취소"

                binding.btnDlgPositive.setOnClickListener {
                    dialog.dismiss()
                    finish()
                }

                binding.btnDlgNegative.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setOnShowListener {
                    val window = dialog.window
                    window?.setBackgroundDrawableResource(android.R.color.transparent)
                }
                dialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    // 커스텀 토스트 메시지
//    protected fun showToast(
//        message: String, duration: Int = Toast.LENGTH_SHORT, onPress: (() -> Unit)? = null
//    ) {
//        val inflater = LayoutInflater.from(this)
//        val binding = CustomToastBinding.inflate(inflater)
//
//        val toast = Toast(this).apply {
//            setDuration(duration)
//            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
//            view = binding.root
//        }
//
//        binding.txToastMessage.text = message
//        binding.btnToast.setOnClickListener {
//            toast.cancel()
//            onPress?.invoke()
//        }
//
//      toast.show()
//    }

    // 커스텀 스넥바 메시지
    fun showSnackBar(
        message: String,
        buttonText: String? = null,
        duration: Int = Snackbar.LENGTH_SHORT,
        onPress: (() -> Unit)? = null
    ) {
        if (!isFinishing && !isDestroyed) {
            try {
                val inflater = LayoutInflater.from(this)
                val binding = CustomSnackbarBinding.inflate(inflater)

                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "", duration)

                // Snackbar의 기본 텍스트와 동작 버튼을 숨깁니다.
                snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text).visibility =
                    View.INVISIBLE
                snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_action).visibility =
                    View.INVISIBLE

                binding.txSnackbarMessage.text = message
                binding.btnSnackbar.text = buttonText ?: "확인"
                binding.btnSnackbar.setOnClickListener {
                    snackbar.dismiss()
                    onPress?.invoke()
                }

                val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
                snackbarLayout.addView(binding.root, 0)
                snackbarLayout.setBackgroundResource(android.R.color.transparent)
                snackbar.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
                // 슬라이드 이벤트 처리 필요시 추가
            }
        })
    }


    fun showRegionPopup() {
        // 팝업 내부에 동적으로 콘텐츠를 추가하는 예제
        val contentFrame: FrameLayout = bottomSheetView.findViewById(R.id.content_frame)
        val customView =
            LayoutInflater.from(this).inflate(R.layout.custom_view_positioning_recomend, null)
        contentFrame.addView(customView)

        val positiveButton: AppCompatButton =
            customView.findViewById(R.id.btn_move_setting_position)
        val negativeButton: AppCompatButton = customView.findViewById(R.id.btn_hide_position_popup)

        positiveButton.setOnClickListener {
            startActivity(Intent(this, LocationSearchActivity::class.java))
        }
        negativeButton.setOnClickListener {
            hidePopup()
        }

        bottomSheetDialog?.show()
    }

    protected fun showPopup() {
        bottomSheetDialog?.show()
    }

    protected fun hidePopup() {
        bottomSheetDialog?.dismiss()
        clearPositionPopupContent()
    }

    protected fun clearPositionPopupContent() {
        val contentFrame: FrameLayout? = bottomSheetView.findViewById(R.id.content_frame)
        contentFrame?.removeAllViews()
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
