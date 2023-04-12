package org.jorigin.device;

import org.jorigin.Common;

/**
 * A class that describe information related to a manufactured device.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.14
 */
public class SimpleDeviceInfo implements DeviceInfo{

	/**
	 * The model of the device.
	 */
	private String model = null;
	
	/**
	 * The manufacturer of the device.
	 */
	private String manufacturer = null;
	
	/**
	 * The serial number of the device.
	 */
	private String serialNumber = null;
	
	/**
	 * An URL that point the specification of the device.
	 */
	private String specificationURL = null;
	
	/**
	 * Create a new simple device info.
	 * @param model the model of the device
	 * @param manufacturer the manufacturer of the device
	 * @param serialNumber the serial number of the device
	 * @param specificationURL an URL that point the specification of the device
	 */
	public SimpleDeviceInfo(String model, String manufacturer, String serialNumber, String specificationURL) {
		this.model = model;
		this.manufacturer = manufacturer;
		this.serialNumber = serialNumber;
		this.specificationURL = specificationURL;
	}
	
	@Override
	public String getDeviceModel() {
		return model;
	}

	@Override
	public String getDeviceManufacturer() {
		return manufacturer;
	}

	@Override
	public String getDeviceSerialNumber() {
		return serialNumber;
	}

	@Override
	public String getDeviceSpecificationURL() {
		return specificationURL;
	}

}
