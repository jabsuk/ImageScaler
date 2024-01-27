package com.juanageitos.imagescalerfx.threads;

import com.juanageitos.imagescalerfx.adapters.ImagesAdapter;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.concurrent.ThreadPoolExecutor;
/**
 * Reload txt message while executor is running
 * @author juani
 * @version 1.0.0
 **/
public class ImagesServiceLogger extends ScheduledService<Boolean> {
    /**
     * Create task that updates message
     * @return
     */
    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws InterruptedException {
                ThreadPoolExecutor executor = ImagesAdapter.getExecutor();
                updateMessage(executor.getCompletedTaskCount() + " of " + executor.getTaskCount() + " tasks finished.");
                return executor.getCompletedTaskCount() == executor.getTaskCount();
            }
        };
    }
}