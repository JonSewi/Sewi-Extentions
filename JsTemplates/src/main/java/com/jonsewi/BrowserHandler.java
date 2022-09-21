package com.jonsewi;

import com.bitwig.extension.controller.api.BrowserFilterItemBank;
import com.bitwig.extension.controller.api.BrowserResultsItemBank;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.PopupBrowser;

public class BrowserHandler {

	private ControllerHost host;
	private CursorTrack cursorTrack;

	
	private PopupBrowser popupBrowser;
	
	private BrowserFilterItemBank browserDeviceType;
	private BrowserResultsItemBank resultsItemBank;
	private BrowserFilterItemBank browserTagCursor;
	
	private String trackType; // TODO future use
	private String templateTag;
	private int templateTagLocation;
	
	public BrowserHandler(ControllerHost host, CursorTrack cursorTrack) 
	{
		this.host = host;
		this.cursorTrack = cursorTrack;
		popupBrowser = host.createPopupBrowser();
	}
	

	public void setupBrowser()
	{	//	Set up getters and cursors for navigating the browser.
		
		popupBrowser.exists().markInterested();
		popupBrowser.selectedContentTypeIndex().markInterested();
		popupBrowser.tagColumn().entryCount().markInterested();
		popupBrowser.resultsColumn().entryCount().markInterested();
		
		browserDeviceType = popupBrowser.deviceTypeColumn().createItemBank(4);
		resultsItemBank = popupBrowser.resultsColumn().createItemBank(20);
		
		// 	Creating name getters for each tag so that they can be matched to the user input Template Tag.
		
		int browserTagColumnCount = 200; 
		// 	arbitrary number as the number of results in the tag column. 
		// 	Getting the size of the tag column requires opening the browser at initialization, which is slower.
		//	Because it's going through a *hypothetical* bank of tags, there's no risk of going out of bounds.
		
		browserTagCursor = popupBrowser.tagColumn().createItemBank(browserTagColumnCount); 
		for (int i = 0; i < (browserTagColumnCount); i++)
			{
				browserTagCursor.getItemAt(i).name().markInterested();
			}
	}
	
	public void insertWithBrowser(String trackType, String templateTag)
	{	//	Opens browser (if not already open) and navigates to the template tag. Commits the chosen device.
		
		//	Note the ScheduleTasks. the numbers behind the functions are delays in milliseconds. 
		//	These delays are required for the browser to function.
		//	This will be optimized and possibly given a user input multiplier to tweak to the speed of the user's system.
		//	For stability, it uses generous times at the moment.
		
		//	For tweakers:
		//	Step 1 and 2 can generally be done simultaneously
		//	Step 3 takes the longest, using by far the most steps, as it iterates through every tag name
		//	Step 4, 5 and 6 are likely quite fast, but do need some delay between them.
		
		this.trackType = trackType;
		this.templateTag = templateTag;
		if (!popupBrowser.exists().getAsBoolean()) {cursorTrack.endOfDeviceChainInsertionPoint().browse();} //open browser
		host.scheduleTask(navigateToDevice(), 10);
		
		// 6. Commit the result (same as pressing 'OK' in the browser)
		host.scheduleTask(() -> {popupBrowser.commit();}, 1200);
	}
	
	public Runnable navigateToDevice()
	{
		return () ->
		{
			// 1. Set to search 'any device type' in the bottom left.
			host.scheduleTask(() -> 
				{browserDeviceType.getItemAt(0).isSelected().set(true);
					}, 200);
			
			// 2. Set to search 'presets' in the top right.
			host.scheduleTask(() -> 
				{popupBrowser.selectedContentTypeIndex().set(1);
					}, 400);
			
			// 3. goes through the whole list of tags to find the one matching the user input template tag.
			host.scheduleTask(findTemplateTag()
					, 600);
			
			// 4. Selects the user input template tag
			host.scheduleTask(() -> 
			{browserTagCursor.getItemAt(templateTagLocation).isSelected().set(true);
				}, 800);
			
			// 5. Selects the first result
			host.scheduleTask(() -> 
				{resultsItemBank.getItemAt(0).isSelected().set(true);
					}, 1000);
		};
	}
	
	public Runnable findTemplateTag()
	{	// Goes through the whole tag column until it finds a tag that matches the user input.
		// sets the location of the tag in the column, used by the next step.
		
		return () ->
		{
			int columnSize = popupBrowser.tagColumn().entryCount().get();
			for (int i = 0; i < columnSize; i++)
			{
				String tagCursor = browserTagCursor.getItemAt(i).name().get(); 
				if (tagCursor.equals(templateTag))
				{
					templateTagLocation = i;
					break;
				}
			};
		};
	}
	
//	Shorthand method to make debugging easier.
	protected void printer(String s) {host.println(s); java.lang.System.out.println(s);}

}
