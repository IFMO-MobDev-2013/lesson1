package ru.zulyaev.ifmo.android;

public class FairTaskScreen extends BaseActivity {
    @Override
    protected DrawingView makeDrawingView() {
        return new DrawingView(this, false, false);
    }
}