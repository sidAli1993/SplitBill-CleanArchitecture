package com.poc.paphoscafe;

import android.app.Application;

public class App extends Application {
    private static App app;

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }
}
