## Generating video data
create a python env with `python -m venv env` (python3) <br>
then activate the env and run `pip install -r requirements`. <br>
After that you can run `python generate.py` (with the env), that will ask you for the file name, <br>
normally you just should give it the `bad_apple.mp4` file that is already present but you can give it any black and white video.
After that you'll get a videoData.txt file that can be put into your BadApple folder inside .minecraft or the run folder in case you run
minecraft with the gradle config 


## Info
the `DIVISOR` variable inside generate.py is the value that divides the video size, so a video that is 200x100 would become 20x10.
Changing this value will make the video bigger or smaller, but it will take much much longer to parse the video