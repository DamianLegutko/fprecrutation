package pl.damianlegutko.fprecrutation.user;

import java.math.BigDecimal;

class DoubleOperations {
    static double add(double first, double second) {
        BigDecimal firstDecimal = new BigDecimal(first);
        BigDecimal secondDecimal = new BigDecimal(second);

        return firstDecimal.add(secondDecimal).doubleValue();
    }

    static double subtract(double first, double second) {
        BigDecimal firstDecimal = new BigDecimal(first);
        BigDecimal secondDecimal = new BigDecimal(second);

        return firstDecimal.subtract(secondDecimal).doubleValue();
    }

    static int compare(double first, double second) {
        BigDecimal firstDecimal = new BigDecimal(first);
        BigDecimal secondDecimal = new BigDecimal(second);

        return firstDecimal.compareTo(secondDecimal);
    }
}