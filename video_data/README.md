## BadApple video data setup
put the `BadApple` folder inside your `.minecraft` or if you are running the <br> gradle configuration then put the `BadApple` folder into the `run` folder of the project after running `gradlew gen source`


## how to generate video data
if you want to add your own video then follow these instructions:
first create an env and run `pip install -r requirements.txt`.
After that you can run `python generate.py path/to/video.mp4`,
that will generate a file called video.txt with all the pixel data.
the video pixel data will be sized down to redouce output size