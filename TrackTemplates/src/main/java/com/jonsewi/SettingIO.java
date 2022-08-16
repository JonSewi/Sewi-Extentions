package com.jonsewi;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.DocumentState;
import com.bitwig.extension.controller.api.EnumValue;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableStringValue;

public class SettingIO {

	private DocumentState documentState;
	private Preferences preferences;

	public SettingIO(DocumentState documentState, Preferences preferences, ControllerHost host) {
		
		this.documentState = documentState;
		this.preferences = preferences;
		
		// TODO Auto-generated constructor stub
		
		SettableStringValue fieldUUID_1 = preferences.getStringSetting("UUID", "Device 1", 40, "Paste UUID Here");
	    SettableStringValue fieldNote_1 = preferences.getStringSetting("(optional) Note", "Device 1", 40, "eg: Type Device Name Here");
	    fieldUUID_1.markInterested();
	    String templateID_1 = fieldUUID_1.get();
	    
		SettableStringValue fieldUUID_2 = preferences.getStringSetting("UUID", "Device 2", 40, "Paste UUID Here");
	    SettableStringValue fieldNote_2 = preferences.getStringSetting("(optional) Note", "Device 2", 40, "eg: Type Device Name Here");
	    fieldUUID_2.markInterested();
	    String templateID_2 = fieldUUID_2.get();
	    
		SettableStringValue fieldUUID_3 = preferences.getStringSetting("UUID", "Device 3", 40, "Paste UUID Here");
	    SettableStringValue fieldNote_3 = preferences.getStringSetting("(optional) Note", "Device 3", 40, "eg: Type Device Name Here");
	    fieldUUID_3.markInterested();
	    String templateID_3 = fieldUUID_3.get();
	    
	    // Testing
	    host.println(templateID_1);
	    host.println(templateID_2);
	    host.println(templateID_3);
	}

	
	

}