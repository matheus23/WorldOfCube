package org.worldOfCube.client.logic.chunks.generation.trees;

import java.util.Random;

/**
 * Used by "Tree", to generate Trees with different looks.
 * @author matheusdev
 * @see org.worldOfCube.client.logic.chunks.generation.trees.Tree
 */
public interface TreeGenerator {
	
	/**
	 * @param predist the distance of the previous Log.
	 * @return a new distance, which should always be 
	 * smaller than the previous distance. Else it could
	 * lead to a never-ending recursive Algorithm, causing
	 * a StackOverflow.
	 */
	public double deriveDistance(double predist);
	
	/**
	 * Derive the distance of the previous Log.
	 * @param prerot the rotation of the previous Log.
	 * @return the new Rotation derived from prerot.
	 */
	public double deriveRotation(double prerot);
	
	/**
	 * @return the Log-length, at which Logs won't grow 
	 * any further anymore.
	 */
	public double getMinimalDist();
	
	/**
	 * Used to build a TreeLog to a World (in a representation of Blocks).
	 * @param tl the Log to be build.
	 * @param rand an instance of Random to create random values.
	 */
	public void build(TreeLog tl, Random rand);

}
