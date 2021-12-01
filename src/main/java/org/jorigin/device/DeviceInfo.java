package org.jorigin.device;

import org.jorigin.Common;

/**
 * An interface that describe information related to a manufactured device.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.14
 */
public interface DeviceInfo {

	/**
	 * Get the model of the device.
	 * @return the model of the device
	 */
	public String getDeviceModel();
	
	/**
	 * Get the manufacturer of the device.
	 * @return the manufacturer of the device
	 */
	public String getDeviceManufacturer();
	
	/**
	 * Get the serial number of the device.
	 * @return the serial number of the device
	 */
	public String getDeviceSerialNumber();

	/**
	 * Get an URL that points a specification of the device.
	 * @return an URL that points a specification of the device
	 */
	public String getDeviceSpecificationURL();
	
}
