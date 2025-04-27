# 고려 사항
1. 일단 storeId, tableId, apiKey 등의 정보는 테이블 오더 설치 시 서버에서 ID를 받아 파일에 저장했다고 가정.
    => storeId는 apiKey로 추출하는 방식이 나을지, parameter로 주는게 나을지
    => 일단 apiKey로 추출하는 방식 채택 (보안이나 효율성이 더 나을 것 같음)
2. 주문 내역은 API 호출로 가져오는 것이 나을지 Front에서 갖고 있는 것이 나을지  
3. 선 결제 방식일 땐 테이블의 주문 내역 clear는 어떻게 하는게 나은가? => POS에 테이블을 똑같이 보여주니까 직원이 clear하는 걸로?

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
- API : PATCH /menu/table/{tableId}/language
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
        "message" : "",
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
                "categoryId" : 1,
                "categoryName" : "피자",
                "menu" : [
                    {
                        "menuId" : 1,
                        "menuName" : "치즈 피자",
                        "price" : 8000,
                        "image" : "https://image.table-order.com/1_1.jpg",                        
                        "isSoldOut" : false,
                        "hasOption" : true,
                        "tags" : ["BEST"]
                    },
                    {
                        "menuId" : 2,
                        "menuName" : "페퍼로니 피자",
                        "price" : 9000,
                        "salePrice" : 8500,
                        "image" : "https://image.table-order.com/1_2.jpg",                        
                        "isSoldOut" : false,
                        "hasOption" : true,
                        "tags" : ["NEW"]
                    },
                ]
            },
            {
                "categoryId" : 2,
                "categoryName" : "사이드",
                "menu" : [
                    {
                        "menuId" : 3,
                        "menuName" : "치즈 오븐 스파게티",
                        "price" : 4000,
                        "image" : "https://image.table-order.com/1_3.jpg",                        
                        "isSoldOut" : false,
                        "hasOption" : false,
                        "tags" : ["BEST", "SPICY"]
                    },
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
            "menuId" : 2,
            "menuName" : "페퍼로니 피자",
            "description" : "짭짤한 페퍼로니가 들어간 피자",
            "price" : 9000,
            "salePrice" : 8500,
            "image" : "https://image.table-order.com/1_2.jpg",
            "tags" : ["NEW"],
            "options" : [
                {
                    "categoryId" : 1,
                    "categoryName" : "토핑",
                    "isMultiple" : true,
                    "maxSelect" : 0, // 0은 수량 제한 없음.
                    "options" : [
                        {
                            "optionId" : 1,
                            "optionName" : "치즈 추가",
                            "price" : 1000,
                            "image" : "",
                            "isSoldOut" : false,
                            "isOnlyOne" : false,
                            "tags" : ["BEST"]
                        },
                        {
                            "optionId" : 2,
                            "optionName" : "페퍼로니 추가",
                            "price" : 1000,
                            "image" : "",
                            "isSoldOut" : false,
                            "isOnlyOne" : false,
                            "tags" : []
                        },
                    ]
                },
                {
                    "categoryId" : 2,
                    "categoryName" : "엣지",
                    "isMultiple" : false,
                    "options" : [
                        {
                            "optionId" : 3,
                            "optionName" : "치즈 크러스트 변경",
                            "price" : 2500,
                            "image" : "",
                            "isSoldOut" : false,
                            "isOnlyOne" : true,
                            "tags" : ["BEST"]
                        },
                        {
                            "optionId" : 4,
                            "optionName" : "치즈 바이트 변경",
                            "price" : 3000,
                            "image" : "",
                            "isSoldOut" : false,
                            "isOnlyOne" : true,
                            "tags" : ["BEST"]
                        },
                    ]
                },
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
                "callId" : 1,
                "callName" : "물"
            },
            {
                "callId" : 2,
                "callName" : "젓가락"
            },
            {
                "callId" : 3,
                "callName" : "호출"
            },
        ]
    }
    ```


# 3. Order

## 주문
- API : POST /order/table/{tableId}
- Request 
    ```json
    {      
        // API 호출 되기 전에 데이터가 바뀔 수도 있어서 고객이 화면에서 결제할 당시 금액으로  
        "menu" : [
            {
                "menuId" : 2,
                "quantity" : 1,
                "price" : 8500,
                "options" : [
                    {
                        "optionId" : 1,
                        "quantity" : 1,
                        "price" : 1000
                    },
                    {
                        "optionId" : 2,
                        "quantity" : 2,
                        "price" : 1000
                    },
                    {
                        "optionId" : 4,
                        "quantity" : 1,
                        "price" : 4000
                    },
                ]
            },
            {
                "menuId" : 3,
                "quantity" : 2,
                "options" : [],
                "price" : 4000
            }
        ]
    }
    ```
- Response
    ```json
    {
        "code" : 200,
        "message" : "",
    }
    ```

## 직원 호출
- API : POST /order/table/{tableId}/call
- Request 
    ```json
    {
        "call" : [1, 3] 
    }
    ```
- Response
    ```json
    {
        "code" : 200,
        "message" : "",
    }
    ```