package Observer;

public interface MyObservable {
    void notifyObservers(ObserverEnum observerEnum, Object o);
    void addObserver(MyObserver observer);
    void removeObserver(MyObserver observer);
}
