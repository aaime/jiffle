/*
 * Copyright 2011 Michael Bedward
 * 
 * This file is part of jai-tools.
 *
 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package jaitools.jiffle.runtime;

import java.util.Random;

import static jaitools.numeric.DoubleComparison.dcomp;
import static jaitools.numeric.DoubleComparison.dzero;
import jaitools.numeric.SampleStats;
import java.util.List;

/**
 * Provides functions for Jiffle runtime objects. An instance of this class
 * is created by {@link AbstractJiffleRuntime}.
 * 
 * @author Michael Bedward
 * @since 1.1
 * @version $Id$
 */
public class JiffleFunctions {
    
    private Random rr = new Random();
    
    /**
     * Converts an angle in degrees to radians.
     * 
     * @param x input angle in degrees
     * @return angle in radians
     */
    public double degToRad(double x) {
        return Math.PI * x / 180d;
    }

    /**
     * Return the sign of {@code x} as an integer. This method is used 
     * by Jiffle to implement its various {@code if} functions.
     * <p>
     * 
     * @param x test value
     * 
     * @return -1 if x is negative; 0 if x is 0; 1 if x is positive; 
     *         or {@code null} if x is NaN
     */
    public Integer sign(double x) {
        if (!Double.isNaN(x)) {
            return dcomp(x, 0);
        }
        return null;
    }
    
    /**
     * Tests if x is infinite (equal to Double.POSITIVE_INFINITY or 
     * Double.NEGATIVE_INFINITY).
     * 
     * @param x test value
     * @return 1 if x is infinite; 0 if finite; or {@code Double.Nan}
     *         if x is {@code Double.Nan}
     */
    public double isinf(double x) {
        return (Double.isNaN(x) ? Double.NaN : (Double.isInfinite(x) ? 1d : 0d));
    }
    
    /**
     * Tests if x is equal to Double.NaN.
     * 
     * @param x test value
     * @return 1 if x is NaN; 0 otherwise
     */
    public double isnan(double x) {
        return Double.isNaN(x) ? 1d : 0d;
    }
    
    /**
     * Tests if x is null. This is the same as {@link #isnan(double)}.
     * 
     * @param x the test value
     * @return 1 if x is null; 0 otherwise
     */
    public double isnull(double x) {
        return Double.isNaN(x) ? 1d : 0d;
    }

    /**
     * Gets the log of x to base b.
     * 
     * @param x the value
     * @param b the base
     * @return log to base b
     */
    public double log2Arg(double x, double b) {
        return Math.log(x) / Math.log(b);
    }
    
    /**
     * Gets the maximum of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the maximum value
     */
    public double max(List values) {
        return SampleStats.max(listToArray(values), true);
    }
    
    /**
     * Gets the mean of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the mean value
     */
    public double mean(List values) {
        return SampleStats.mean(listToArray(values), true);
    }
    
    /**
     * Gets the median of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the median value
     */
    public double median(List values) {
        return SampleStats.median(listToArray(values), true);
    }
    
    /**
     * Gets the minimum of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the minimum value
     */
    public double min(List values) {
        return SampleStats.min(listToArray(values), true);
    }
    
    /**
     * Gets the mode of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the modal value
     */
    public double mode(List values) {
        return SampleStats.mode(listToArray(values), true);
    }
    
    /**
     * Converts an angle in radians to degrees.
     * 
     * @param x input angle in radians
     * @return angle in degrees
     */
    public double radToDeg(double x) {
        return x / Math.PI * 180d;
    }
    
    /**
     * Gets a random value between 0 (inclusive) and x (exclusive).
     * 
     * @param x upper limit
     * @return the random value
     */
    public double rand(double x) {
        return rr.nextDouble() * x;
    }
    
    /**
     * Gets a random integer value (actually a truncated double) between 
     * 0 (inclusive) and {@code floor(x)} (exclusive).
     * 
     * @param x upper limit
     * @return the random value
     */
    public double randInt(double x) {
        return rr.nextInt((int) x);
    }
    
    /**
     * Gets the range of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the range of the input values
     */
    public double range(List values) {
        return SampleStats.range(listToArray(values), true);
    }
    
    /**
     * Rounds the input value to the given precision.
     * 
     * @param x the input value
     * @param prec the desired precision
     * @return the rounded value
     */
    public double round2Arg(double x, double prec) {
        int ifac = (int) (prec + 0.5);
        return Math.round(x / ifac) * ifac;
    }
    
    /**
     * Gets the sample standard deviation of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the standard deviation of the input values
     */
    public double sdev(List values) {
        return SampleStats.range(listToArray(values), true);
    }
    
    
    /**
     * Gets the sum of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the sum of the input values
     */
    public double sum(List values) {
        return SampleStats.sum(listToArray(values), true);
    }

    /**
     * Gets the sample variance of the input values. Double.Nan (null)
     * values are ignored.
     * 
     * @param values the input values
     * @return the variance of the input values
     */
    public double variance(List values) {
        return SampleStats.variance(listToArray(values), true);
    }
    
    /**
     * Tests if either x or y is non-zero.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if either x or y is non-zero; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double OR(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (!dzero(x) || !dzero(y) ? 1d : 0d);
    }

    /**
     * Tests if both x and y are non-zero.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if both x and y are non-zero; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double AND(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (!dzero(x) && !dzero(y) ? 1d : 0d);
    }

    /**
     * Tests if just one of x or y is non-zero.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if just one of x or y is non-zero; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double XOR(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (!dzero(x) ^ !dzero(y) ? 1d : 0d);
    }

    /**
     * Tests if x is greater than y.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if x is greater than y; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double GT(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (dcomp(x, y) > 0 ? 1d : 0d);
    }

    /**
     * Tests if x is greater than or equal to y.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if x is greater than or equal to y; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double GE(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (dcomp(x, y) >= 0 ? 1d : 0d);
    }

    /**
     * Tests if x is less than y.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if x is less than y; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double LT(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (dcomp(x, y) < 0 ? 1d : 0d);
    }

    /**
     * Tests if x is less than or equal to y.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if x is less than or equal to y; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double LE(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (dcomp(x, y) <= 0 ? 1d : 0d);
    }

    /**
     * Tests if x is equal to y.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if x is equal to y; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double EQ(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (dcomp(x, y) == 0 ? 1d : 0d);
    }

    /**
     * Tests if x is not equal to y.
     * 
     * @param x x value
     * @param y y value
     * @return 1 if x is not equal to y; 0 if this is not the case;
     *         or {@code Double.Nan} if either x or y is {@code Double.Nan}
     */
    public double NE(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        return (dcomp(x, y) != 0 ? 1d : 0d);
    }

    /**
     * Treats x as true if non-zero, or false if zero and then
     * returns the logical complement.
     * 
     * @param x the test value
     * @return 1 if x is zero; 0 if x is non-zero; 
     * or {@code Double.Nan} if x is {@code Double.Nan}
     */
    public double NOT(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        return (dzero(x) ? 1d : 0d);
    }

    private Double[] listToArray(List values) {
        final int N = values.size();
        Double[] dvalues = new Double[values.size()];
        for (int i = 0; i < N; i++) {
            dvalues[i] = ((Number)values.get(i)).doubleValue();
        }
        return dvalues;
    }
}

