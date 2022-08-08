# Abroady
한국에 거주하는 외국인, 교환학생 신분으로 대학에 다니는 외국인 등을 위한 커뮤니티 어플리케이션

## 도입해야 하는 기술 & 수정 사항
1. 액세스 토큰 & 리프레시 토큰 저장 시 SharedPreference 변수에 그대로 저장하지 말고 클래스 하나 생성해서 putToken, getToken 메소드 작성한 후 사용하기
2. 소셜 로그인 코드가 반복되는 부분이 많고 가독성이 떨어짐. MVVM 패턴으로 수정 필요
