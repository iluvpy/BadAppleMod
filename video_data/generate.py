import cv2
import os

DIVISOR = 2

def main() -> None:
    path = input("video file: ")
    if not os.path.exists(path):
        print("Invalid path")
        return

    cap = cv2.VideoCapture(path)
    videoData = open("videoData.txt", "w+")
    width = int(cap.get(3)/DIVISOR)
    height = int(cap.get(4)/DIVISOR)
    videoData.write(f"{width}d")
    videoData.write(f"{height}d")
    fps = cap.get(cv2.CAP_PROP_FPS)
    print(f"fps: {fps}")
    print(f"width: {width}")
    print(f"height: {height}")

    while(cap.isOpened()):
        ret, frame = cap.read()
        if not ret:
            break 
        frame_shape = frame.shape
        i_r = int(frame_shape[0]/DIVISOR)
        for i in range(i_r):
            for j in range(int(frame_shape[1]/DIVISOR)):
                id = i*DIVISOR
                jd = j*DIVISOR
                res = int(frame[id][jd][0])+int(frame[id][jd][1])+int(frame[id][jd][2])
                videoData.write(f"{0 if res < 255*3 else 1}")
        videoData.write("e") # 'e' means end of a frame
    
    cap.release()
    videoData.close()

if __name__ == "__main__":
    main()