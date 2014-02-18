package com.cirrus.osgi.extension;

public interface ICirrusStorageService {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    String SERVICE_NAME_PROPERTY = "Storage-Service-Name";
    String SERVICE_VERSION_PROPERTY = "Storage-Service-Version";
    String SERVICE_VENDOR_PROPERTY = "Storage-Service-Vendor";
    String SERVICE_CLASS_PROPERTY = "Storage-Service-Class";

    long getAvailableDiskSpace();
}
