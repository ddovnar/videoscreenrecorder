# videoscreenrecorder
Screen recording service

## Recording desktop screen to video file
OpenCVScreenRecorder - this class implements recording screen to video file in MP4 format
The quality (size of file) can be setting up

## Recording desktop screen to stream through HTTP
OpenCVHttpScreenRecorder - this class implements recording screen to http-stream, by jpg-files sending by the time interval

# Compile

```bash
mvn install
```

This command compile project. After compiling, the folder "target" appear in the base project folder. It's contain `appassembler`-folder with bash-command to launching.
All files in `lib`-folder need copied to the `appassembler/repo`