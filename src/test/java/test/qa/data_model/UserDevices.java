package test.qa.data_model;

import java.util.List;

public class UserDevices {
    private String name;
    private int totalDevices;
    private List<Device> devices;

    public String getName() {
        return name;
    }

    public int getTotalDevices() {
        return totalDevices;
    }

    public List<Device> getDevices() {
        return devices;
    }


    public static class Device {
        private String platform;
        private List<String> models;


        public String getPlatform() {
            return platform;
        }


        public List<String> getModels() {
            return models;
        }

    }
}
