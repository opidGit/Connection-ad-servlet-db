# Andoird - Servlet Connection Template #

## Introduction ##
안드로이드와 데이터베이스를 연결하기 위한 템플릿으로, 중간에 웹서버(아파치 톰캣)를 통해서 데이터를 주고받는다. 서버는 서블릿으로 작성되어 있으며 POST 방식과 GET방식 모두 처리할 수 있다. 클라이언트 역시 각각의 POST 방식, GET 방식 메서드를 만들어놈으로써 선택하여 사용할 수 있다.

#### 수정사항 ####
- 2014.12.16 - 클라이언트에서 패키지 template_2에 get방식, post방식 수정하고 서버에서 getData 파일 수정. 
	- 정상적인 통신 성공.

## Data form ##
- TEXT
- JSON

## Android package ##
### template_1 ###
HTTP 통신과 관련된 클래스와 액티비티를 분할하여놓은 패키지.
### template_2 ###
액티비티 하나도 통합한 패키지.
### template_json ###
json을 사용하여 데이터를 처리하는 패키지(post)

## Using API ##
- json-simple-1.1.1.jar
- mysql-connector-java-5.1.30-bin.jar


## 참고사이트 ##
- [안드로이드 AsynTask](http://www.androidsnippets.com/asyntask-in-android)
