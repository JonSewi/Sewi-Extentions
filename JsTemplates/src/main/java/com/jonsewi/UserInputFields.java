package com.jonsewi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bitwig.extension.callback.NoArgsCallback;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.DocumentState;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableEnumValue;
import com.bitwig.extension.controller.api.SettableStringValue;
import com.bitwig.extension.controller.api.Signal;

public class UserInputFields 
{
	List<String> modeSwitches = new ArrayList<String>(); 
	// 0 = ID insert mode, 1 = Browser insert mode
	
	List<Object> listOfIDs = new ArrayList<Object>(); 
	List<String> listOfIDTypes = new ArrayList<String>();
	
	private String[] onOffButtonList = {"On", "Off"}; 
	// Used as the text for the preference and settings buttons

	private ControllerHost host;
	private Preferences preferences;
	private DocumentState documentState;
	
	protected SettableEnumValue modeAutoInsert;
	protected SettableEnumValue modeBrowser;
	protected SettableStringValue templateTagField;

	protected ID_Field id_Field1;
	protected ID_Field id_Field2;
	protected ID_Field id_Field3;
	
	protected DeviceID deviceID1;
	protected DeviceID deviceID2;
	protected DeviceID deviceID3;
	
	protected Signal preferenceUpdateButton;

	public UserInputFields(ControllerHost host) 
	{
		this.host = host;
		preferences = host.getPreferences();
		documentState = host.getDocumentState();
	}
	
	public void createPrefModeFields() 
	{	//	Creates the buttons and template tag name in the Bitwig preferences 
		//		found at the top of the page, under 'Settings' -> 'Controllers'.
		//	Creates them in order of script execution, but groups them by category 
		//		which is the second argument in the methods.
		
		modeAutoInsert = preferences.getEnumSetting
				("Use Auto Insert", "Insert Functionality", onOffButtonList, onOffButtonList[0]);
  			modeAutoInsert.markInterested();
  		
  		modeBrowser = preferences.getEnumSetting
  				("Use Browser", "Insert Functionality", onOffButtonList, onOffButtonList[0]);
  			modeBrowser.markInterested();
  		
  		templateTagField = preferences.getStringSetting
  				("Template Tag Name", "Insert Functionality", 50, "Template");
  			templateTagField.markInterested();
  		
  		preferenceUpdateButton = preferences.getSignalSetting
  				("Set ID's", "Commit Changes", "GO!");
  			preferenceUpdateButton.addSignalObserver(updatePreferencesCallback());
  		
	}
	
	public void createPrefIdFields()
	{	// Creates separate ID_Field objects and DeviceID objects
		
		id_Field1 = new ID_Field(preferences, 1);
		id_Field2 = new ID_Field(preferences, 2);
		id_Field3 = new ID_Field(preferences, 3);
		
		deviceID1 = new DeviceID(1);
		deviceID2 = new DeviceID(2); 
		deviceID3 = new DeviceID(3); 
	}
	
	public void createDocumentFields()
	{	// TODO future feature
		
	}
	
	public NoArgsCallback updatePreferencesCallback()
	{	// 	A Type wrapper
		// 	The preference update button needed to use a NoArgsCallback type instead of a Runnable type
		
		return () -> {host.scheduleTask(updatePreferences(), 10); };
	}
	
	public Runnable updatePreferences()
	{	// 	Goes through all the buttons and input fields and puts their values in lists for easy access.
		//	Also prints out a handy list of what went into those lists in the Bitwig Console.
		
		return () -> 
		{
			deviceID1.set();
			deviceID2.set();
			deviceID3.set();
			
			if (! modeSwitches.isEmpty()){modeSwitches.clear();};
			if (! listOfIDs.isEmpty()){listOfIDs.clear();};
			if (! listOfIDTypes.isEmpty()){listOfIDTypes.clear();};
			
			modeSwitches.add(modeAutoInsert.get());
			modeSwitches.add(modeBrowser.get());
		
			listOfIDs.add(deviceID1.get());
			listOfIDs.add(deviceID2.get());
			listOfIDs.add(deviceID3.get());
			
			listOfIDTypes.add(deviceID1.deviceType);
			listOfIDTypes.add(deviceID2.deviceType);
			listOfIDTypes.add(deviceID3.deviceType);
			
			host.showPopupNotification("Preferences Updated");
			
			printer	(
						"\nPreferences set to:" +
						"\nAuto Insert " + modeSwitches.get(0) + ", Browser " + modeSwitches.get(1) + "." +
						"\nTemplate Tag is set to \"" + templateTagField.get() + "\"" +
						"\nDevice 1 is a " + listOfIDTypes.get(0) + " device with ID: " + listOfIDs.get(0) + 
						"\nDevice 2 is a " + listOfIDTypes.get(1) + " device with ID: " + listOfIDs.get(1) + 
						"\nDevice 3 is a " + listOfIDTypes.get(2) + " device with ID: " + listOfIDs.get(2)
					);
		};
	}
	
	
	
	public class ID_Field 
	{	//	Creates the input field and value getter for the ID in the preference page
		
		private SettableStringValue fieldString;
		
		public ID_Field(Preferences preferences, int fieldNum) 
		{  		
			fieldString = preferences.getStringSetting 	("ID " + fieldNum, "Device " + fieldNum, 50, "paste ID here");
			preferences.getStringSetting 				("Optional Note", "Device " + fieldNum, 50, "eg: Device Name");
			this.fieldString.markInterested();
		}
		
		public SettableStringValue get() {return(fieldString);}
	}
	
	public class DeviceID 
	{	//	Reads the ID_Field and turns its value into the correct ID type.
		//	Values are set only after script initialization and by user input.
		
		Integer deviceNum;
		Integer idLength;
		String deviceType;
		Integer vst2ID;
		String vst3ID;
		UUID bitwigID;
		SettableStringValue idField;
		
		public DeviceID(int i)
		{
			this.deviceNum = i;
		}
		
		public void set()
		{	// Automatically detects what type of device ID is put in the field 
		  	// by string length and sets the matching ID variable.
		  	// Reference examples: 
		  	// 
		  	// 		VST 2 ID: 		1400128611 (10 characters)
		  	// 		VST 3 ID: 		565354436D36626261627920636F6D65 (32 characters)	
		  	// 		BITWIG UUID: 	d275f9a6-0e4a-409c-9dc4-d74af90bc7ae (36 characters)

			switch (deviceNum) 
			{
				case 1: idField = id_Field1.get(); break;
				case 2: idField = id_Field2.get(); break;
				case 3: idField = id_Field3.get(); break;
			}
			
			int idLength = idField.get().length();
			
			switch (idLength) {
			case 10: this.vst2ID = Integer.parseInt(idField.get()); 
			this.deviceType = "VST2";
			break;
				
			case 32: this.vst3ID = idField.get(); 
			this.deviceType = "VST3";
			break;
				
			case 36: this.bitwigID = UUID.fromString(idField.get()); 
			this.deviceType = "Bitwig";
			break;
			
			case 0: this.deviceType = "Empty";
			break;
				
  			default: 
  			this.deviceType = "Non-existent"; 
  			host.showPopupNotification("Can't recognize the device type. Check the log.");
  			// 	TODO future update will do more exception catching. 
  			//	Initial value and empty value should not report this message.
			}
		}
		
		public Object get()
		{
			switch (deviceType) 
			{
				case "VST2": return(vst2ID);
				case "VST3": return(vst3ID);
				case "Bitwig": return(bitwigID);
				default: return(null); 
			}
		}
	}
	
	//	Shorthand method to make debugging easier.
	protected void printer(String s) {host.println(s); java.lang.System.out.println(s);}

}
