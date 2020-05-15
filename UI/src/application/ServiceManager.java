package application;

import javafx.concurrent.Task;

public class ServiceManager extends Task { //zatím nic nedělá
    @Override
    protected Object call() throws Exception {
        while(!this.isCancelled()){
        }
        return null;
    }

}
