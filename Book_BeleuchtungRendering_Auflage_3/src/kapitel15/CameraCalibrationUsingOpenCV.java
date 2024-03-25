package kapitel15;

import java.io.File;
import java.util.ArrayList;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class CameraCalibrationUsingOpenCV {
	private int countFrames, countGoodFrames;
	private boolean highPolynomeDegree;
	private int flagsCorner = 0;
	private int flagsCalib = 0;
	private TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 40, 0.001);
	private Size winSize = new Size(21, 21); // 15, 15
	private Size zoneSize = new Size(-1, -1);
	private Size patternSize;
	private ArrayList<Mat> objectPoints;
	private ArrayList<Mat> imagePoints = new ArrayList<Mat>();
	private ArrayList<Mat> vImg;
	private Mat cameraMatrix = Mat.eye(3, 3, CvType.CV_64F);
	private Mat distCoeffs = Mat.zeros(8, 1, CvType.CV_64F);
	private ArrayList<Mat> rvecs = new ArrayList<Mat>();
	private ArrayList<Mat> tvecs = new ArrayList<Mat>();
	public double lastCalibrationReprojectionError;
	
	double fx, fy, cx, cy, k1, k2, k3, k4, k5, k6, p1, p2;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public CameraCalibrationUsingOpenCV() {
		this(new Size(9, 6), true);
	}

	public CameraCalibrationUsingOpenCV(Size patternSize, boolean highPolynomeDegree) {
		this.patternSize = patternSize;
		this.highPolynomeDegree = highPolynomeDegree;
		initialize();
	}

	public void initialize() {
		// flags corners

		/*
		 * Use adaptive thresholding to convert the image to black and white, rather
		 * than a fixed threshold level (computed from the average image brightness
		 */
		flagsCorner |= Calib3d.CALIB_CB_ADAPTIVE_THRESH;

		/*
		 * Run a fast check on the image that looks for chessboard corners, and shortcut
		 * the call if none is found. This can drastically speed up the call in the
		 * degenerate condition when no chessboard is observed.
		 */
		flagsCorner |= Calib3d.CALIB_CB_FAST_CHECK;

		/*
		 * Normalize the image gamma with "equalizeHist" before applying fixed or
		 * adaptive thresholding.
		 */
		flagsCorner |= Calib3d.CALIB_CB_NORMALIZE_IMAGE;

		/*
		 * Use additional criteria (like contour area, perimeter, square-like shape) to
		 * filter out false quads extracted at the contour retrieval stage.
		 */
		flagsCorner |= Calib3d.CALIB_CB_FILTER_QUADS;

		// flags calibration
		flagsCalib |= Calib3d.CALIB_ZERO_TANGENT_DIST;
		flagsCalib |= Calib3d.CALIB_RATIONAL_MODEL;
		//flagsCalib |= Calib3d.CALIB_FIX_PRINCIPAL_POINT;
		flagsCalib |= Calib3d.CALIB_SAME_FOCAL_LENGTH;

		if (!highPolynomeDegree) {
			flagsCalib |= Calib3d.CALIB_FIX_K4;
			flagsCalib |= Calib3d.CALIB_FIX_K5;
			flagsCalib |= Calib3d.CALIB_FIX_K6;
			System.out.println("set k4, k5 k6 fix");
		}
	}

	public boolean getCorners(Mat gray, MatOfPoint2f corners, File f) {       
		if (!Calib3d.findChessboardCorners(gray, patternSize, corners, flagsCorner))
			return false;
		Imgproc.cornerSubPix(gray, corners, winSize, zoneSize, criteria);
		return true;
	}

	public MatOfPoint3f getCorner3f() {
		MatOfPoint3f corners3f = new MatOfPoint3f();
		double squareSize = 50;
		Point3[] vp = new Point3[(int) (patternSize.height * patternSize.width)];
		int cnt = 0;
		for (int i = 0; i < patternSize.height; ++i)
			for (int j = 0; j < patternSize.width; ++j, cnt++)
				vp[cnt] = new Point3(j * squareSize, i * squareSize, 0.0d);
		corners3f.fromArray(vp);
		return corners3f;
	}

	public void calibrate() {
		double errReproj = Calib3d.calibrateCamera(objectPoints, imagePoints, ((Mat) (vImg.get(0))).size(),
				cameraMatrix, distCoeffs, rvecs, tvecs, flagsCalib);

		boolean isCalibrated = Core.checkRange(cameraMatrix) && Core.checkRange(distCoeffs);
		System.out.println("isCalibrated. " + isCalibrated);

		System.out.println("done, \nerrReproj = " + errReproj);
		System.out.println("cameraMatrix = \n" + cameraMatrix.dump());
		System.out.println("distCoeffs = \n" + distCoeffs.dump());

		fx = cameraMatrix.get(0, 0)[0];
		fy = cameraMatrix.get(1, 1)[0];
		cx = cameraMatrix.get(0, 2)[0];
		cy = cameraMatrix.get(1, 2)[0];

		k1 = distCoeffs.get(0, 0)[0];
		k2 = distCoeffs.get(1, 0)[0];
		p1 = distCoeffs.get(2, 0)[0];
		p2 = distCoeffs.get(3, 0)[0];
		k3 = distCoeffs.get(4, 0)[0];
		k4 = distCoeffs.get(5, 0)[0];
		k5 = distCoeffs.get(6, 0)[0];
		k6 = distCoeffs.get(7, 0)[0];
	}

	public void getAllCorners(String pathSrc, String pathDst) {
		countFrames = 0;
		countGoodFrames = 0;
		vImg = new ArrayList<Mat>();
		objectPoints = new ArrayList<Mat>();
		imagePoints = new ArrayList<Mat>();
		MatOfPoint3f corners3f = getCorner3f();
		for (File f : new File(pathSrc).listFiles()) {
			Mat mat = Highgui.imread(f.getPath(), Highgui.CV_LOAD_IMAGE_COLOR);
			if (mat == null || mat.channels() != 3)
				continue;

			countFrames++;
			System.out.println("fn = " + f.getPath());
			System.out.println("mat.channels() = " + mat.channels() + ", " + mat.cols() + ", " + mat.rows());

			Mat gray = new Mat();
			Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
			MatOfPoint2f corners = new MatOfPoint2f();
			if (!getCorners(gray, corners, f))
				continue;

			System.out.println("chessboard found");
			Mat renderedFrameCol = new Mat(mat.size(), mat.type());
			Imgproc.cvtColor(mat, renderedFrameCol, Imgproc.COLOR_BGR2RGB);
			Calib3d.drawChessboardCorners(renderedFrameCol, patternSize, corners, true);
			Highgui.imwrite(pathDst + "/cb_col_" + f.getName(), renderedFrameCol);

			countGoodFrames++;
			objectPoints.add(corners3f);
			imagePoints.add(corners);
			vImg.add(mat);
		}
		System.out.println("Schachbrett wurde in "+countGoodFrames+" von "+countFrames+" identifiziert");
	}

	public void undistFrames(String pathsrc, String pathdest) {
		for (File f : new File(pathsrc).listFiles()) {
			Mat mat = Highgui.imread(f.getPath(), Highgui.CV_LOAD_IMAGE_COLOR);
			if (mat == null || mat.channels() != 3)
				continue;

			Mat renderedFrame = new Mat(mat.size(), mat.type());
			Imgproc.undistort(mat, renderedFrame, cameraMatrix, distCoeffs);
			Highgui.imwrite(pathdest + "/" + f.getName(), renderedFrame);
		}
	}

	public static void main(String[] args) {
		CameraCalibrationUsingOpenCV myCCB = new CameraCalibrationUsingOpenCV(new Size(9, 6), true);

		System.out.println("   > detecting chessboard corners");
		myCCB.getAllCorners("calibration/chessboards", "calibration/pattern");

		myCCB.calibrate();

		System.out.println("   > applying calibration parameters to frame set");
		myCCB.undistFrames("calibration/chessboards", "calibration/undistorted");
		myCCB.undistFrames("calibration/distorted", "calibration/undistorted");
	}
}
