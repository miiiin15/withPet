package com.withpet.mobile.ui.test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.R
import com.withpet.mobile.ui.custom.*

class TestActivity : BaseActivity() {

    private lateinit var customInput: CustomInput
    private lateinit var customInput_disable: CustomInput
    private lateinit var customSelect: CustomSelect
    private lateinit var customSelect_disable: CustomSelect
    private lateinit var customSelect_gender: CustomSelect
    private lateinit var btnTest1: CustomButton
    private lateinit var btnTest2: CustomButton
    private lateinit var btnTest3: CustomButton
    private lateinit var btnTest4: CustomButton
    private lateinit var btnTest5: CustomButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        customInput = findViewById(R.id.customInput)
        customInput_disable = findViewById(R.id.customInput_disable)
        customSelect = findViewById(R.id.customSelect)
        customSelect_disable = findViewById(R.id.customSelect_disable)
        customSelect_gender = findViewById(R.id.customSelect_gender)
        btnTest1 = findViewById(R.id.btn_test1)
        btnTest2 = findViewById(R.id.btn_test2)
        btnTest3 = findViewById(R.id.btn_test3)
        btnTest4 = findViewById(R.id.btn_test4)
        btnTest5 = findViewById(R.id.btn_test5)

        customSelect_disable.setDisable(true)
        customInput_disable.setDisable(true)
        customSelect_gender.type = "gender"

        customInput.setIsValidListener(object : IsValidListener {
            override fun isValid(text: String): Boolean {
                return try {
                    val value = text.toInt()
                    value >= 0
                } catch (e: NumberFormatException) {
                    false
                }
            }
        })

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

        btnTest1.setOnClickListener {
            showAlert(
                "input : ${customInput.text}\nselect : ${customSelect.getValue()}\nselect_gender : ${customSelect_gender.getValue()}",
                "인풋 현황"
            ) {
                Toast.makeText(this, "showAlert onPress", Toast.LENGTH_SHORT).show()
            }
        }
        btnTest2.setOnClickListener {
            showAlert(
                message = "버튼이 2개지요",
                title = "투버튼",
                onPress = {
                    Toast.makeText(this, "showAlert onPress", Toast.LENGTH_SHORT).show()
                },
                onCancel = {
                    Toast.makeText(this, "showAlert onCancel", Toast.LENGTH_SHORT).show()
                }
            )
        }

        btnTest3.setOnClickListener {
            loadingDialog.show(supportFragmentManager, "")
            Handler(Looper.getMainLooper()).postDelayed({
                loadingDialog.dismiss()
            }, 3000)
        }

        btnTest4.setOnClickListener {
            showSnackBar(
                message = "이건 테스트 입니다.",
                buttonText = "보러가기",
                onPress = { showAlert("스낵바 버튼 누름") }
            )
        }



        btnTest5.setOnClickListener {
            val intent = Intent(this, SampleActivity::class.java)
            startActivity(intent)
        }

    }


}
