package yapimath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ComplexFraction {

    private Fraction real;
    private Fraction imaginary;

    private MathContext precision = new MathContext(200, RoundingMode.HALF_UP);

    public ComplexFraction(Fraction real) {
        this(real, Fraction.ZERO);
    }

    public ComplexFraction(Fraction real, Fraction imaginary) {
        this.real = real;
        this.imaginary = imaginary;

        this.real.setPrecision(precision);
        this.imaginary.setPrecision(precision);
    }

    public ComplexFraction add(Fraction fraction) {
        return add(new ComplexFraction(fraction));
    }

    public ComplexFraction add(Fraction real, Fraction imaginary) {
        return add(new ComplexFraction(real, imaginary));
    }

    public ComplexFraction add(ComplexFraction complexFraction) {
        return new ComplexFraction(real.add(complexFraction.real), imaginary.add(complexFraction.imaginary));
    }

    public ComplexFraction subtract(Fraction fraction) {
        return subtract(new ComplexFraction(fraction));
    }

    public ComplexFraction subtract(Fraction real, Fraction imaginary) {
        return subtract(new ComplexFraction(real, imaginary));
    }

    public ComplexFraction subtract(ComplexFraction complexFraction) {
        return new ComplexFraction(real.subtract(complexFraction.real), imaginary.subtract(complexFraction.imaginary));
    }

    public ComplexFraction multiply(Fraction fraction) {
        return multiply(new ComplexFraction(fraction));
    }

    public ComplexFraction multiply(Fraction real, Fraction imaginary) {
        return multiply(new ComplexFraction(real, imaginary));
    }

    public ComplexFraction multiply(ComplexFraction complexFraction) {
        return new ComplexFraction(real.multiply(complexFraction.real).subtract(imaginary.multiply(complexFraction.imaginary)), real.multiply(complexFraction.real).add(imaginary.multiply(complexFraction.imaginary)));
    }

    public ComplexFraction scale(Fraction fraction) {
        return new ComplexFraction(real.multiply(fraction), imaginary.multiply(fraction));
    }

    public ComplexFraction divide(Fraction fraction) {
        return divide(new ComplexFraction(fraction));
    }

    public ComplexFraction divide(Fraction real, Fraction imaginary) {
        return divide(new ComplexFraction(real, imaginary));
    }

    public ComplexFraction divide(ComplexFraction complexFraction) {
        return multiply(complexFraction.reciprocal());
    }

    public ComplexFraction conjugate() {
        return new ComplexFraction(imaginary, real);
    }

    public ComplexFraction reciprocal() {
        if (real.power(2).add(imaginary.power(2)).getBigDecimal().compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Complex number needs to be non-zero complex number.");
        }
        Fraction div = real.power(2).add(imaginary.power(2));
        return new ComplexFraction(real.divide(div), imaginary.divide(div).multiply(Fraction.NEGATION));
    }

    public ComplexFraction[] sqrt() {
        ComplexFraction[] complexFractions = new ComplexFraction[2];
        if (imaginary.compareTo(Fraction.ZERO) == 0) {
            if (real.compareTo(Fraction.ZERO) == 0) {
                complexFractions[0] = new ComplexFraction(real);
                complexFractions[1] = new ComplexFraction(real);
            } else {
                complexFractions[0] = new ComplexFraction(real.sqrt());
                complexFractions[1] = new ComplexFraction(real.sqrt().multiply(Fraction.NEGATION));
            }
        } else {
            Fraction sqrt = real.power(2).add(imaginary.power(2)).sqrt();
            Fraction nR = real.add(sqrt).divide(Fraction.TWO).sqrt();
            Fraction signum = Fraction.NEGATION;
            if (imaginary.getBigDecimal().signum() > 0) {
                signum = Fraction.ONE;
            }
            Fraction nI = signum.multiply(real.multiply(Fraction.NEGATION).add(sqrt).divide(Fraction.TWO).sqrt());
            complexFractions[0] = new ComplexFraction(nR, nI);
            complexFractions[1] = new ComplexFraction(nR.multiply(Fraction.NEGATION), nI.multiply(Fraction.NEGATION));
        }
        return complexFractions;
    }

}
