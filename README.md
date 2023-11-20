# BeatFileEditor
Creates and edits beat files for ChristmasJukebox.

## About
The BeatFileEditor for ChristmasJukebox allows editing beats based on the inputted song.
Some features include:
- Beat recording: Record beats with your keyboard while the song is playing
- In-depth beat editing: Track editor for adding, removing, and moving beats within the beat file
- Per-channel editing: Edit for one channel, or edit for many channels at once
- Dynamic beat precision: Edit in seconds, 100ms, 10ms, or 1ms intervals
- Simple User Interface: Click to add beats to the beat file in a specific channel **(WIP)**

## Performance
This software was oriented to be used on low end hardware. The maximum ram consumption should be around 200MB or less.
Ram usaged reductions are made wherever possible, unless the change would severely hurt CPU performance.

## File Output
`1` - Single beat (Ex: 1ms)

`[1, 10]` - Held beat (Ex: 1ms to 10ms)
