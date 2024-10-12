package com.withpet.mobile.ui.activity.Location

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.withpet.mobile.BaseActivity
import com.withpet.mobile.data.repository.LocationRepo
import com.withpet.mobile.databinding.ActivityLocationSearchBinding
import com.withpet.mobile.utils.PermissionUtils
import java.lang.Exception
import java.text.DecimalFormat

class LocationSearchActivity : BaseActivity() {

    // View Binding 변수 선언
    private lateinit var binding: ActivityLocationSearchBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 초기화
        binding = ActivityLocationSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 외부 뷰 터치 시 키보드 내리기와 포커스 해제
        binding.outsideView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.outsideView.windowToken, 0)
                binding.inputSearchText.clearFocus()
            }
            true
        }

        // 검색 버튼 클릭 리스너 설정
        binding.ivSearchButton.setOnClickListener {
            performSearch(binding.inputSearchText.text.toString())
        }

        // "현재 위치로 찾기" 버튼 클릭 리스너 설정
        binding.btnFindCurrentLocation.setOnClickListener {
            findLocationUsingCurrentPosition()
        }

        // ListView 클릭 리스너 설정
        binding.lvSearchResults.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            onAddressSelected(selectedItem)
        }
    }

    // 7. 검색 버튼을 눌렀을 때 API 호출
    private fun performSearch(query: String) {
        // 검색 API 호출 로직 구현
        // 예: ApiClient.searchLocation(query)...

        // 예시: API 호출 후 콜백에서 데이터 처리
        val results = listOf<String>() // API 결과를 여기에 설정

        if (results.isEmpty()) {
            // 9. 검색 결과가 없을 경우
            binding.tvNoResults.visibility = View.VISIBLE
            binding.lvSearchResults.visibility = View.GONE
        } else {
            // 8. 결과값을 리스트뷰에 세팅
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, results)
            binding.lvSearchResults.adapter = adapter

            // 검색 결과가 있을 경우
            binding.tvNoResults.visibility = View.GONE
            binding.lvSearchResults.visibility = View.VISIBLE
        }

        // 11. 입력한 값을 텍스트뷰에 세팅
        binding.tvInputtedAddress.text = query
    }

    // 10. "현재 위치로 찾기" 버튼 클릭 리스너
    private fun findLocationUsingCurrentPosition() {
        // 위치 권한이 있는지 확인
        if (PermissionUtils.isLocationPermissionGranted(this)) {
            // 권한이 있는 경우 현재 위치의 좌표값을 가져옴
            getCurrentLocation()
        } else {
            // 권한이 없을 경우 로그 출력 또는 사용자에게 권한 요청
            showAlert("위치 권한이 허용되지 않았습니다."){
                PermissionUtils.requestLocationPermission(this)
            }
        }
    }

    @SuppressLint("MissingPermission") // 이 주석은 권한이 체크되었음을 알려줌
    private fun getCurrentLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val locationProvider = LocationManager.GPS_PROVIDER

        // 마지막으로 알려진 위치 가져오기
        val lastKnownLocation: Location? = locationManager.getLastKnownLocation(locationProvider)

        if (lastKnownLocation != null) {
            // 소숫점 12자리로 포맷팅
            val decimalFormat = DecimalFormat("#.############")

            val x = decimalFormat.format(lastKnownLocation.longitude).toDouble()
            val y = decimalFormat.format(lastKnownLocation.latitude).toDouble()


            saveLocation(x, y)
        } else {
            showAlert("현재 위치를 가져올 수 없습니다.")
        }
    }

    private fun saveLocation(x: Double, y: Double) {
        loadingDialog.show(supportFragmentManager, "")
        try {
            LocationRepo.saveLocation(x, y, success = {
                showSnackBar("동네 설정이 완료 됐어요!"){
                    finish()
                }
                // TODO : 유저정보 저장 로직 실행할 것
            },
                networkFail = {
                    showAlert(it)
                },
                failure = {
                    showAlert(it.message ?: "failure")
                }
            )
        } catch (e: Exception) {
            showAlert(e.message ?: "error")
        } finally {
            loadingDialog.dismiss()
        }
    }

    // 리스트의 아이템 클릭 리스너
    private fun onAddressSelected(address: String) {
        // 선택된 주소를 사용하여 다음 작업 수행
        // 예: 주소 저장 API 호출, 화면 전환 등
    }

}
