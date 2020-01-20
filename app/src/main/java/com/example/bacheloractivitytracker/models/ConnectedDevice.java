package com.example.bacheloractivitytracker.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import lombok.Getter;

@Getter
public class ConnectedDevice {

    @SerializedName("Response")
    private final ResponseInfo response;
    @SerializedName("Body")
    private final Body body;
    @SerializedName("Uri")
    private final String uri;
    @SerializedName("Method")
    private final String method;

    public ConnectedDevice(ResponseInfo response, Body body, String uri, String method) {
        this.response = response;
        this.body = body;
        this.uri = uri;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectedDevice)) return false;

        ConnectedDevice that = (ConnectedDevice) o;

        return body != null && body.serial.equals(that.body.serial);
    }


    @Override
    public int hashCode() {
        return Objects.hash(response, body, uri, method);
    }

    //-----------Body class-------------//
    @Getter
    public class Body {
        @SerializedName("DeviceInfo")
        private final DeviceInfo deviceInfo;
        @SerializedName("Connection")
        private final ConnectionInfo connection;
        @SerializedName("Serial")
        private final String serial;

        public Body(DeviceInfo deviceInfo, ConnectionInfo connection, String serial) {
            this.deviceInfo = deviceInfo;
            this.connection = connection;
            this.serial = serial;
        }

        @Getter
        public class DeviceInfo {
            public class ArrayModel {
                private final String name;
                private final String address;

                public ArrayModel(String name, String address) {
                    this.name = name;
                    this.address = address;
                }
            }

            @SerializedName("Mode")
            private final int mode;
            @SerializedName("SwVersion")
            private final String swVersion;

            private final String productName;
            private final String design;
            private final List<ArrayModel> addressInfo;
            private final String brandName;
            private final String manufacturerName;
            private final String hwCompatibilityId;
            private final String hw;
            private final String variant;
            private final String sw;
            private final String serial;
            private final String additionalVersionInfo;
            private final String pcbaSerial;
            @SerializedName("Serial")
            private final String connectionSerial;
            @SerializedName("Description")
            private final String description;
            private final String apiLevel;
            @SerializedName("Name")
            private final String name;

            public DeviceInfo(int mode, String swVersion, String productName, String design, List<ArrayModel> addressInfo, String brandName, String manufacturerName, String hwCompatibilityId, String hw, String variant, String sw, String serial, String additionalVersionInfo, String pcbaSerial, String connectionSerial, String description, String apiLevel, String name) {
                this.mode = mode;
                this.swVersion = swVersion;
                this.productName = productName;
                this.design = design;
                this.addressInfo = addressInfo;
                this.brandName = brandName;
                this.manufacturerName = manufacturerName;
                this.hwCompatibilityId = hwCompatibilityId;
                this.hw = hw;
                this.variant = variant;
                this.sw = sw;
                this.serial = serial;
                this.additionalVersionInfo = additionalVersionInfo;
                this.pcbaSerial = pcbaSerial;
                this.connectionSerial = connectionSerial;
                this.description = description;
                this.apiLevel = apiLevel;
                this.name = name;
            }
        }

        @Getter
        public class ConnectionInfo {
            @SerializedName("UUID")
            private final String uuid;

            @SerializedName("Type")
            private final String type;

            public ConnectionInfo(String uuid, String type) {
                this.uuid = uuid;
                this.type = type;
            }
        }
    }

    @Getter
    public class ResponseInfo {
        @SerializedName("Status")
        private final int status;

        public ResponseInfo(int status) {
            this.status = status;
        }
    }
}
