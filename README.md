##### 회원가입 폼

- 로그인 상태에서 들어올수 없음
- 폼이 존재
- 폼 체크



##### 회원가입 폼 처리

- 로그인 상태에서 들어올 수 없음.
- 유효성 체크
- member테이블에 회원이 저장됨.



##### 로그인 폼

- 로그인 상태에서 들어올수 없음
- 폼 존재
- 폼 체크 완료



##### 인스타 회원 정보 입력

- 인스타 id 가 이미 존재해도 성별이 U면 연결 가능하게
- 본인의 인스타 id 성별을 고르고 저장



##### 호감 표시 폼 추가

- 인스타 등록시 사용가능
- 상대방 id와 호감이유를 고르고 추가



##### 호감 목록

- 내가 호감 표시한 상대 인스타 id 와 호감 이유를 보여줌
- 호감 수정 및 호감 취소 가능.



##### 카카오 로그인 추가

- 카카오 dev에 앱등록후 rest api key를 받아서 추가
  - rest api key는 노출되지 않게 암호화



##### jasypt추가

- 암호화를 위해서 추가 진행



##### 404 에러 페이지 추가

- 404 발생시 사용자 기준에서 커스텀한 오류페이지를 보여주는게 좋다고 생각해서 진행.