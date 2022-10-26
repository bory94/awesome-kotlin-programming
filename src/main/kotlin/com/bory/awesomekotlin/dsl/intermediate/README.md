## 중급 수준 DSL

여기서는 좀 더 그럴듯한, 그리고 현실적인 Kotlin DSL 을 이용한 인스턴스 생성 방법에 대해 다룬다.

이 패키지에는 세 개의 Kotlin 파일이 존재하는데 각각 다음과 같은 역할을 한다.

- [IntermediateReservation.kt](IntermediateReservation.kt): 예약 정보 생성을 위한 두 개 클래스가 정의되어 있다.
    - IntermediateReservation: 예약 정보를 위한 DTO Data Class. 모든 프러퍼티가 불변으로 정의되어 있다.
    - IntermediateReservationDSL: 예약 정보 생성을 위한 DSL 전용 클래스. 수신자 타입 함수를 선언할 때 이 클래스를 이용한다.
- [IntermediateCustomer.kr](IntermediateCustomer.kt): 고객 정보 생성을 위한 두 개 클래스가 정의되어 있다.
    - IntermediateCustomer: 고객 정보를 위한 DTO Data Class. 모든 프러퍼티가 불변으로 정의되어 있으며 IntermediateReservation
      클래스에서 사용된다.
    - IntermediateCustomerDSL: 고객 정보 생성을 위한 DSL 전용 클래스. 수신자 타입 함수를 선언할 때 이 클래스를 사용한다.
- [IntermediateReservationManager.kr](IntermediateReservationManager.kt): 데이터를 받아 예약을 수행하는 클래스가 정의되어
  있다.

> 여기서 DSL 을 위해 전용 DSL 을 사용한 방식은 하나의 방식일 뿐 꼭 이렇게 해야만 하는 것은 아니다.
>
> 전용 DSL 클래스 없이도 충분히 불변 DTO 를 이용할 수 있으며, 아울러 DTO 의 생성자를 private 으로 지정하여 DSL 을 통하지 않고는 생성하지 못하도록 할 수도
> 있다.
>
> 그리고 굳이 별도의 Manager 가 예약을 받도록 했지만 IntermediateReservation 클래스 내에 Companion Object 함수를 두어 직접 생성하도록 할
> 수도 있다.

---

### 사전 지식

- [Kotlin DSL Basics](../basic/README.md)

---

### [IntermediateReservation.kr](./IntermediateReservation.kt)

위의 설명에서 알 수 있듯, 각 DTO 클래스 파일에는 DTO 클래스와 DSL 전용 클래스가 각각 정의되어 있다.

```kotlin
data class IntermediateReservation(
    val id: UUID,
    val reservationTime: LocalDateTime,
    val intermediateCustomer: IntermediateCustomer
) {
    fun validateSelf() {
        if (reservationTime < LocalDateTime.now()) throw IllegalArgumentException("Reservation Time should be after now")
        this.intermediateCustomer.validateSelf()
    }
}

data class IntermediateReservationDSL(
    val id: UUID = UUID.randomUUID(),
    var reservationTime: LocalDateTime = LocalDateTime.now(),
    var intermediateCustomerDsl: IntermediateCustomerDSL = IntermediateCustomerDSL()
) {
    fun toReservation() =
        IntermediateReservation(
            id = id,
            reservationTime = reservationTime,
            intermediateCustomer = intermediateCustomerDsl.toCustomer()
        )

    fun customer(initialize: IntermediateCustomerDSL.() -> Unit) {
        this.intermediateCustomerDsl = IntermediateCustomerDSL().apply(initialize)
    }
}
```

DTO 클래스는 일반적인 불변 DTO 클래스이며 모든 프러퍼티 값을 지정해야만 생성할 수 있도록 기본값을 지정하지 않았다.

> IntermediateReservation 클래스의 프러퍼티로 IntermediateCustomer 클래스가 포함되어 있음을 주목해야 한다.

이와는 반대로 DSL 클래스는 [Kotlin DSL Basics](../basic/README.md)에서 사용했던 DTO와 마찬가지로 모든 프러퍼티가 가편이고 기본값이 지정되어
있다.

또, DSL 클래스에는 IntermediateCustomer 프러퍼티르 설정을 위한 customer 함수가 포함되어 있는데, 이 함수는
IntermediateCustomerDSL.() -> Unit 타입의 수신자 타입 함수를 매개변수로 받고 있어 재차 DSL 을 통해 인스턴스 설정을 할 수 있도록 하였다.

---

### [IntermediateReservationManager.kt](./IntermediateReservationManager.kt)

IntermediateReservationManager 클래스는 reserve 함수를 이용하여 예약을 처리해 줄 수 있다.

이 함수는 DSL 을 이용하기 위해 DSL 전용 객체 타입의 수신자 타입 함수를 이용한다.

```kotlin
class IntermediateReservationManager {
    infix fun reserve(initialize: IntermediateReservationDSL.() -> Unit) =
        IntermediateReservationDSL().apply(initialize).toReservation().apply { validateSelf() }
}
```

> IntermediateReservation.validationSelf 함수를 어디서 호출할 것이냐는 것은 선택의 문제이다.
>
> 지금처럼 IntermediateReservationManager.reserve 함수에서 호출할 수도 있고,
> IntermediateReservationDSL.toReservation 함수에서 호출할 수도 있다.
> 물론 IntermediateReservation.init 에서도 호출할 수 있다.

이제 다음과 같은 DSL 형식으로 예약을 처리할 수 있다.

```kotlin
val reservation = IntermediateReservationManager() reserve {
    reservationTime = LocalDateTime.of(2023, 10, 15, 22, 0, 0)

    customer { // <-- 이 customer는 필드가 아니라 함수이다.
        name = "John Doe"
        numberOfAccompanies = 2
    }
}
```

여기서 IntermediateCustomer 정보를 설정하기 위해 IntermediateReservationDSL.customer 함수를 사용한 것을 알 수 있다.

함수지만 DSL 형식을 중첩하여 사용하였기 때문에 일반적인 인스턴스 생성 방식보다 훨씬 깔끔한 코드가 되었다.

---

### 생성자 기반 생성 방식과의 비교

일반적으로 IntermediateReservation 인스턴스를 생성한다면 다음과 같은 코드를 이용해야 한다.

```kotlin
val reservation2 = IntermediateReservation(
    id = UUID.randomUUID(),
    reservationTime = LocalDateTime.of(2023, 10, 15, 22, 0, 0),

    intermediateCustomer = IntermediateCustomer(
        name = "Jane Doe",
        numberOfAccompanies = 3
    )
)
reservation2.validateSelf() // <-- 생성하는 측에서 직접 validationSelf 메소드를 호출하여야 한다.
```

DSL 코드와 생성자 기반 생성 코드를 비교해보면 다음과 같은 장단점이 있다.

- DSL 방식이 좀 더 깔끔하다. 쉼표나 생성자명을 표기할 필요가 없고 필드명도 간결한 함수명으로 변경할 수 있다.
- 생성 과정에서 특정 비즈니스 로직을 수행하는 경우 (여기서는 validate) DSL 방식이 로직을 제어하기 더 편하다.
  > 물론 이 예제에서는 validateSelf 함수를 어디서 호출하냐에 따라 두 방식 간에 별 차이가 없을 수 있다.
  >
  > 하지만 이 예에 국한하지 않고 다양한 비즈니스 로직의 처리가 일정한 방식으로 제어되어야 한다면 DSL 방식이 더 바람직하다고 할 수 있다.
- 작성해야 할 코드의 양은 DSL 방식이 훨씬 많다. (거의 두 배)
  > 그래서, 일반적으로 프레임워크 설정이나 복잡한 워크플로우를 간단히 표현하기 위해 DSL을 사용한다.
  >
  > 사실 이런 식의 단순 객체 생성은 DSL 에 좋은 예는 아니다.

보다 실제적인 DSL 구현과 사용 방법에 대한 내용은 [Workflow Template](../workflow/README.md)를 통해 확인하길 바란다.