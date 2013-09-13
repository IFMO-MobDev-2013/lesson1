package ru.zulyaev.ifmo.android;

public class FairFullScreen extends BaseActivity {
    @Override
    protected DrawingView makeDrawingView() {
        return new DrawingView(this, true, false);
    }
}