/**
 Handy length / volume conversion utility.  Defines enums for various units and
 a static conversion method for both length and volume conversions.

 @author <A HREF="mailto:jonathan.engelsma@gvsu.edu">Jonathan Engelsma</A>
 @version $Revision: 1.0 $ $Date: 2018/10/01 22:15:25 $
 **/

package edu.gvsu.cis.convcalc;

import java.util.HashMap;

public class UnitsConverter {

    public enum VolumeUnits {
        Liters, Gallons, Quarts
    }

    public enum LengthUnits {
        Meters, Yards, Miles
    }

    private static class VolumeConversionKey  {
        VolumeUnits from;
        VolumeUnits to;
        VolumeConversionKey(VolumeUnits t, VolumeUnits f) { from = f; to = t;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VolumeConversionKey that = (VolumeConversionKey) o;

            if (from != that.from) return false;
            return to == that.to;
        }

        @Override
        public int hashCode() {
            int result = from.hashCode();
            result = 31 * result + to.hashCode();
            return result;
        }
    }

    private static class LengthConversionKey  {
        LengthUnits from;
        LengthUnits to;
        LengthConversionKey(LengthUnits t, LengthUnits f) { from = f; to = t;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LengthConversionKey that = (LengthConversionKey) o;

            if (from != that.from) return false;
            return to == that.to;
        }

        @Override
        public int hashCode() {
            int result = from.hashCode();
            result = 31 * result + to.hashCode();
            return result;
        }
    }

    private static HashMap<LengthConversionKey,Double> lengthConversionTable;
    private static HashMap<VolumeConversionKey,Double> volumeConversionTable;

    static {
        volumeConversionTable = new HashMap<VolumeConversionKey,Double>();
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Liters, VolumeUnits.Liters ), 1.0);
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Liters, VolumeUnits.Gallons ), 3.78541);
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Liters, VolumeUnits.Quarts ), 0.946353);
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Gallons, VolumeUnits.Liters ), 0.264172);
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Gallons, VolumeUnits.Gallons ), 1.0);
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Gallons, VolumeUnits.Quarts ), 0.25);
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Quarts, VolumeUnits.Liters ), 1.05669);
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Quarts, VolumeUnits.Gallons ), 4.0);
        volumeConversionTable.put(new VolumeConversionKey(VolumeUnits.Quarts, VolumeUnits.Quarts ), 1.0);

        lengthConversionTable = new HashMap<LengthConversionKey,Double>();
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Meters, LengthUnits.Meters ), 1.0);
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Meters, LengthUnits.Yards ), 0.9144);
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Meters, LengthUnits.Miles ), 1609.34);
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Yards, LengthUnits.Meters ), 1.09361);
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Yards, LengthUnits.Yards ), 1.0);
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Yards, LengthUnits.Miles ), 1760.0);
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Miles, LengthUnits.Meters ), 0.000621371);
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Miles, LengthUnits.Yards ), 0.000568182);
        lengthConversionTable.put(new LengthConversionKey(LengthUnits.Miles, LengthUnits.Miles ), 1.0);

    }

    /**
     * computes volume conversions. For example:
     *
     * double lenVal = UnitsConverter.convert(10.0, VolumeUnits.Gallons,VolumeUnits.Liters)
     *
     * @param val the value to be converted
     * @param fromUnits the units of the value to be converted
     * @param toUnits the units of the converted value
     * @return the converted value
     */
    public static double convert(Double val, VolumeUnits fromUnits, VolumeUnits toUnits) {
        VolumeConversionKey key = new VolumeConversionKey(toUnits,fromUnits);
        return val * volumeConversionTable.get(key).doubleValue();
    }

    /**
     * computes length conversions. For example:
     *
     * double lenVal = UnitsConverter.convert(100.0, LengthUnits.Miles,LengthUnits.Yards)
     *
     * @param val the value to be converted
     * @param fromUnits the units of the value to be converted
     * @param toUnits the units of the converted value
     * @return the converted value
     */
    public static double convert(Double val, LengthUnits fromUnits, LengthUnits toUnits) {
        LengthConversionKey key = new LengthConversionKey(toUnits,fromUnits);
        return val * lengthConversionTable.get(key).doubleValue();
    }
}
