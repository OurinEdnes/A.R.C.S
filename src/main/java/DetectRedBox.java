import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_highgui.*;

public class DetectRedBox {
    public static void main(String[] args) throws Exception {

        // Pilih kamera (0 = Webcam laptop, 1 = Iriun/USB)
        FrameGrabber grabber = new OpenCVFrameGrabber(1);
        grabber.start();

        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Frame frame;
        Mat img;

        System.out.println("🚀 Tracking System Activated! Press any key to exit.");

        while ((frame = grabber.grab()) != null) {
            img = converter.convert(frame);
            if (img == null) continue;

            // 1. Persiapan Gambar (Sama kayak sebelumnya)
            Mat hsv = new Mat();
            cvtColor(img, hsv, COLOR_BGR2HSV);

            // 2. Filter Warna Merah
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

            // 3. ✨ MENCARI KONTOUR (Garis Tepi) ✨
            // Kita cari "pulau-pulau" putih di dalam mask
            MatVector contours = new MatVector();
            findContours(mask, contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

            // 4. ✨ MENGGAMBAR KOTAK ✨
            // Kita cek satu-satu pulaunya
            for (long i = 0; i < contours.size(); i++) {
                Mat contour = contours.get(i);

                // Hitung luas area merahnya
                double area = contourArea(contour);

                // Filter: Kalau areanya kecil (cuma titik/noise), cuekin aja
                // Angka 500 ini sensitivitasnya, makin gede makin "buta" sama titik kecil

                if (area > 500) {

                    Rect rect = boundingRect(contour);

                    rectangle(img, rect, new Scalar(0, 255, 0, 0), 2, LINE_8, 0);

                    putText(img, "Korban Ditemukan", new Point(rect.x(), rect.y() - 10),
                            FONT_HERSHEY_PLAIN, 1.2, new Scalar(0, 255, 0, 0), 2, LINE_AA, false);
                }
            }

            // 5. Tampilkan SATU VIEW aja
            imshow("AIkooo Tracking System", img);

            if (waitKey(30) >= 0) break;
        }

        grabber.close();
        System.exit(0);
    }
}