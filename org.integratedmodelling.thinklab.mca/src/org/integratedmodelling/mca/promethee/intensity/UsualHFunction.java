package org.integratedmodelling.mca.promethee.intensity;

/**
 *
 * @author Edwin Boaz Soenaryo
 */
public class UsualHFunction implements IHFunction {

    public double getHValue(double difference) {
        if (difference != 0) return 1.0;
        else return 0.0;
    }

}
