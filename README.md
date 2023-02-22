
<h1 align="center" >
 ⏲️ WAITING CATCH! ⏲️
</h1>
<p align="center">
"사용자가 설정한 지역의 레스토랑을 불러와 줄서기를 대신해주고, 그것과 관련된 서비스를 편리하게를 대신 해주는 웹사이트 입니다."<br> 백엔드 로직에 집중하기 위해서 화면은 최대한 간결하게 만들고 설계하였으며 REST API 서버로 대용량 트래픽을 고려한 애플리케이션으로 개발하였습니다. </p>
<br>

##  :rocket: 프로젝트 사용기술 <br>
<img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"><img src="https://img.shields.io/badge/NaverAPI-03C75A?style=for-the-badge&logo=naver&logoColor=white"><img src="https://img.shields.io/badge/GoogleAPI-4285F4?style=for-the-badge&logo=google&logoColor=white"><img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazon-ec2&logoColor=white"><img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazon-s3&logoColor=white"><img src="https://img.shields.io/badge/nGrinder-F79A10?style=for-the-badge&logo=ngrinder&logoColor=white"><img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=intellij-idea&logoColor=white"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=github&logoColor=white">

<br>


##  :rocket: 전체 프로젝트의 구조
<br>

![Project Structure](https://user-images.githubusercontent.com/83831110/220520960-4fe34da8-7729-405b-82de-e5594dbfc467.png)


<br>
<br>

##  :rocket: WIKI

화면설계에 대한 **kakao oven** 프로토타입 디자인과 Usecase를 보실 수 있습니다.
API에 대한 상세하게 설명해두었으며 기술적인 문제에 부딪혀 해결한 이야기에 대한 개인 테크블로그의 주소도 포함되어있습니다.

 - click! :arrow_forward: [Usecase](https://github.com/f-lab-edu/event-recommender-festa/wiki/Usecase)
 - click! :arrow_forward: [WIKI Home](https://github.com/mitoconcrete/waiting-catch-project/wiki)
 - click! :arrow_forward: [API 상세스펙](https://github.com/f-lab-edu/event-recommender-festa/wiki#%EC%83%81%EC%84%B8-api-%EC%8A%A4%ED%8E%99-%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0)
 - click! :arrow_forward: [PR규칙](https://github.com/f-lab-edu/event-recommender-festa/wiki#pr-%EA%B7%9C%EC%B9%99)
 
<br>
<br>

## 👥 팀원 구성
- 김태훈(리더) : 유저 - 시큐리티, 로그인, 로그아웃<br>
- 박정훈(부리더) : 레스토랑 - 판매자신청, 블랙리스트,레스토랑수정<br>
- 한정규 : 레스토랑 - 메뉴, 카테고리, 검색<br>
- 조성제 : 줄서기 - 호출, 리뷰<br>
- 송경헌 : 이벤트 - 쿠폰 유저 발급쿠폰<br>


##  :rocket: 프로젝트 주요 관심사

:heavy_check_mark: 대용량 트래픽의 상황에서 지속적인 서버 성능을 개선하기 위해 노력하였습니다.    
:heavy_check_mark: 클린코드를 위한 꾸준한 코드 리팩토링을 진행 중입니다.      
:heavy_check_mark: 이유와 근거가 명확한 기술의 사용을 지향합니다.    
:heavy_check_mark: 객체지향적 개념을 이해하고 이를 코드에 녹여내어 의미있는 설계를 지향하였습니다.    
:heavy_check_mark: 성공만 하는 테스트 보단 실패할 만한 단위 테스트를 작성하였습니다.    
:heavy_check_mark: 반복적인 작업은 자동화하여 개발의 효율을 높이기 위해 노력하였습니다.      

<br>

### :diamond_shape_with_a_dot_inside: Git-Flow 브랜치 전략

**Git-Flow 브랜치 전략**에 따라 기능별로 브랜치를 나누어 작업하고 있고 
모든 브랜치에 대해 pull request를 통한 리뷰 완료 후 Merge를 하고 있습니다.

<br>

![깃허브전략](https://user-images.githubusercontent.com/83831110/220528904-f3387e71-30cc-437e-8e9c-5ce8576112ea.svg)


<br>

:white_check_mark: master : 제품으로 출시될 수 있는 브랜치를 의미합니다.     
:white_check_mark: develop : 다음 출시 버전을 개발하는 브랜치입니다. feature에서 리뷰완료한 브랜치를 Merge하고 있습니다.    
:white_check_mark: feature : 기능을 개발하고 hotfix 버그 수정도 같이하는 브랜치 입니다.    

<br>


### :diamond_shape_with_a_dot_inside: PR 규칙

- 브랜치는 develop을 기반으로 생성하고, 도메인 단위로 생성하고 PR을 요청합니다. feature/도메인명 (영어로)<br>
- [Github Project](https://github.com/users/mitoconcrete/projects/6/views/1)를 사용 : New: 새로운기능, Ready: 만들어야하는 기능, In progress: 만들고 있는 기능,<br> In review: 리뷰 중인 기능, Done: 끝난기능 으로 나누어 분업화 한다.
- commit은 C/R/U/D 기능단위로 묶고 [커밋분류] #이슈번호 커밋메시지 형식으로 만든다. ex:[feat] #1 로그인 기능을 추가합니다.
- 모든 PR은 반드시 지정한 리뷰어에게 코드리뷰를 받아야만 합니다. 
- 리뷰어 중 모든 리뷰어의  `Approve` 를 받아야 `Merge pull request` 를 할 수 있습니다.
- 모든 PR은 Github Action의 CI/CD를 통과하고 통과가 되어야 `Merge pull request` 된다.
<br>

### :diamond_shape_with_a_dot_inside: 리뷰

정해진 커밋 컨벤션과 코딩 컨벤션을 지켜 일관성을 유지합니다.<br>
합의되지 않은 코드는 리뷰를 통해 필터링 합니다. 팀원 전원의 승인이 있어야, Merge가 가능합니다.<br>
오전(10시 - 13시)은 PR에 대해 팀원전원이 리뷰합니다. <br>
리뷰는 우선순위를 정하여, 다른 작업에 영향을 줄 수 있는 것 부터 차례대로 진행합니다. <br>리뷰는 아침회의에서 결정된 우선순위대로 진행하며, 리뷰 이후에 Merge를 한번에 진행합니다.

### :diamond_shape_with_a_dot_inside: nGrinder 성능테스트

:white_check_mark: 네이버에서 개발한 오픈소스 플랫폼인 **nGrinder**를 이용해 성능테스트를 진행 중입니다.

<br>
<br>

##  :rocket: 화면 구성도

![image](https://user-images.githubusercontent.com/58355531/109332890-15c1f580-78a2-11eb-9596-eadf94a1ac10.png)

<br>

___


![image](https://user-images.githubusercontent.com/58355531/109332798-f6c36380-78a1-11eb-9283-92796a76ade2.png)

<br>
<br>

##  :rocket: DB ERD 구조

## DB ERD<br>
![DB ERD](https://user-images.githubusercontent.com/83831110/220526975-21451a53-93f4-4911-9b31-37e8a65a10da.png)

<br>
<br>

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Ff-lab-edu%2Fevent-recommender-festa&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
