# 스프링 배치 프로그램
엑셀을 이용한 메뉴 등록

# 준비사항
- 데이터베이스
  - 상점용 데이터베이스 생성(delivery_store)
  - 스프링 배치 메타 테이블 생성 (https://bit.ly/3Gpn31S)

# Run Configuration
배치 프로그램 실행을 위해 필요한 옵션
```shell
-Dspring.batch.job.name=excelJob -DexecutionId=[임의의 실행 숫자] -DfilePath=[엑셀 위치] 
```