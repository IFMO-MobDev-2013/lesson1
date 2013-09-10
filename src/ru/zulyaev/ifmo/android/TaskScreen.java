package ru.zulyaev.ifmo.android;

public class TaskScreen extends BaseActivity {
    @Override
    protected DrawingView makeDrawingView() {
        return new DrawingView(this, false, true);
    }
}