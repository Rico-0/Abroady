# Abroady
한국에 거주하는 외국인, 교환학생 신분으로 대학에 다니는 외국인 등을 위한 커뮤니티 어플리케이션

메인 화면 (1)

![Untitled](https://user-images.githubusercontent.com/45986958/183343764-e3e38266-3d4e-4ddb-9b39-47e7650f1570.png)

메인 화면 (2)

![Untitled (1)](https://user-images.githubusercontent.com/45986958/183343768-49d8d3a6-b938-4181-a701-f54a0b9b7fdd.png)

파티 상세 화면

![Untitled (2)](https://user-images.githubusercontent.com/45986958/183343773-cc77505e-ac86-494c-9c87-cda0a26f9223.png)

## 도입해야 하는 기술 & 수정 사항
1. 액세스 토큰 & 리프레시 토큰 저장 시 SharedPreference 변수에 그대로 저장하지 말고 클래스 하나 생성해서 putToken, getToken 메소드 작성한 후 사용하기
2. 소셜 로그인 코드가 반복되는 부분이 많고 가독성이 떨어짐. MVVM 패턴으로 수정 필요
