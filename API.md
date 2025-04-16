# 고민 사항
1. 일단 storeId, tableId, apiKey 등의 정보는 테이블 오더 설치 시 서버에서 ID를 받아 파일에 저장했다고 가정.  
2. 주문 내역은 API 호출로 가져오는 것이 나을지 Front에서 갖고 있는 것이 나을지  
3. 선 결제 방식일 땐 테이블의 주문 내역 clear는 어떻게 하는게 나은가? => POS에 테이블을 똑같이 보여주니까 직원이 clear하는 걸로?

# header
```
Content-Type: application/json
Authorization: Basic {{apiKey}}
```
# 에러 Reposne
```json
{
    "code" : "오류 코드",
    "message" : "오류 내용"
}
```
# 1. Store



# 2. Menu

## 메뉴 조회
- API : GET /menu/{{storeId}}
- Request 
- Response
    ```json
    {
        "code" : "01",
        "message" : "",
        "data" : [
            {
                "categoryId" : 1,
                "categoryName" : "피자",
                "menu" : [
                    {
                        "menuId" : 1,
                        "name" : "치즈 피자",
                        "price" : 8000,
                        "image" : "https://image.table-order.com/1_1.jpg",
                        "recommend" : true,
                        "sale" : false,
                        "soldOut" : false,
                        "option" : true,
                    },
                    {
                        "menuId" : 2,
                        "name" : "페퍼로니 피자",
                        "price" : 9000,
                        "image" : "https://image.table-order.com/1_2.jpg",
                        "recommend" : true,
                        "sale" : true,
                        "salePrice" : 8500,
                        "soldOut" : false,
                        "option" : true,
                    },
                ]
            },
            {
                "categoryId" : 2,
                "categoryName" : "사이드",
                "menu" : [
                    {
                        "menuId" : 3,
                        "name" : "치즈 오븐 스파게티",
                        "price" : 4000,
                        "image" : "https://image.table-order.com/1_3.jpg",
                        "recommend" : false,
                        "sale" : false,
                        "soldOut" : false,
                        "option" : false,
                    },
                ]
            }
        ]
    }
    ```

## 옵션 조회
- API : GET /menu/{{storeId}}/option/{{menuId}}
- Request 
- Response
    ```json
    {
        "code" : "01",
        "message" : "",
        "data" : [
            {
                "categoryId" : 1,
                "categoryName" : "토핑",
                "multiple" : true,
                "maxSelect" : 0, // 0은 수량 제한 없음.
                "options" : [
                    {
                        "optionId" : 1,
                        "name" : "치즈 추가",
                        "price" : 1000,
                        "image" : "",
                        "recommend" : true,
                        "sale" : false,
                        "soldOut" : false,
                        "onlyOne" : false,
                    },
                    {
                        "optionId" : 2,
                        "name" : "페퍼로니 추가",
                        "price" : 1000,
                        "image" : "",
                        "recommend" : true,
                        "sale" : false,
                        "soldOut" : false,
                        "onlyOne" : false,
                    },
                ]
            },
            {
                "categoryId" : 2,
                "categoryName" : "엣지",
                "multiple" : false,
                "options" : [
                    {
                        "optionId" : 3,
                        "name" : "치즈 크러스트 변경",
                        "price" : 2500,
                        "image" : "",
                        "recommend" : true,
                        "sale" : false,
                        "soldOut" : false,
                        "onlyOne" : true,
                    },
                    {
                        "optionId" : 4,
                        "name" : "치즈 바이트 변경",
                        "price" : 3000,
                        "image" : "",
                        "recommend" : false,
                        "sale" : false,
                        "soldOut" : false,
                        "onlyOne" : true,
                    },
                ]
            },
        ]
    }
    ```

## 직원 호출 메뉴 조회
- API : GET /menu/{{storeId}}/call


# 3. Order

## 주문
- API : POST /order/{{storeId}}/table/{{tableId}}
- Request 
    ```json
    {
        "totalPrice" : 18500, // 백엔드에서 호출 되기 전에 데이터가 바뀔 수도 있어서 고객이 화면에서 결제할 당시 금액으로
        "menu" : [
            {
                "menuId" : 2,
                "quantity" : 1,
                "options" : [
                    {
                        "optionId" : 1,
                        "quantity" : 1
                    },
                    {
                        "optionId" : 2,
                        "quantity" : 2
                    },
                    {
                        "optionId" : 4,
                        "quantity" : 1
                    },
                ]
            },
            {
                "menuId" : 3,
                "quantity" : 2,
                "options" : []
            }
        ]
    }
    ```
- Response
    ```json
        {
            "code" : "01",
            "message" : "",
        }
    ```

## 직원 호출
- API : POST /order/{{storeId}}/table/{{tableId}}/call