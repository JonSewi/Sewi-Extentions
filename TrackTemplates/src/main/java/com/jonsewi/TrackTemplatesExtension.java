package com.jonsewi;

import com.bitwig.extension.callback.IntegerValueChangedCallback;
import com.bitwig.extension.callback.NoArgsCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.BrowserFilterItem;
import com.bitwig.extension.controller.api.BrowserFilterItemBank;
import com.bitwig.extension.controller.api.BrowserResultsItemBank;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.DeviceBank;
import com.bitwig.extension.controller.api.DocumentState;
import com.bitwig.extension.controller.api.EnumValue;
import com.bitwig.extension.controller.api.PopupBrowser;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.Signal;
import com.bitwig.extension.controller.api.TrackBank;

public class TrackTemplatesExtension extends ControllerExtension
{
	// ## Initializing variables
	private ControllerHost host;
	
	private TrackBank trackBank;
	private DeviceBank deviceBank;
	
	private DocumentState documentState;
	private PopupBrowser popupBrowser;
	private BrowserFilterItemBank browserTagCursor;
	private BrowserFilterItemBank browserDeviceTypeCursor;
	private BrowserResultsItemBank resultsItemBank;
	private CursorTrack cursorTrack;
	
	private String trackCountStr;
	private String tagName;
	private int browserTagAmount = 150;
	private int tagLocation;
	
	private String templateTagName = "template"; // TODO make available
	
	// #####################################################################################################################################
	
	
	// ## Bitwig Generated Code 
	protected TrackTemplatesExtension(final TrackTemplatesExtensionDefinition definition, final ControllerHost host)
	   {
	      super(definition, host);
	   }
	
	// ## Println but easier
	private void printer(String s) {
	    host.println(s);
	    java.lang.System.out.println(s);
	  }
	
	// #####################################################################################################################################
   
	@Override
	public void init()
	{
		// ## Declaring variables: creating observers --
		
		host = getHost();      
		documentState = host.getDocumentState();
		cursorTrack = host.createCursorTrack(0, 0); 
		popupBrowser = host.createPopupBrowser();
		browserDeviceTypeCursor = popupBrowser.deviceTypeColumn().createItemBank(4);
		resultsItemBank = popupBrowser.resultsColumn().createItemBank(20); //TODO Make bank sizes dynamic to max size of items with tag
		
		trackBank = host.createMainTrackBank(1, 0, 0);
		
		deviceBank = cursorTrack.createDeviceBank(1);
		
		
		// #####################################################################################################################################
      
		// ## Marking values to observe for getting and setting --
		
		trackBank.itemCount().markInterested();
		trackBank.itemCount().addValueObserver(printTrackCount());
		
		deviceBank.itemCount().markInterested();
		
		popupBrowser.exists().markInterested();
		popupBrowser.resultsColumn().entryCount().markInterested();
		popupBrowser.tagColumn().entryCount().markInterested();
		popupBrowser.selectedContentTypeIndex().markInterested();

		// Marking every tag as interesting for name retrieval
		browserTagCursor = popupBrowser.tagColumn().createItemBank(browserTagAmount); 
		for (int i = 0; i < (browserTagAmount); i++)
		{
			browserTagCursor.getItemAt(i).name().markInterested();
		}
		
		// #####################################################################################################################################
		
		// Debug Button to open Browser
      	documentState.getSignalSetting("Browse", "Browse", "Browse").addSignalObserver(browserTemplateCommit());
      
	  	//TODO set interested on every tag entry in browser
      
      
	}
	
	private IntegerValueChangedCallback printTrackCount() 
	{
		return (valueChanged) ->
		{
		Integer trackCount = trackBank.itemCount().get();
		String trackCountStr = trackCount.toString();
		printer("Amount of tracks = " + trackCountStr);
		
		if (deviceBank.itemCount().get() == 0)
		{
			
			browserTemplateCommit().call();
		}
		
		};
		
	}

	// #####################################################################################################################################
   
	private NoArgsCallback browserTemplateCommit()
	{
	   return () -> 
	   {
		      if (!popupBrowser.exists().getAsBoolean()) 
		      {
		        cursorTrack.endOfDeviceChainInsertionPoint().browse();
		      }
		   	printer("Setting template...");
		   	host.scheduleTask(
		    	() -> {
    			  popupBrowser.selectedContentTypeIndex().set(1); // Set browser to search Presets
    			  browserDeviceTypeCursor.getItemAt(1).isSelected().set(true); // Set browser to search Any Device Type
    			  
    			  
    			  host.scheduleTask(
    					  () -> { 
    						  
    						// Set the position of the Template Tag in the browser
    						  browserTagAmount = popupBrowser.tagColumn().entryCount().get();
    						  for (int i = 0; i < (browserTagAmount); i++)
    					      {
    							  tagName = browserTagCursor.getItemAt(i).name().get();
    							  if (tagName.equals(templateTagName))
    							  {
    								  tagLocation = i;
    								  printer("The int value of the " + templateTagName + " tag is " + tagLocation);
    							  }
    					      }
    						  
    						  // Navigate to Template Tag, Navigate to item, press OK
    						  host.scheduleTask( () -> {browserTagCursor.getItemAt(tagLocation).isSelected().set(true);}, 100);
    						  host.scheduleTask( () -> {resultsItemBank.getItemAt(0).isSelected().set(true);}, 150);
    						  host.scheduleTask( () -> {popupBrowser.commit() ;}, 200);
    						  
    					  }, 250);
    			  
    			  
		    		  }, 300);
		      
	   };
	}
	
	// #####################################################################################################################################
   
	@Override
	public void exit()
	{
      // TODO: Perform any cleanup once the driver exits
      // For now just show a popup notification for verification that it is no longer running.
      getHost().showPopupNotification("TrackTemplates Exited");
	}

	@Override
	public void flush()
	{
      // TODO Send any updates you need here.
	}


}
