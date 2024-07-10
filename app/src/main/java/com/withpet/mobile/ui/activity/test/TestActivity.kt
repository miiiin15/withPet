package com.withpet.mobile.ui.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.api.response.PetAddRequest
import com.withpet.mobile.data.repository.PetRepo
import com.withpet.mobile.ui.custom.CustomButton
import com.withpet.mobile.ui.custom.CustomInput
import com.withpet.mobile.ui.custom.CustomSelect
import com.withpet.mobile.ui.custom.Option

class TestActivity : BaseActivity() {

    private lateinit var customInput: CustomInput
    private lateinit var customInput_disable: CustomInput
    private lateinit var customSelect: CustomSelect
    private lateinit var customSelect_disable: CustomSelect
    private lateinit var customSelect_gender: CustomSelect
    private lateinit var btnAlertTest: CustomButton
    private lateinit var btnLoadingTest: CustomButton
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
        customSelect_gender = findViewById(R.id.customSelect_gender)
        btnAlertTest = findViewById(R.id.btn_alert_test)
        btnLoadingTest = findViewById(R.id.btn_loading_test)
        btnReserved1 = findViewById(R.id.btn_reserved_1)
        btnReserved2 = findViewById(R.id.btn_reserved_2)

        customSelect_disable.setDisable(true)
        customInput_disable.setDisable(true)
        customSelect_gender.type = "gender"

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

        btnAlertTest.setOnClickListener {
            showAlert(
                "input : ${customInput.text}\nselect : ${customSelect.getValue()}\nselect_gender : ${customSelect_gender.getValue()}",
                "인풋 현황"
            )
        }

        btnLoadingTest.setOnClickListener {
            loadingDialog.show(supportFragmentManager, "")
            Handler(Looper.getMainLooper()).postDelayed({
                loadingDialog.dismiss()
            }, 3000)
        }

        btnReserved1.setOnClickListener {
            loadingDialog.show(supportFragmentManager, "")
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
                },
                finally = {
                    loadingDialog.dismiss()
                }
            )
        }

        btnReserved2.setOnClickListener {
            Toast.makeText(this, "예비 2 버튼 클릭", Toast.LENGTH_SHORT).show()
        }

    }


}
