# 🐾 프로젝트 소개 ![Watchers](https://img.shields.io/github/watchers/miiiin15/withPet?style=social) ![Repository Size](https://img.shields.io/github/repo-size/miiiin15/withPet?color=%23FCC419)
![헤더](https://capsule-render.vercel.app/api?type=rect&height=100&color=FCC419&text=위드펫%20🐾&fontColor=ffffff&animation=fadeIn&fontSize=45&desc=마음에%20맞는%20산책%20친구를%20찾다&descAlignY=80&fontAlignY=40&descSize=20&textBg=false)

이 프로젝트에 대한 더 많은 정보는 아래 블로그 게시글에서 확인하실 수 있습니다:
- [블로그 게시글 링크](https://velog.io/@gg04253/series/withPet)

# 기술 스택

- **구조**: MVVM
- **기본**: Kotlin, AAC(ViewModel, LiveData, Navigation), retrofit2
- **이미지**: Glide
- **테스트**: Firebase App Distribution

# 특징

### RESTful API 통신 / JWT 기반 인증 처리
`Interceptor`와 토큰 보안을 위해 `EncryptedSharedPreferences`를 활용한 암호화 처리

- **OkHttpClient 설정**: [Network.kt](app/src/main/java/com/withpet/mobile/data/api/Network.kt)
   ```kotlin
      val tokenRepository = TokenRepository(context)
      val okHttpClient = OkHttpClient.Builder().apply {
         addInterceptor(AddInterceptor(tokenRepository)) // 요청 인터셉터
         addInterceptor(ReceiveInterceptor(tokenRepository)) // 응답 인터셉터
         addInterceptor(LoggingInterceptor.create()) // 로깅 인터셉터
         ...생략
      }.build()
   ```
- **응답 헤더에서 약속된 토큰값 추출**: [ReceiveInterceptor.kt](app/src/main/java/com/withpet/mobile/data/api/ReceiveInterceptor.kt)
   ```kotlin
   class ReceiveInterceptor(private val tokenRepository: TokenRepository) : Interceptor {
       override fun intercept(chain: Interceptor.Chain): Response {
           val response = chain.proceed(chain.request())
           response.headers("X-ACCESS-TOKEN").firstOrNull()
               ?.let { tokenRepository.saveAccessToken(it) }
           ...생략
           return response
       }
   }
   ```

- **추출한 토큰값을 AES256으로 암호화해 EncryptedSharedPreferences에 저장**: [TokenRepository.kt](app/src/main/java/com/withpet/mobile/data/api/TokenRepository.kt)
   ```kotlin
   class TokenRepository(private val context: Context) {
		private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

       private val preferences = EncryptedSharedPreferences.create(
        context,
        "encryptedDataPreferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
   
       fun saveAccessToken(token: String) {
           preferences.edit().putString("access-token", token).apply()
       }
       fun getAccessToken(): String? = preferences.getString("access-token", null)
       ...생략
   }
   ```

- **요청시 다시 꺼내서 헤더에 첨부**: [AddInterceptor.kt](app/src/main/java/com/withpet/mobile/data/api/AddInterceptor.kt)
   ```kotlin
   class AddInterceptor(private val tokenRepository: TokenRepository) : Interceptor {
       override fun intercept(chain: Interceptor.Chain): Response {
           val builder = chain.request().newBuilder()
   
           val accessToken = tokenRepository.getAccessToken()
           accessToken?.let { builder.addHeader("X-ACCESS-TOKEN", it) }
           ...생략
           return chain.proceed(builder.build())
       }
   }
   ```
### **Custom UI Components**
사용자 경험을 향상시키기 위해 제작한 커스텀 뷰
- **[CustomEditText](./app/src/main/java/com/withpet/mobile/ui/custom/CustomEditText.kt) ↔ [CustomInput](./app/src/main/java/com/withpet/mobile/ui/custom/CustomInput.kt)**: 사용자 입력을 위한 커스텀 위젯 [ [게시글 링크](https://velog.io/@gg04253/withPet3) ]
- **[CustomOption](./app/src/main/java/com/withpet/mobile/ui/custom/CustomOption.kt) ↔ [CustomSelect](./app/src/main/java/com/withpet/mobile/ui/custom/CustomSelect.kt)**: 콤보 박스 역할을 하는 커스텀 선택 위젯 [ [게시글 링크](https://velog.io/@gg04253/withPet5) ]
- **CustomAlert**, **CustomSnackBar**: Dialog, Snackbar에 UI요소를 확장한 커스텀 위젯 [ [게시글 링크](https://velog.io/@gg04253/withPet6) ]

<table border="1" style="text-align: center; width: 100%; margin-bottom: 40px;">
  <tr>
    <th>1. 로그인</th>
    <th>2. 홈화면</th>
    <th>3. 매칭 및 좋아요</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/e9005624-65f9-4840-856d-17f1d787142e" alt="로그인" height="500"></td>
    <td><img src="https://github.com/user-attachments/assets/13ce08aa-ddc7-4e91-8cc1-2bcd58dd1d8b" alt="홈화면" height="500"></td>
    <td><img src="https://velog.velcdn.com/images/gg04253/post/f678c328-bf0c-46bb-af5b-52d222680a7d/image.gif" alt="매칭 및 좋아요" height="500"></td>
  </tr>
</table>

# 실행 방법

서버 담당자 개인 사정으로 서버 상시운영은 하고있지 않습니다.

해당 브랜치는 Hilt/Koin 없이 수동 DI 구현을 작업중인 브랜치입니다. 원할한 실행을 위해서 `develop`브랜치를 clone 하시길 바랍니다.

1. **환경 요구 사항**
    - **Java Development Kit (JDK)**: 1.8 이상
    - **Gradle**: 프로젝트에 포함된 `gradle-wrapper.properties` 파일에서 버전을 확인하십시오.
    - **Android SDK**: 최소 SDK 23 이상, 타겟 SDK 30 이상
    - **Kotlin**: 버전 1.5 이상

2. **프로젝트 클론 및 설정**
   ```bash
   git clone https://github.com/miiiin15/withPet.git
   cd withPet
   ```

3. **Android Studio에서 프로젝트 열기**
    - Android Studio를 열고 `File > Open`을 통해 클론한 프로젝트를 선택합니다.
    - 필요한 Gradle 종속성 파일이 자동으로 다운로드됩니다.

4. **Gradle 파일 동기화**
    - `build.gradle` 파일과 함께 필요한 플러그인 및 종속성을 동기화합니다.
    - 동기화 과정에서 문제가 발생하면 Android SDK와 JDK 버전을 확인하고, 요구 사항에 맞게 설정합니다.

5. **프로젝트 빌드 및 실행**
    - Android Studio의 `Run` 버튼을 클릭하여 프로젝트를 빌드하고 실행합니다.
    - **에뮬레이터** 또는 **실제 기기**에서 테스트할 수 있습니다.


# 🚧 기획되었지만 구현되지 않은 기능들

팀원의 사정으로 프로젝트 신규기능 개발은 일시중지된 상황입니다:

- **좋아요 Push 알림 및 메시지**: 좋아요 받은 경우 사용자에게 푸시 알림을 보내고, 매칭된 사용자와 메시지를 주고받을 수 있는 기능. : Firebase 클라우드 메시징
- **산책 경로 추적 및 기록**: 사용자의 산책 경로를 추적하고 기록하는 기능. : Map(네이버, 구글) api
- **유저 간 커뮤니티**: 유저 간 정보 공유와 소통을 위한 커뮤니티 기능.
- **출시 및 Play Store 자동 배포**: 앱 출시 및 Fastlane을 통한 자동 배포 구현.




