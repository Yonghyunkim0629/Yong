### 인터페이스를 사용한 Db 접속, 조회 구현.

- Junit 테스트 사용
- 탐식기에서 이클립스 workspace 폴더로 이동한 후 
- 탐색기 상단경로에서 CMD 워크스페이스 폴더경로 엔터
- git clone https://github.com/miniplugin/Dbinterface.git
- 이클립스에서 프로젝트 Import 실행
- 이 후 강사와 수업 함께 진행.
- 작업순서1: member테이블과 매칭되는 MemberVO 클래스 제작 > Mapper쿼리작성 > IF와 매칭되는 구현클래스 생성
- 작업순서2: IF클래스에 아래 3개의 메서드 추가 후 구현클래스 자동 생성 후 구현클래스에서 쿼리 호출

```
public void insertMember(MemberVO vo) throws Exception;//학생 실습용 추가
public void delete(String userid) throws Exception;//학생 실습용 추가
public List<MemberVO> listMember() throws Exception;//학생실습용 추가
```

- 구현클래스 쿼리 추가 핵심(구현 SampleMapper 클래스에 아래 @inject 추가

```
@Inject
	private SqlSession sqlSession;
```
