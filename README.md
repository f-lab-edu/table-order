# table-order
테이블 오더 시스템

## 개발 이슈.
### 2025.05.03
1. store_1.json 형태로 DB에 저장 시 h2 (RDB)로 구현 했을 때 Entity에서 역직렬화 시 문제가 있음.  
    => save할 때 StoreMapper에서 @AfterMapping로 하위 테이블?에서 상위 테이블 set하도록 함.   
    => Entity에서 OneToMany = @JsonManagedReference, ManyToOne = @JsonBackReference로 처리했는데 더 좋은 방식이 있을까
2. 위 이슈가 발생한 이유는 RDB를 쓰면 STORE, MENU 테이블이 따로 있기 때문에 menuId로만 바로 조회 가능하지만,  
    NoSQL을 쓰면 store별로 Collection을 나누는게 효율적일 거 같아 menu 조회할 때 storeId를 필수 입력하려고 해서 발생
3. getAllMenu 할 때 쿼리 실행을 많이 하게 되는데 어떻게 해야 효율적으로 조회할지
### 2025.05.15
1. NoSQL의 Documents 저장 방식을 사용할 때 MenuCategory, Menu, OptionCategory, Option의 정규화를 어떻게 하는게 나을지
    => MenuCategory, OptionCategory를 Category로 합치고 isOption 필드를 추가 (2025.05.17)
### 2025.05.18
1. 어디에서 어디까지 Redis에 저장하는게 나을지
### 2025.05.28
1. Redis에 List type 저장할 때 Deserialize 오류 발생  
    => RedisTemplate의 generic 명시  
    ```java
    private <T> RedisTemplate<String, T> getDefaultRedisTemplate(Class<T> cls, RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, T> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());



        return template;
    }

    @Bean
    public RedisTemplate<String, MenuCategoryDTO> menuRedisTemplate(RedisConnectionFactory connectionFactory) {
        return getDefaultRedisTemplate(MenuCategoryDTO.class, connectionFactory);
    }
    ```
    => @Cacheable로 간단한 캐싱에선 사용이 불가능하며, 타입이 늘어날 때마다 함수를 만들어야 해서 비효율적  
    => RedisCacheManager + 자체 List Serialzizer Class로 해결