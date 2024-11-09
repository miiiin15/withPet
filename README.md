
# 🐾 프로젝트 소개 ![Watchers](https://img.shields.io/github/watchers/miiiin15/withPet?style=social)
![헤더](https://capsule-render.vercel.app/api?type=rect&height=100&color=FCC419&text=위드펫%20🐾&fontColor=ffffff&animation=fadeIn&fontSize=45&desc=마음에%20맞는%20산책%20친구를%20찾다&descAlignY=80&fontAlignY=40&descSize=20&textBg=false)




![GitHub commit activity](https://img.shields.io/github/commit-activity/m/miiiin15/withPet?color=%23FCC419&label=Monthly%20Commit)
![Weekly Commits](https://img.shields.io/github/commit-activity/w/miiiin15/withPet?color=%23FCC419&label=Weekly%20Commits)
![GitHub last commit](https://img.shields.io/github/last-commit/miiiin15/withPet?color=%23FCC419&label=Last%20Commit)
![Repository Size](https://img.shields.io/github/repo-size/miiiin15/withPet?color=%23FCC419) 

이 프로젝트는 사용자의 편의성을 높이고 반려동물과의 삶을 더욱 풍요롭게 만드는 것을 목표로 합니다.

# 📧 연락처

문의사항이 있을 경우 이메일로 연락 부탁드립니다:

이메일 문의: gg04253@gmail.com

# 📝 관련 블로그 게시글

이 프로젝트에 대한 더 많은 정보는 아래 블로그 게시글에서 확인하실 수 있습니다:
- [블로그 게시글 링크](https://velog.io/@gg04253/series/withPet)

# 🛠️ 기술 스택

- **언어**: Kotlin
- **아키텍처 패턴**: [📝MVVM](https://velog.io/@gg04253/withPet9) (Model-View-ViewModel)
- **의존성 주입**: [📝Hilt](https://velog.io/@gg04253/withPet8) (DI)
- **Jetpack 라이브러리**: ViewModel, LiveData, Navigation 등
- **네트워킹**: retrofit2
- **이미지 로딩**: Glide
- **테스트 앱 배포**: Firebase App Distribution

# ✨ 주요 기능

1. **로그인 / 회원가입**: 간편하게 가입하고 로그인할 수 있습니다.
- [🚀 LoginActivity](./app/src/main/java/com/withpet/mobile/ui/activity/login/LoginActivity.kt)
- [🚀 SignupActivity](./app/src/main/java/com/withpet/mobile/ui/activity/signup/SignupActivity.kt)

![위드펫_회원가입](https://github.com/user-attachments/assets/e9005624-65f9-4840-856d-17f1d787142e)


2. **메인 화면 구성**: 로그인 후 메인 화면에서 다양한 기능을 사용할 수 있습니다. 매칭 유저 리스트와 좋아요 기능을 제공합니다. 
- [🚀 MainFragment](./app/src/main/java/com/withpet/mobile/ui/fragment/main/MainFragment.kt)
- [🚀 MainViewModel](./app/src/main/java/com/withpet/mobile/viewmodel/MainViewModel.kt)
- [🚀 MatchedList](./app/src/main/java/com/withpet/mobile/ui/custom/MatchedList.kt)

![위드펫_로그인](https://github.com/user-attachments/assets/13ce08aa-ddc7-4e91-8cc1-2bcd58dd1d8b)


3. **매칭 및 좋아요 관리**: 매칭 탭에서 더 긴 추천 유저 리스트를 제공하며, 좋아요 목록을 관리할 수 있습니다. 
- [🚀 LikedListActivity](./app/src/main/java/com/withpet/mobile/ui/activity/liked/LikedListActivity.kt)
- [🚀 LikedViewModel](./app/src/main/java/com/withpet/mobile/viewmodel/LikedViewModel.kt)

![](https://velog.velcdn.com/images/gg04253/post/f678c328-bf0c-46bb-af5b-52d222680a7d/image.gif)

# 📲 실행 방법

~서버 담당자 개인 사정으로 서버 상시운영은 하고있지 않습니다.~

1. **환경 요구 사항**
   - **Java Development Kit (JDK)**: 1.8 이상
   - **Gradle**: 프로젝트에 포함된 `gradle-wrapper.properties` 파일에서 버전을 확인하십시오.
   - **Android SDK**: 최소 SDK 21 이상, 타겟 SDK 30 이상
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

# 🗂️ 프로젝트 구조

프로젝트는 다음과 같은 구조로 구성되어 있습니다:

- **ViewModel**: 비즈니스 로직을 처리하고 UI와 데이터를 연결하는 역할을 합니다.
- **UI (Activity/Fragment)**: 사용자 인터페이스를 담당하며, 사용자의 상호작용을 처리합니다.
- **Repository**: 데이터 관리를 책임지며, **[🚀 네트워크 요청](./app/src/main/java/com/withpet/mobile/data/api/NetworkService.kt)** 및 로컬 데이터 접근을 통합합니다.
- **Custom UI Components**: 사용자 경험을 향상시키기 위해 직접 제작한 커스텀 뷰들이 포함되어 있습니다. 주요 커스텀 요소는 다음과 같습니다:
  - **[🚀 CustomEditText](./app/src/main/java/com/withpet/mobile/ui/custom/CustomEditText.kt) ↔ [🚀 CustomInput](./app/src/main/java/com/withpet/mobile/ui/custom/CustomInput.kt)**: 사용자 입력을 위한 커스텀 위젯
  - **[🚀 CustomOption](./app/src/main/java/com/withpet/mobile/ui/custom/CustomOption.kt) ↔ [🚀 CustomSelect](./app/src/main/java/com/withpet/mobile/ui/custom/CustomSelect.kt)**: 콤보 박스 역할을 하는 커스텀 선택 위젯
  - **CustomAlert**, **CustomSnackBar**: 사용자에게 알림을 제공하는 커스텀 컴포넌트
  - **LoadingDialog** : 사용자에게 로딩 상태를 표시하는 다이얼로그


# 🚧 기획되었지만 구현되지 않은 기능들

팀원의 사정으로 프로젝트 신규기능 개발은 일시중지된 상황입니다:

- **좋아요 Push 알림 및 메시지**: 좋아요 받은 경우 사용자에게 푸시 알림을 보내고, 매칭된 사용자와 메시지를 주고받을 수 있는 기능. : Firebase 클라우드 메시징
- **산책 경로 추적 및 기록**: 사용자의 산책 경로를 추적하고 기록하는 기능. : Map(네이버, 구글) api
- **유저 간 커뮤니티**: 유저 간 정보 공유와 소통을 위한 커뮤니티 기능.
- **출시 및 Play Store 자동 배포**: 앱 출시 및 Fastlane을 통한 자동 배포 구현.




