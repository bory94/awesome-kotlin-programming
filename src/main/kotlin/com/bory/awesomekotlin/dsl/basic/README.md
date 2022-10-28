## Kotlin DSL Basics

Kotlin DSL 을 작성하기 위해 필요한 기본 개념들에 대해 설명한다.

이 패키지에는 다음 두 개 Kotlin 파일이 존재한다.

- [SimpleReservation.kt](./SimpleReservation.kt): 예약 데이터를 위한 Kotlin Data Class.
- [SimpleReservationManager.kt](./SimpleReservationManager.kt): 예약을 처리해주는 매니저 클래스. 이 클래스에는 다음 두 가지
  함수가 정의되어 있다.
    - reserve 함수: SimpleReservation 인스턴스를 생성할 때 DSL 을 이용할 수 있도록 정의된 함수.
    - main 함수: DSL 을 이용하는 예제 함수

---

### 사전 지식

- 1급 객체인 Kotlin 함수
- Type Inference
- 수신자 타입<sub>Reciever Type</sub> 함수
- Lambda와 그에 대한 Syntactic Sugar

> 함수, 수신자 타입 함수, Lambda 등에 대한
> 내용은 [High-order functions and lambdas](https://kotlinlang.org/docs/lambdas.html)를 참고하라.
>
> Type Inference에 대한 내용은 [Type Inference](https://kotlinlang.org/spec/type-inference.html)을 참고하라.

### SimpleReservation 클래스

SimpleReservation 클래스는 Kotlin Data Class 로 예약 정보를 표현하는 DTO 이다.

```kotlin
data class SimpleReservation(
    var id: UUID = UUID.randomUUID(),
    var customerName: String = "",
    var numberOfAccompanies: Int = 0,
    var reservationTime: LocalDateTime = LocalDateTime.now()
) {
    fun validateSelf() {
        if (customerName.isEmpty()) throw IllegalArgumentException("Name Cannot be Empty")
        if (numberOfAccompanies < 1) throw IllegalArgumentException("Number of Accompanies should be positive")
        if (reservationTime < LocalDateTime.now()) throw IllegalArgumentException("Reservation Time should be after now")
    }
}
```

특별할 것 없는 일반 DTO 이지만 두 가지 주목할 점이 있다.

- 모든 프러퍼티가 var로 지정되어 있다. (가변이다.)
- 모든 프러퍼티에 기본값이 설정되어 있다.

이 두 가지 모두 DSL 을 원활히 이용하기 위한 기법이며, 이에 대해 다음 절에서 상세히 설명하겠다.

> 하지만 일반적으로 DTO 를 불변으로 지정하는 것을 생각하면 그다지 바람직해 보이지 않는다.
>
> 또 모든 프러퍼티에 굳이 기본값을 지정해야만 하는 것도 마음에 들지 않는다.
> (아래에서 언급하겠지만 이것은 필수사항은 아니다.)

---

### SimpleReservationManager 클래스

예약 정보를 받아 SimpleReservation 인스턴스를 생성해주는 클래스이다.

```kotlin
class SimpleReservationManager {
    infix fun reserve(initialize: SimpleReservation.() -> Unit) =
        SimpleReservation().apply {
            initialize()
            validateSelf()
        }
}
```

reserve 함수를 통해 SimpleReservation 인스턴스를 생성할 수 있는데 이 함수의 매개변수를 주목할 필요가 있다.

수신자 타입<sub>Receiver Type</sub> 함수는 확장 함수<sub>Extension Function</sub>과 마찬가지로 함수 블럭 내에서 수신자 Type을
this로 접근할 수 있다.

또, reserve 함수 내부에서도 주목할 내용이 있는데, 여기서 SimpleReservation() 객체를 생성한 뒤 매개변수로 전달된 initialize 함수를 호출하고 있다.

여기서 initialize() 함수는 당연히 this.initialize()이며, 여기서의 this는 (수신자 타입 함수의 정의대로) SimpleReservation 인스턴스이다.

> SimpleReservation Data Class 의 모든 프러퍼티에 기본값이 할당되었던 이유가 여기에 있다. 일단 데이터 없이 SimpleReservation 객체를 생성한
> 뒤, initialize 함수를 이용하여 값을 할당해야 하기 때문에 모두 기본값이 할당되어 있던 것이다.
>
> 사실 꼭 기본값을 지정해야만 하는 것은 아니지만 그렇지 않은 경우 다음과 같이 SimpleReservation 생성 코드가 지저분해진다.
> ```kotlin
> // 프러퍼티데 기본값이 지정되어 있지 않다면 다음과 같이 의미없는 초기화 데이터를 입력해야 한다.
> SimpleReservation("", 0, LocalDateTime.now()).apply {
>   initialize()
>   validateSelf()
> }
> ```

수신자 타입 함수를 매개변수로 사용했기 때문에 다음과 같이 Lambda 표현식을 함수의 매개변수로 전달할 수 있다.

```kotlin
// main 함수 내에서 사용한 reserve 함수
// 이해를 돕기 위해 입력값들이 하드코딩되어 있다. 
val reservation = SimpleReservationManager() reserve {
    customerName = "Jane Doe"
    numberOfAccompanies = 2
    reservationTime = LocalDateTime.of(2022, 11, 1, 17, 0, 30)
}
```

이 코드에 대해 주목할 부분은 세 가지이다.

- reserve 함수의 최종 매개변수로 Lambda 표현식이 이용되었다.
    - 함수 뒤에 괄호 없이 바로 Lambda 중괄호 표현식 사용
- Lambda 표현식 내부에서 SimpleReservation 인스턴스의 프러퍼티 값이 할당되었다.
    - 이 이유로 SimpleReservation Data Class 의 모든 프러퍼티들이 var로 지정되었다.
- 수신자 타입 함수에 대한 Type Inference 적용
    - reserve 함수 뒤에 타입에 대한 정의가 없는 Lambda 식이 나와도 SimpleReservation.() -> Unit 임을 알 수 있다.

(infix 함수 표현이 사용되었지만 이는 그리 중요한 부분은 아니다.)

이렇게 객체 생성이나 업무 흐름을 지정된 특정 방식으로 정의하는 것을 DSL<sub>Domain Specific Language</sub>라고 한다.

---

### 그런데 굳이 왜 DSL을?

사실 SimpleReservation 인스턴스를 생성하는 데에 굳이 DSL을 사용할 필요는 없다.
다음과 같이 생성자를 이용하여 훨씬 간단하게 인스턴스를 생성할 수 있다.

```kotlin
val reservation = SimpleReservation(
    customerName = "Jane Doe",
    numberOfAccompanies = 2,
    reservationTime = LocalDateTime.of(2022, 11, 1, 17, 0, 30)
)
```

그리고 DSL 형식을 이용하기 위해 SimpleReservation DTO 클래스는 불변을 포기해야 했고 모든 변수에 기본값을 지정해야만 했다.

DSL 이 진정으로 필요한 상황은 언제일까? 그리고 이런 단점들을 없앨 수는 없을까?

이에 대해서는 [중급 수준 DSL](../intermediate/README.md)과 [Workflow Template](../advanced/README.md)을 통해 자세히
알아보기로 하자.

---

### References

- [High-order functions and lambdas](https://kotlinlang.org/docs/lambdas.html)
- [Type Inference](https://kotlinlang.org/spec/type-inference.html)