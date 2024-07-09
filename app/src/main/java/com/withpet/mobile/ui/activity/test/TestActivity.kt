package com.withpet.mobile.ui.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.card.MaterialCardView
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.api.response.PetAddRequest
import com.withpet.mobile.data.repository.PetRepo
import com.withpet.mobile.ui.custom.CustomButton
import com.withpet.mobile.ui.custom.CustomInput
import com.withpet.mobile.ui.custom.CustomSelect
import com.withpet.mobile.ui.custom.Option
import com.withpet.mobile.utils.Logcat

class TestActivity : BaseActivity() {

    private lateinit var customInput: CustomInput
    private lateinit var customInput_disable: CustomInput
    private lateinit var customSelect: CustomSelect
    private lateinit var customSelect_disable: CustomSelect
    private lateinit var btnAlertTest: CustomButton
    private lateinit var btnGenderInputTest: CustomButton
    private lateinit var btnReserved1: CustomButton
    private lateinit var btnReserved2: CustomButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        customInput = findViewById(R.id.customInput)
        customInput_disable = findViewById(R.id.customInput_disable)
        customSelect = findViewById(R.id.customSelect)
        customSelect_disable = findViewById(R.id.customSelect_disable)
        btnAlertTest = findViewById(R.id.btn_alert_test)
        btnGenderInputTest = findViewById(R.id.btn_gender_input_test)
        btnReserved1 = findViewById(R.id.btn_reserved_1)
        btnReserved2 = findViewById(R.id.btn_reserved_2)


        setupGenderPopUp()

        customSelect_disable.setDisable(true)
        customInput_disable.setDisable(true)

        btnAlertTest.setOnClickListener {
            showAlert("input : ${customInput.text}\nselect ${customSelect.getValue()}", "인풋 현황")
        }

        btnGenderInputTest.setOnClickListener {
            showPopup()
        }

        btnReserved1.setOnClickListener {
            val petAddRequest = PetAddRequest(
                size = "소형",
                sex = "남자",
                age = 3,
                introduction = "소개",
                memberId = 47
            )
            PetRepo.savePetInfo(petAddRequest, null,
                success = {
                    if (it.result.code == 200) {
                        showAlert("전송 성공 저장 성공 : ${it.payload}")
                    } else {
                        showAlert("전송 성공 저장 실패 : ${it.result}")
                    }
                },
                networkFail = {
                    showAlert("전송 실패 : $it")
                },
                failure = {
                    showAlert("그냥 실패")
                }
            )
        }

        btnReserved2.setOnClickListener {
            Toast.makeText(this, "예비 2 버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        // Example usage of CustomSelect
        customSelect.setOptions(
            arrayOf(
                Option("하나", "01"),
                Option("다섯", "05"),
                Option("일곱", "07"),
                Option("둘", "02"),
                Option("여덟", "08"),
                Option("열", "10"),
                Option("구십구", "99")
            )
        )


    }


    private fun setupGenderPopUp() {
        val contentFrame: FrameLayout = bottomSheetView.findViewById(R.id.content_frame)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_select_gender_view, null)
        contentFrame.addView(customView)


        val cardView1: MaterialCardView = customView.findViewById(R.id.card_view_1)
        val cardView2: MaterialCardView = customView.findViewById(R.id.card_view_2)
        // 첫 번째 카드뷰에 클릭 리스너 추가
        cardView1.setOnClickListener {
            // 클릭 이벤트 처리
            showAlert("남성", "성별 선택")
        }

        // 두 번째 카드뷰에 클릭 리스너 추가
        cardView2.setOnClickListener {
            // 클릭 이벤트 처리
            showAlert("여성", "성별 선택")
        }

    }
}
