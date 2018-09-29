import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
public class MainClass {


	public static void main(String[] args) {
		// Import Ascii stl file.
		ImportSTL stl = new ImportSTL("solid sphere.stl");
		try {
			stl.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

			
			ArrayList layerShell = new ArrayList();
			ArrayList volumeShell = new ArrayList();

		Plane p = new Plane(new Vector(0, 0, 1)); //z axis
		float objHeight = 50; // in mm
		int counter = 0;

		// Trival Algorithm
		for(float cutter = 0; cutter < objHeight; cutter += 0.2f) {
			counter++;
			p.setDistance(cutter);
			ArrayList<Vector> intersectPoints = new ArrayList<Vector>();


			// Position Vector of the plane
			Vector e = p.normal.mull(p.height);  // Vector e = p.height;

			// Draw new Image
			BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = img.createGraphics();



			for (int i = 0; i < stl.triangleMesh.size(); i++) {
				ArrayList<Vector> ints = stl.triangleMesh.get(i).intersectPlane(p);

				float[] x = new float[ints.size()];
				float[] y = new float[ints.size()];

				for(int j = 0; j < ints.size(); j++) {
					// calc position vector of points in plane via hessian
					Vector s =e.sub(ints.get(j));
					x[j] = p.getV().scalarProduct(s);
					y[j] = p.getU().scalarProduct(s);

/*					System.out.println(x[j]);
					System.out.println(y[j]);
*/					
				double pointX = (double) x[j];
				double pointY = (double) y[j];
				double[] ls = new double[2];
				ls[0] = pointX;
				ls[1] = pointY;
				layerShell.add(j, ls);
				}

				//draw resulting line in image
				if(x.length > 1) {
					g2d.drawLine((int) (x[0] * 10 + img.getWidth() / 16), 
						(int) (y[0] * 10 + img.getWidth() / 16),
						(int) (x[1] * 10 + img.getWidth() / 16),
						(int) (y[1] * 10 + img.getWidth() / 16));
				}

				volumeShell.add(i, layerShell);

				intersectPoints.addAll(ints);
			}
			System.out.println("#Intersects_=_" + intersectPoints.size());
			g2d.dispose();
			// write output image in file
			try {				
				ImageIO.write(img, "png", new File("Output/render" + counter + ".png"));  
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}