# part0-chapter3-2

- UI 를 그리는 기본 구성요소
- CustomView 를 만들기 위함

### [How Android Draws View](https://developer.android.com/guide/topics/ui/how-android-draws)

![Untitled](https://user-images.githubusercontent.com/24618293/204136897-70a64a1d-54e5-4fd1-bef5-251492733a80.png)

- 전위순회 방식을 쓰기 때문에, 부모 부터 자식 뷰 순서로 그려지게 됨

- measure
    - 뷰의 크기를 계산
    - 모든 뷰는 각각 자신의 width, height 를 계산
    - measure 과정에서, 부모 - 자식 뷰간의 크기 정보 전달을 위해 2가지 클래스 사용
        - ViewGroup.LayoutParams : 자식 뷰가 부모 뷰에게 자신이 어떻게 측정되고 위치를 정할지 요청 할 때 사용, (how big)
            - DP, PX.. : 자식뷰가 원하는 사이즈
            - MATCH_PARENT : 부모 뷰 사이즈와 똑같이 자식뷰 사이즈 지정
            - WRAP_CONTENT : 부모 뷰 안에서, content 를 표현할 수 있는 fit 한 사이즈 지정
        - ViewGroup.MeasureSpecs : 부모 뷰가 자식 뷰에게 요구사항을 전달할 때 사용
            - UNSPECIFIED : 부모 뷰는 자식 뷰가 원하는 사이즈로 결정
            - EXACTLY : 부모 뷰가 자식 뷰의 사이즈를 정확히 지정할 때
            - AT_MOST : 부모 뷰가 자식 뷰의 최대 사이즈를 지정할 때
- layout
    - 뷰의 크기와 위치를 할당
    - 부모기준의 상대적 위치 (left, top, right, bottom) 을 계산
- draw
    - 뷰를 그리는 단계
        - Canvas : 뷰의 모양을 그리는 객체
        - Paint : 뷰의 색상을 칠하는 객체
    - measure, layout 에서 측정한 크기와, 계산한 위치에 뷰를 그림
    - 이 콜백은 언제든 다시 호출 될 수 있음
        - scroll 이나 swipe 를 하게 되면 뷰는 onDraw 다시 호출
        - 객체 할당과 같이 리소스가 많이 소모되는 로직은 추가하지 말 것

- ViewUpdate : 런타임에 뷰를 다시 그리게 하는 함수
    - invalidate : view 에 변화가 생겨서 다시 그려야 할 때
        - color 변화 등
    - requestLayout : view 를 처음부터 그려야 할 때
        - 크기가 변화해서 measure 부터 다시 해야할 때
