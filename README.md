# Sewi-Extensions
Here's my collection of Bitwig extensions (a.k.a. Remote Scripts).
I'm new to a lot of things in the dev world, including Git, so helpful feedback is appreciated.

!! I'm sharing these for collaborative purposes. For growth. 
In no means are these meant to be bug-free, flexible for *your* workflow, stable, or efficient. 
I'm just trying to make Bitwig suit my needs more, and figure that others may benefit. !!

# Track Templates
*(version: 0.SoftWater - quite useful and nice to have, but can feel a bit sticky or slimy if you're not used to it.)*

Bitwig doesn't have a way to automatically populate new tracks in a session (yet), and that's annoying to anybody wanting the same channelstrip on every track.
So here's an extention that 'fixes' that. more like a patch... 

https://user-images.githubusercontent.com/109223338/184865688-a8bf2541-898c-4097-8da4-2e321d3b8eed.mp4

## Latest Changes (sept 29 2022)
- Rewrote the whole thing to be _much_ cleaner and easier to expand, bugfix, etc.

- Added: **Auto insert mode!**
-   The preferences page has fields to input device ID's.
-   You can right click on devices in Bitwig to copy the device ID. you can then paste it in the input field.
-   ID's are automatically identified as VST2, VST3 or Bitwig Native. 
-   (Also has an input field that can function as a little notepad where you can write down the name of the device)

- Changed: Browser mode. 
-   Now has an input field in the preferences for the name of the template tag. Makes customizing and switching much easier. 
-   Now slower for testing, but much more stable. Will be made faster in a future update. 

- Removed: Document settings. 
-   This feature may return in the future, but was a bit annoying to implement at the moment and seemed a little niche anyways.

--------

## Functionality
- Looks for changes in track number
- Checks for empty device chain on selected track
- Checks for the Auto insert and/or Browser Mode
- Auto Insert: looks for the Devices using ID's that are put in the preferences input field
- Browser: Opens browser and navigates to the top device of the audio FX presets with the user input tag (default: Template). 
- Commits the device onto the track.

## Known 'Issues'
- Sometimes doesn't update the preference settings
- Devices are inserted before the manually selected device, messing up default routing. 

## Future Features, Fixes, Uses and Thingstuffs
- User Manual, written and/or video.
- 
