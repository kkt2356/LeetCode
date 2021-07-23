# 카카오페이증권 사전과제 - 특정고객 거래내역 조회서비스
## 목차
- [개발 프레임워크](#개발-프레임워크)
- [문제해결 방법](#문제해결-방법)
- [빌드 및 실행 방법](#빌드-및-실행-방법)


---

## 개발 프레임워크
- 기본 환경
    - IDE : STS 4.11.0
    - OS : Windows
    - JAVA 1.8
    - Gradle 7.1.1
    - Spring Boot 2.4.5
    - h2
    - Junit4


## 문제해결 방법
### 1. Jar 어플리케이션 실행 시, InitData에서 Resourec의 getFile을 로딩할 때, FileNotFoundException이 발생함.
- Jar가 classpath가 아닌 resources의 경로를 그대로 사용해서 발상하는 문제임. ClassLoader.getResourceAsStream()을 사용하여 해결.


```java
private void initAccount() throws IOException {
    // ...
        	InputStream is = getClass().getResourceAsStream("/account_info.csv");
        	BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    // ...
}
```
### 2. 2018년, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출하는 API 개발
- Request

```
http://localhost:8080/account/maxPriceByYear
```

```
GET /account/maxPriceByYear HTTP/1.1
```

- Response

```json
[
  {
    "name": "테드",
    "year": "2018",
    "sumAmt": 28992000,
    "acctNo": "11111114"
  },
  {
    "name": "에이스",
    "year": "2019",
    "sumAmt": 40998400,
    "acctNo": "11111112"
  }
]
```
- `JpaRepository`의 `@Query` 어노테이션을 사용하여 데이터를 정제 및 추출 할 수 있었다.
- 해당 쿼리는 GROUP BY를 통해 데이터를 연도별, 사용자별 거래금액 리스트를 추출 하였으며, JOIN 및 서브쿼리를 통해 연도별, 사용자별 거래금액의 최대값을 선택할 수 있었다.

### 3. 2018년 또는 2019년에 거래가 없는 고객을 추출하는 API 개발
- Request

```
http://localhost:8080/account/notTransactionByYear
```

```
GET /account/notTransactionByYear HTTP/1.1
```

- Response

```json
[
  {
    "name": "사라",
    "year": "2018",
    "acctNo": "11111115"
  },
  {
    "name": "제임스",
    "year": "2018",
    "acctNo": "11111118"
  },
  {
    "name": "에이스",
    "year": "2018",
    "acctNo": "11111121"
  },
  {
    "name": "테드",
    "year": "2019",
    "acctNo": "11111114"
  },
  {
    "name": "제임스",
    "year": "2019",
    "acctNo": "11111118"
  },
  {
    "name": "에이스",
    "year": "2019",
    "acctNo": "11111121"
  }
]
```
- `JpaRepository`의 `@Query` 어노테이션을 사용하여 데이터를 정제 및 추출 할 수 있었다.
- UNION을 통하여 2018, 2019년도의 데이터를 조회 후 합쳐서 해결하였다.

### 4. 연도별 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력하는 API 개발
- Request

```
http://localhost:8080/account/maxBranchPriceByYear
```

```
GET /account/maxBranchPriceByYear HTTP/1.1
```

- Response

```json
[
  {
    "year": "2018",
    "dataList": [
      {
        "sumAmt": 38484000,
        "brName": "분당점",
        "brCode": "B"
      },
      {
        "sumAmt": 20505700,
        "brName": "판교점",
        "brCode": "A"
      },
      {
        "sumAmt": 20232867,
        "brName": "강남점",
        "brCode": "C"
      },
      {
        "sumAmt": 14000000,
        "brName": "잠실점",
        "brCode": "D"
      }
    ]
  },
  {
    "year": "2019",
    "dataList": [
      {
        "sumAmt": 66795100,
        "brName": "판교점",
        "brCode": "A"
      },
      {
        "sumAmt": 45396700,
        "brName": "분당점",
        "brCode": "B"
      },
      {
        "sumAmt": 19500000,
        "brName": "강남점",
        "brCode": "C"
      },
      {
        "sumAmt": 6000000,
        "brName": "잠실점",
        "brCode": "D"
      }
    ]
  },
  {
    "year": "2020",
    "dataList": [
      {
        "sumAmt": 1000000,
        "brName": "을지로점",
        "brCode": "E"
      }
    ]
  }
]
```
- Response JSON 의 형태로 가공하기 위해 `BranchByYearResult.java`, `MaxPriceByBranchResult.java` 두개의 DTO를 사용하였다. 코드는 아래와 같다.
- 그 후 결과를 합친후 Return하여 해결하였다.
```java
public class BranchByYearResult {
    private String year;
    private List<MaxPriceByBranchResult> dataList = new ArrayList<>();
    public String getYear() {
    	return year;
    };
    public List<MaxPriceByBranchResult> getDataList() {
    	return dataList;
    };
    public void setYear(String year) {
    	this.year = year;
    };
    public void setDataList(List<MaxPriceByBranchResult> dataList) {
    	this.dataList = dataList;
    };
}
```

```java
    public interface MaxPriceByBranchResult {
    	String getBrName();
    	String getBrCode();
    	int getSumAmt();
    }
```

### 5. 분당점과 판교점을 통폐합하여 판교점으로 관리점을 이관 및 지점명을 입력하면 해당지점의 거래금액의 합계를 출력하는 API 개발
- Request

```
http://localhost:8080/account/sumTransactionByBranch
```

```
POST /account/sumTransactionByBranch HTTP/1.1
```
- Rquest

```json
  {
    "branchName" : "판교점"
  }
```

- Response

```json
[
  {
    "sumAmt": 171181500,
    "brName": "판교점",
    "brCode": "A"
  }
]
```
- `판교점`과 `분당점` 을 통폐합 하여서, `분당점` 입력시 `404`로 리턴해야한다.
- `분당점` 입력시 `throw new NotFoundException()`를 사용하여 강제로 에러상태로 리턴시킨다.
- `@ExceptionHandler`, `ResponseEntity`를 사용하여, `NotFoundException()` 도달 시, 사용자가 지정한 코드 및 메시지를 띄우도록 한다.

```java
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotfoundResult> notFoundException(NotFoundException e) {
		NotfoundResult er = new NotfoundResult();
		er.setCode("404");
		er.setMsg("br code not found error");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);	
    }
```
## 빌드 및 실행 방법
### Windows 환경
```
$ 구글드라이브(__) 에서 '사전과제.zip' 을 내려받음. 그 후 압축해제.
$ cmd에서 'cd {압축해제 경로}/사전과제/과제1' 명령어를 입력하여 해당 경로로 이동.
$ 'gradlew clean build' 명령어 수행.
$ 'java -jar build/libs/kps-kkt-test-0.0.1-SNAPSHOT.jar' 명령어 수행.
$ http://localhost:8080/swagger-ui.html 접속.
