import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_highgui.*;

public class UavComp implements CamView{
    private String camera;
    private boolean lidar;
    private boolean thermal;

    FrameGrabber grabber;
    Frame frame;
    Mat img;
    OpenCVFrameConverter.ToMat converter;

    public UavComp(String camera, boolean lidar, boolean thermal, String alamatCam) throws FrameGrabber.Exception {
        this.camera = camera;
        this.lidar = lidar;
        this.thermal = thermal;
        this.grabber = new OpenCVFrameGrabber(alamatCam);
        this.converter = new OpenCVFrameConverter.ToMat();
    }

    public UavComp(String camera, boolean lidar, boolean thermal, int alamatCam) throws FrameGrabber.Exception {
        this.camera = camera;
        this.lidar = lidar;
        this.thermal = thermal;
        this.grabber = new OpenCVFrameGrabber(alamatCam);
        grabber.start();
        this.converter = new OpenCVFrameConverter.ToMat();
    }

    public void infoSensor() {
        System.out.println("Camera: " + camera);
        System.out.println("LIDAR: " + lidar);
        System.out.println("Thermal: " + thermal);
    }

    //Getter Ini awokoakwo
    public String getCamera() {
        return camera;
    }

    public boolean isLidar() {
        return lidar;
    }

    public boolean isThermal() {
        return thermal;
    }


    @Override
    public void TampilkanGUI() throws FrameGrabber.Exception {
        System.out.println("Menampilkan Live View Camera ...");
        grabber.start();
        while((frame = grabber.grab()) != null){
            img = converter.convert(frame);
            if (img == null) continue;

            Mat hsv = new Mat();
            cvtColor(img,hsv,COLOR_BGR2HSV);

            Scalar lowerRed = new Scalar(0, 100, 100, 0);
            Scalar upperRed = new Scalar(10, 255, 255, 0);
            Scalar lowerRed2 = new Scalar(160, 100, 100, 0);
            Scalar upperRed2 = new Scalar(179, 255, 255, 0);

            Mat mask1 = new Mat();
            Mat mask2 = new Mat();
            Mat mask = new Mat();

            inRange(hsv, new Mat(lowerRed), new Mat(upperRed), mask1);
            inRange(hsv, new Mat(lowerRed2), new Mat(upperRed2), mask2);
            addWeighted(mask1, 1.0, mask2, 1.0, 0.0, mask);

            MatVector contours = new MatVector();
            findContours(mask, contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

            for (long i = 0; i < contours.size(); i++) {
                Mat contour = contours.get(i);

                double area = contourArea(contour);

                if (area > 500) {

                    Rect rect = boundingRect(contour);

                    rectangle(img, rect, new Scalar(0, 255, 0, 0), 2, LINE_8, 0);

                    putText(img, "Hipotesis Korban", new Point(rect.x(), rect.y() - 10),
                            FONT_HERSHEY_PLAIN, 0.7, new Scalar(0, 255, 204, 0), 1, LINE_AA, false);
                }
            }

            imshow("UUAV-CAM_View", img);
            if (waitKey(30) >= 0) break;
        }

        grabber.close();
    }

}
