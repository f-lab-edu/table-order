# 고려 사항
1. 일단 storeId, tableNum, apiKey 등의 정보는 테이블 오더 설치 시 서버에서 ID를 받아 파일에 저장했다고 가정.
   => storeId는 apiKey로 추출하는 방식이 나을지, parameter로 주는게 나을지
   => 일단 apiKey로 추출하는 방식 채택 (보안이나 효율성이 더 나을 것 같음)
2. 주문 내역은 API 호출로 가져오는 것이 나을지 Front에서 갖고 있는 것이 나을지
3. 선 결제 방식일 땐 테이블의 주문 내역 clear는 어떻게 하는게 나은가? => POS에 테이블을 똑같이 보여주니까 직원이 clear하는 걸로?
4. ApiKey는 캐싱하려고 하는데 Redis를 사용 해야할까? (단일 서버면 필요 없을 거 같은데) - 2025.05.02

# version
- 2025.04.16 :
    - API 추가
        - PATCH /menu/table/{tableNum}/language
        - GET /menu
        - GET /menu/{menuId}
        - GET /menu/call
        - POST /order/table/{tableNum}
        - POST /order/table/{tableNum}/call
- 2025.04.27 : isRequired 필드 추가
- 2025.05.25 : GET /order/table/{tableNum} API 추가

# header
    ```
    Content-Type: application/json
    Authorization: Bearer {apiKey}
    ```
# 에러 Reposne
    ```json
    {
        "code" : "오류 코드",
        "message" : "오류 내용"
    }
    ```
# 1. Store

## 가게 등록 (회원 가입)


# 2. Menu

## 언어 변경
- API : PATCH /menu/table/{tableNum}/language
- Request
    ```json
    {
        "language" : "en"    
    }
    ```
- Response
    ```json
    {
        "code" : 200,
        "message" : ""
    }
    ```

## 메뉴 조회
- API : GET /menu
- Request
- Response
    ```json
    {
        "code" : 200,
        "message" : "",
        "data" : [
            {
                "categoryId" : "ObjectId1",
                "categoryName" : "피자",
                "menu" : [
                    {
                        "menuId" : "ObjectId3",
                        "menuName" : "치즈 피자",
                        "price" : 8000,
                        "image" : "https://image.table-order.com/1_1.jpg",                        
                        "isSoldOut" : false,
                        "isOptionEnabled" : true,
                        "tags" : ["BEST"]
                    },
                    {
                        "menuId" : "ObjectId4",
                        "menuName" : "페퍼로니 피자",
                        "price" : 9000,
                        "salePrice" : 8500,
                        "image" : "https://image.table-order.com/1_2.jpg",                        
                        "isSoldOut" : false,
                        "isOptionEnabled" : true,
                        "tags" : ["NEW"]
                    }
                ]
            },
            {
                "categoryId" : "ObjectId2",
                "categoryName" : "사이드",
                "menu" : [
                    {
                        "menuId" : "ObjectId5",
                        "menuName" : "치즈 오븐 스파게티",
                        "price" : 4000,
                        "image" : "https://image.table-order.com/1_3.jpg",                        
                        "isSoldOut" : false,
                        "isOptionEnabled" : false,
                        "tags" : ["BEST", "SPICY"]
                    }
                ]
            }
        ]
    }
    ```

## 상세 조회
- API : GET /menu/{menuId}
- Request
- Response
    ```json
    {
        "code" : 200,
        "message" : "",
        "data" : {
            "menuId" : "ObjectId4",
            "menuName" : "페퍼로니 피자",
            "description" : "짭짤한 페퍼로니가 들어간 피자",
            "price" : 9000,
            "salePrice" : 8500,
            "image" : "https://image.table-order.com/1_2.jpg",
            "tags" : ["NEW"],
            "options" : [
                {
                    "categoryId" : "ObjectId5",
                    "categoryName" : "토핑",
                    "isMultiple" : true,
                    "isRequired" : false,
                    "maxSelect" : 0, // 0은 수량 제한 없음.
                    "options" : [
                        {
                            "optionId" : "ObjectId6",
                            "optionName" : "치즈 추가",
                            "price" : 1000,
                            "image" : "",
                            "isSoldOut" : false,
                            "isOnlyOne" : false,
                            "tags" : ["BEST"]
                        },
                        {
                            "optionId" : "ObjectId7",
                            "optionName" : "페퍼로니 추가",
                            "price" : 1000,
                            "image" : "",
                            "isSoldOut" : false,
                            "isOnlyOne" : false,
                            "tags" : []
                        }
                    ]
                },
                {
                    "categoryId" : "ObjectId8",
                    "categoryName" : "엣지",
                    "isMultiple" : false,
                    "isRequired" : false,
                    "options" : [
                        {
                            "optionId" : "ObjectId9",
                            "optionName" : "치즈 크러스트 변경",
                            "price" : 2500,
                            "image" : "",
                            "isSoldOut" : false,
                            "isOnlyOne" : true,
                            "tags" : ["BEST"]
                        },
                        {
                            "optionId" : "ObjectId10",
                            "optionName" : "치즈 바이트 변경",
                            "price" : 3000,
                            "image" : "",
                            "isSoldOut" : false,
                            "isOnlyOne" : true,
                            "tags" : ["BEST"]
                        }
                    ]
                }
            ]
        }
    }
    ```

## 직원 호출 메뉴 조회
- API : GET /menu/call
- Request
- Response
    ```json
    {
        "code" : 200,
        "message" : "",
        "data" : [
            {
                "callId" : "ObjectId11",
                "callName" : "물"
            },
            {
                "callId" : "ObjectId12",
                "callName" : "젓가락"
            },
            {
                "callId" : "ObjectId13",
                "callName" : "호출"
            }
        ]
    }
    ```


# 3. Order

## 주문
- API : POST /order/table/{tableNum}
  - Request
      ```json
      [
          {
              "menuId" : "ObjectId4",
              "quantity" : 1,
              "price" : 8500,
              "options" : [
                  {
                      "optionId" : "ObjectId6",
                      "quantity" : 1,
                      "price" : 1000
                  },
                  {
                      "optionId" : "ObjectId7",
                      "quantity" : 2,
                      "price" : 1000
                  },
                  {
                      "optionId" : "ObjectId10",
                      "quantity" : 1,
                      "price" : 4000
                  }
              ]
          },
          {
              "menuId" : "ObjectId5",
              "quantity" : 2,
              "options" : [],
              "price" : 4000
          }
      ]
      ```
- Response
    ```json
    {
        "code" : 200,
        "message" : ""
    }
    ```
- Flow
    1. validation
    2. 테이블의 주문 내역 업데이트 (Redis, 테이블 초기화 시 삭제)
    3. 당일 기준 메뉴 및 옵션 판매 수량 업데이트 (Mongo) 
    4. 당일 매출 업데이트 (Redis, 24h)

## 주문 내역 조회
- API : GET /order/table/{tableNum}
- Request
  - Response
      ```json
      {
          "code" : 200,
          "message" : "",
          "data" : [
              {
                  "menuId" : "ObjectId4",
                  "quantity" : 1,
                  "price" : 8500,
                  "options" : [
                      {
                          "optionId" : "ObjectId6",
                          "quantity" : 1,
                          "price" : 1000
                      },
                      {
                          "optionId" : "ObjectId7",
                          "quantity" : 2,
                          "price" : 1000
                      },
                      {
                          "optionId" : "ObjectId10",
                          "quantity" : 1,
                          "price" : 4000
                      }
                  ]
              },
              {
                  "menuId" : "ObjectId5",
                  "quantity" : 2,
                  "options" : [],
                  "price" : 4000
              } 
          ] 
      }
      ```

## 직원 호출
- API : POST /order/table/{tableNum}/call
- Request
    ```json
    {
        "call" : ["ObjectId1", "ObjectId3"] 
    }
    ```
- Response
    ```json
    {
        "code" : 200,
        "message" : ""
    }
    ```
