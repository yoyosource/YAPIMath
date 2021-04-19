package yapimath.number;

import java.math.MathContext;

public interface MatrixNumber<T> {

    T add(T value);

    T subtract(T value);

    T multiply(T value);

    T divide(T value);

    void setPrecision(MathContext precision);

}
