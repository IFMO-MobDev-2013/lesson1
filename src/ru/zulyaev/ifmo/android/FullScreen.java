package ru.zulyaev.ifmo.android;

public class FullScreen extends BaseActivity {
    @Override
    protected DrawingView makeDrawingView() {
        return new DrawingView(this, true, true);
    }
}