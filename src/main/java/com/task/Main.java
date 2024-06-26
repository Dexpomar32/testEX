package com.task;

import com.task.Controller.StoreController;
import com.task.View.StoreView;

public class Main {
    public static void main(String[] args) {
        StoreView view = new StoreView();
        StoreController controller = new StoreController(view);
        controller.start();
    }
}
