/* Bitwig Extension written by 
 * Jon Sewi (Jeremy Martin van der Waard)
 * 
 * Code in this package is licensed under CC-BY-SA. 
 * This means every fork or iteration will prominently credit my name and also be licensed under CC-BY-SA. 
 * Everyone is free to use this, but I enjoy knowing what it's being used in, so let me know!
 * 
 * Jonsewi.com */

package com.jonsewi;

import java.util.List;
import java.util.UUID;

import com.bitwig.extension.callback.IntegerValueChangedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.DeviceBank;
import com.bitwig.extension.controller.api.TrackBank;

public class JsTemplatesExtension extends ControllerExtension
{
   protected JsTemplatesExtension(final JsTemplatesExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }
   
   UserInputFields userInputFields; 
   //Class defining all the input fields in preferences (settings) and the document settings 
   BrowserHandler browserHandler; 
   //Class handling everything that browserMode does
   
   private ControllerHost host;
  
   private TrackBank trackBank;
   private CursorTrack cursorTrack;
   private DeviceBank deviceBank;

   private int triggerCount; 
   //used to prevent issues at startup


   @Override
   public void init()
   {   	//	Is run when the script is launched.
	   	//	It sets up the classes and then updates the preference page with a small delay (to make sure the preferences are loaded)
	   
	   	host = getHost();
       
	   	trackObservers();
	   	
	   	triggerCount = 0;
	 
		userInputFields = new UserInputFields(host);
		userInputFields.createPrefModeFields();
		userInputFields.createPrefIdFields();
		userInputFields.updatePreferences();
		   
		browserHandler = new BrowserHandler(host, cursorTrack);
		browserHandler.setupBrowser();
		
		host.scheduleTask(userInputFields.updatePreferences(), 200);
   }
   
   public void trackObservers()
   {	// Sets up the getters for the cursor track information and trigger for inserting the template.
	   
		trackBank = host.createMainTrackBank(1, 0, 0);
		trackBank.itemCount().markInterested();
		
		cursorTrack = host.createCursorTrack(0, 0); 
		cursorTrack.trackType().markInterested();
		
		deviceBank = cursorTrack.createDeviceBank(1);
		deviceBank.itemCount().markInterested();
		
		trackBank.itemCount().addValueObserver(checkTrackState(), 0);
   }
   
   private IntegerValueChangedCallback checkTrackState() 
   {	//	Increments triggercount, so the trigger can skip the first two changes in track numbers during initialisation. 
	   	//	Then checks if the track is empty. If so, it starts inserting devices using the chosen method.
	   	//	(also checks track type for future features)
	   
	   	return (value) -> 
	   	{
		   triggerCount ++;
		   boolean empty = (deviceBank.itemCount().get() == 0);
		   
		   if (empty && triggerCount > 2) 
		   {
			   String trackType = cursorTrack.trackType().get();
			   String templateTag = userInputFields.templateTagField.get();
			   
			   List<String> modeSwitches = userInputFields.modeSwitches;
			   if (modeSwitches.get(0) == "On") {insertWithIDs(trackType);}
			   if (modeSwitches.get(1) == "On") {browserHandler.insertWithBrowser(trackType, templateTag);}
		   }
	   	}; 
   }

   
   private void insertWithIDs(String trackType) 
   {	// 	Gets the list of device IDs and their types from the preference page (and document settings in the future)
	   	//	Then iterates through the lists and inserts the devices in order.
	   
	   	List<String> listOfIDTypes = userInputFields.listOfIDTypes;
	   	List<Object> listOfIDs = userInputFields.listOfIDs;
	   
	   	for (int i = 0; i < 3; i++) 
	   	{
	   		switch (listOfIDTypes.get(i)) 
	   		{
	   		case "VST2": cursorTrack.endOfDeviceChainInsertionPoint().insertVST2Device((int) listOfIDs.get(i));
			   break;
	   		case "VST3": cursorTrack.endOfDeviceChainInsertionPoint().insertVST3Device((String) listOfIDs.get(i));
			   break;
	   		case "Bitwig": cursorTrack.endOfDeviceChainInsertionPoint().insertBitwigDevice((UUID) listOfIDs.get(i));
			   break;
	   		case "Empty": 
	   			break;
	   		default: host.showPopupNotification("ID insert went wrong at Device " + i);
	   		}
	   	}
   }
   
   public void getDocumentOverrides()
   {	//TODO future feature
	   
   }


   public void cleanup()
   {	//TODO future feature
	 
   }
   

   @Override
   public void exit()
   {

   }

   @Override
   public void flush()
   {
	   
   }
   
//	Shorthand method to make debugging easier.
   protected void printer(String s) {getHost().println(s); java.lang.System.out.println(s);}


}

/* Bitwig Extension written by 
 * Jon Sewi (Jeremy Martin van der Waard)
 * 
 * Code in this package is licensed under CC-BY-SA. 
 * This means every fork or iteration will prominently credit my name and also be licensed under CC-BY-SA. 
 * Everyone is free to use this, but I enjoy knowing what it's being used in, so let me know!
 * 
 * Jonsewi.com */
