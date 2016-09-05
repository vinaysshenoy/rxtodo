# rxtodo(WIP)
Sample application to demonstrate the use of MVP with RXJava

### Intro
This is based on the MVP architecture design used on the Android Testing Codelab [guide](https://codelabs.developers.google.com/codelabs/android-testing/index.html#0). One major change from that design, however, was inverting the flow of data from the `View` and `Presenter`, i.e, instead of the `View` and `Presenter` calling methods on each other, they observe each other for events and react accordingly. [RxJava](https://github.com/ReactiveX/RxJava) is used to create `Observable` objects and handle threading.

### Goals
- Separate threading from View, i.e., `View` objects only know the Main thread and don't have any concept of background threading. `Presenter` objects don't know anything about the Main thread, but can choose to run events on background threads as and when required.
- Nothing, and **absolutely NOTHING** from the android SDK packages is allowed to be present in `Presenter` objects. This allows us to test `Presenter` objects using pure JUnit Tests, and then test the rest in Android Tests.
- Build a simple architecture(some boilerplate, but no magic), that is scalable and expandable as the complexity of the project increases.
