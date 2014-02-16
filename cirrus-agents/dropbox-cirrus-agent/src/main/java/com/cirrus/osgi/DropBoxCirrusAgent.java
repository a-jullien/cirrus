package com.cirrus.osgi;

import com.cirrus.agent.impl.AbstractCirrusAgent;
import com.cirrus.agent.impl.StorageServiceVendor;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.osgi.service.DropBoxStorageService;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

public class DropBoxCirrusAgent extends AbstractCirrusAgent {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final String DROP_BOX_AGENT_NAME = "DropBox Cirrus Agent";
    private static final String PRODUCT_NAME = "DropBox";
    private static final String PRODUCT_VERSION = "2.6.11";
    private static final String PRODUCT_VENDOR = "DropBox";

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public DropBoxCirrusAgent() {
        super(new StorageServiceVendor(PRODUCT_NAME, PRODUCT_VERSION, PRODUCT_VENDOR));
    }

    //==================================================================================================================
    // Override
    //==================================================================================================================
    @Override
    public void start(final BundleContext context) throws Exception {
        final Dictionary<String, Object> dictionary = new Hashtable<String, Object>();
        dictionary.put(ICirrusStorageService.NAME_PROPERTY, DROP_BOX_AGENT_NAME);
        context.registerService(ICirrusStorageService.class.getName(), new DropBoxStorageService(), dictionary);
    }
}
