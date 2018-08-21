# MSA Application을 작성하기 위해 필요한 기반 기술을 사용한 Sample Application

[Framework and Library]
Spring Boot
Spring Data JPA
Tymeleaf
JQeury, jsrender

[Test Driven]
Repository(or DAO)
 - Junit

Service
 - Junit
 
Api
 - Junit
 - Postman

Web
 - Selenium(katalon)

 
[SampleBoard Application]
Repository

Service

Api

Web



Katalon + ajax 기반 web application test case 작성 시 주의사항
테스트 대상의 web element에 id나 name이 반복적인 test에도 문제가 없도록 구분할 수 있어야 한다.
db sequence의 unique id를 id나 name에 사용한 경우 테스트 전에 반드시 db 초기화가 필요하다.

