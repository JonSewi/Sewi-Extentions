# Sewi-Extensions
Here's my collection of Bitwig extensions (a.k.a. Remote Scripts).
I'm new to a lot of things in the dev world, including Git, so helpful feedback is appreciated.

!! I'm sharing these for collaborative purposes. For growth. 
In no means are these meant to be bug-free, flexible for *your* workflow, stable, or efficient. 
I'm just trying to make Bitwig suit my needs more, and figure that others may benefit. !!

# Track Templates
*(version: 0.Bleach: functional but a little heavy handed)*

Bitwig doesn't have a way to automatically populate new tracks in a session (yet), and that's annoying to anybody wanting the same channelstrip on every track.
So here's an extention that 'fixes' that. more like a patch... 

https://user-images.githubusercontent.com/109223338/184865688-a8bf2541-898c-4097-8da4-2e321d3b8eed.mp4

## Functionality
- Requires a (currently audio FX) preset to be saved with the tag 'Template'. 
- Looks for changes in track number
- Checks for empty device chain on selected track
- Opens browser and navigates to the top device of the audio FX presets with the 'Template' tag. 
- Commits the device onto the track.

## Known 'Issues'
- The popping up of the browser is distracting and looks slow (though it's just 1/3 of a second)
- Can only have one 'Template' preset in the tags. For now, naming the top template with a '1' in front of it should work. 
- (EZ FIX!) Opens up right when the project loads
- Has some debugging things happening (like 'exited Track Templates' when exiting, logging some info in the console, etc) 

## Future Features, Fixes, Uses and Thingstuffs
- Make a version that uses device ID's instead of the browser if presets aren't necessary. 
- Have user input for which device they want to load from their Template tag. (Which can have a 'default' in the main settings and an 'override' in the document settings.)
- Have user input on the name of the Template tag (for if you already have a Template tag for instance)
- Have a template for each track type. 
