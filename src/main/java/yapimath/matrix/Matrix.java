package yapimath.matrix;

import yapimath.ComplexFraction;
import yapimath.Fraction;
import yapimath.number.ConstantHolder;
import yapimath.number.MatrixNumber;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Matrix<T extends MatrixNumber<T>> {

    public static void main(String[] args) {
        Matrix<Fraction> matrix1 = Matrix.createFractionMatrix(3, 3);
        matrix1.set(0, 0, Fraction.valueOf(8));
        matrix1.set(1, 0, Fraction.valueOf(6));
        matrix1.set(2, 0, Fraction.valueOf(9));
        matrix1.set(0, 1, Fraction.valueOf(5));
        matrix1.set(1, 1, Fraction.valueOf(3));
        matrix1.set(2, 1, Fraction.valueOf(4));
        matrix1.set(0, 2, Fraction.valueOf(1));
        matrix1.set(1, 2, Fraction.valueOf(7));
        matrix1.set(2, 2, Fraction.valueOf(2));

        Matrix<Fraction> matrix2 = Matrix.createFractionMatrix(3, 3);
        matrix2.set(0, 0, Fraction.valueOf(6));
        matrix2.set(1, 0, Fraction.valueOf(9));
        matrix2.set(2, 0, Fraction.valueOf(8));
        matrix2.set(0, 1, Fraction.valueOf(3));
        matrix2.set(1, 1, Fraction.valueOf(4));
        matrix2.set(2, 1, Fraction.valueOf(5));
        matrix2.set(0, 2, Fraction.valueOf(7));
        matrix2.set(1, 2, Fraction.valueOf(2));
        matrix2.set(2, 2, Fraction.valueOf(1));

        System.out.println(matrix1.multiply(matrix2));
        System.out.println();
        System.out.println(matrix2.multiply(matrix1));
        System.out.println();
        System.out.println(matrix1.multiply(matrix2).divide(matrix2));
        System.out.println();
        System.out.println(matrix1.divide(matrix2));
        System.out.println();
        System.out.println(matrix1.adjugate());
        System.out.println();
        System.out.println(matrix1.inverse());
        System.out.println();
        System.out.println(matrix1);
        System.out.println();
        System.out.println(matrix1.transpose());
        System.out.println();
        System.out.println(matrix1.inverse().multiply(matrix1));
        System.out.println();

        Matrix<Fraction> matrix3 = Matrix.createFractionMatrix(3, 3);
        matrix3.set(0, 0, Fraction.valueOf(0.6));
        matrix3.set(1, 0, Fraction.valueOf(0.15));
        matrix3.set(2, 0, Fraction.valueOf(0.25));
        matrix3.set(0, 1, Fraction.valueOf(0.05));
        matrix3.set(1, 1, Fraction.valueOf(0.8));
        matrix3.set(2, 1, Fraction.valueOf(0.15));
        matrix3.set(0, 2, Fraction.valueOf(0.2));
        matrix3.set(1, 2, Fraction.valueOf(0.05));
        matrix3.set(2, 2, Fraction.valueOf(0.75));

        System.out.println(matrix3.multiply(matrix3));
        System.out.println();
        System.out.println(matrix3.inverse().multiply(matrix3));
        System.out.println();

        Matrix<ComplexFraction> matrix4 = Matrix.createComplexFractionMatrix(3, 3);
        matrix4.set(0, 0, ComplexFraction.valueOf(0.6, 0.5));
        matrix4.set(1, 0, ComplexFraction.valueOf(0.15));
        matrix4.set(2, 0, ComplexFraction.valueOf(0.25));
        matrix4.set(0, 1, ComplexFraction.valueOf(0.05));
        matrix4.set(1, 1, ComplexFraction.valueOf(0.8));
        matrix4.set(2, 1, ComplexFraction.valueOf(0.15));
        matrix4.set(0, 2, ComplexFraction.valueOf(0.2));
        matrix4.set(1, 2, ComplexFraction.valueOf(0.05));
        matrix4.set(2, 2, ComplexFraction.valueOf(0.75));
        System.out.println(matrix4);
        System.out.println();
        System.out.println(matrix4.multiply(matrix4));
        System.out.println();
        System.out.println(matrix4.multiply(matrix4).divide(matrix4));
    }

    public static Matrix<Fraction> createFractionMatrix(int rows, int columns) {
        return new Matrix<>(rows, columns, (irows, icolumns) -> new Fraction[icolumns][irows], Fraction.FRACTION_CONSTANT_HOLDER, Fraction::encodeFlat);
    }

    public static Matrix<ComplexFraction> createComplexFractionMatrix(int rows, int columns) {
        return new Matrix<>(rows, columns, (irows, icolumns) -> new ComplexFraction[icolumns][irows], ComplexFraction.COMPLEX_FRACTION_CONSTANT_HOLDER, ComplexFraction::encodeFlat);
    }

    private BiFunction<Integer, Integer, T[][]> arrayCreator;
    private ConstantHolder<T> constantHolder;
    private Function<T, String> toStringFunction;

    private T[][] numbers;
    private final int rows, columns;

    private MathContext precision = new MathContext(200, RoundingMode.HALF_UP);

    private Matrix(int rows, int columns, BiFunction<Integer, Integer, T[][]> arrayCreator, ConstantHolder<T> constantHolder, Function<T, String> toStringFunction) {
        this.rows = rows;
        this.columns = columns;

        this.arrayCreator = arrayCreator;
        this.constantHolder = constantHolder;
        this.toStringFunction = toStringFunction;

        numbers = arrayCreator.apply(rows, columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                numbers[j][i] = constantHolder.getZero();
                numbers[j][i].setPrecision(precision);
            }
        }
    }

    public T get(int row, int column) {
        return numbers[column][row];
    }

    public void set(int row, int column, T number) {
        if (number == null) return;
        numbers[column][row] = number;
    }

    private void checkSize(Matrix<T> matrix) {
        if (rows != matrix.rows) {
            throw new IllegalArgumentException();
        }
        if (columns != matrix.columns) {
            throw new IllegalArgumentException();
        }
    }

    private void checkQuadratic(String message) {
        if (rows != columns) {
            throw new IllegalArgumentException(message);
        }
    }

    private void checkMultiply(Matrix<T> matrix) {
        if (columns != matrix.rows) {
            throw new IllegalArgumentException();
        }
        if (rows != matrix.columns) {
            throw new IllegalArgumentException();
        }
    }

    public Matrix<T> add(Matrix<T> matrix) {
        checkSize(matrix);
        return copy(matrixValue -> {
            matrixValue.number = matrixValue.number.add(matrix.get(matrixValue.row, matrixValue.column));
            return matrixValue;
        });
    }

    public Matrix<T> sub(Matrix<T> matrix) {
        checkSize(matrix);
        return copy(matrixValue -> {
            matrixValue.number = matrixValue.number.subtract(matrix.get(matrixValue.row, matrixValue.column));
            return matrixValue;
        });
    }

    public Matrix<T> multiply(T number) {
        return copy(matrixValue -> {
            matrixValue.number = matrixValue.number.multiply(number);
            return matrixValue;
        });
    }

    public Matrix<T> multiply(Matrix<T> matrix) {
        checkMultiply(matrix);
        Matrix<T> result = new Matrix<>(rows, matrix.columns, arrayCreator, constantHolder, toStringFunction);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                result.set(i, j, skalarProduct(getRow(i), matrix.getColumn(j)));
            }
        }
        return result;
    }

    public Matrix<T> square() {
        return power(2);
    }

    public Matrix<T> cube() {
        return power(3);
    }

    public Matrix<T> power(int power) {
        Matrix<T> result = this;
        for (int i = 0; i < power; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    public Matrix<T> divide(T number) {
        return copy(matrixValue -> {
            matrixValue.number = matrixValue.number.divide(number);
            return matrixValue;
        });
    }

    public Matrix<T> divide(Matrix<T> matrix) {
        return multiply(matrix.inverse());
    }

    @SuppressWarnings({"java:S2234"})
    public Matrix<T> transpose() {
        return copy(columns, rows, matrixValue -> {
            int temp = matrixValue.column;
            matrixValue.column = matrixValue.row;
            matrixValue.row = temp;
            return matrixValue;
        });
    }

    public Matrix<T> adjugate() {
        checkQuadratic("No adjugate, Matrix needs to be quadratic");
        return copy(matrixValue -> {
            T determinant = subMatrix(matrixValue.column, matrixValue.row).det();
            if (((matrixValue.column % 2) + (matrixValue.row % 2)) % 2 == 1) {
                determinant = determinant.multiply(constantHolder.getNegation());
            }
            matrixValue.number = determinant;
            return matrixValue;
        }).transpose();
    }

    public Matrix<T> inverse() {
        checkQuadratic("No inverse, Matrix needs to be quadratic");
        return adjugate().multiply(constantHolder.getOne().divide(det()));
    }

    public T det() {
        checkQuadratic("No determinant, Matrix needs to be quadratic");
        if (rows == 1 || columns == 1) {
            return get(0, 0);
        }

        T number = constantHolder.getZero();
        for (int i = 0; i < columns; i++) {
            T det = get(0, i).multiply(subMatrix(i, 0).det());
            number = (i % 2 == 0) ? number.add(det) : number.subtract(det);
        }
        return number;
    }

    private Matrix<T> subMatrix(int column, int row) {
        Matrix<T> matrix = new Matrix<>(rows - 1, columns - 1, arrayCreator, constantHolder, toStringFunction);
        int internalRow = 0;
        int internalColumn = 0;
        for (int i = 0; i < columns; i++) {
            if (i == column) continue;
            for (int j = 0; j < rows; j++) {
                if (j == row) continue;
                matrix.set(internalRow, internalColumn, get(j, i));
                internalRow++;
            }
            internalColumn++;
            internalRow = 0;
        }
        return matrix;
    }

    private T[] getRow(int row) {
        T[] numbers = arrayCreator.apply(columns, 1)[0];
        for (int i = 0; i < columns; i++) {
            numbers[i] = get(row, i);
        }
        return numbers;
    }

    private T[] getColumn(int column) {
        T[] numbers = arrayCreator.apply(rows, 1)[0];
        for (int i = 0; i < rows; i++) {
            numbers[i] = get(i, column);
        }
        return numbers;
    }

    private T skalarProduct(T[] fractions1, T[] fractions2) {
        if (fractions1.length != fractions2.length) {
            throw new IllegalArgumentException();
        }
        T number = constantHolder.getZero();
        for (int i = 0; i < fractions1.length; i++) {
            number = number.add(fractions1[i].multiply(fractions2[i]));
        }
        return number;
    }

    public Matrix<T> copy() {
        return copy(matrixValue -> matrixValue);
    }

    private Matrix<T> copy(MatrixFunction<T> matrixFunction) {
        return copy(rows, columns, matrixFunction);
    }

    private Matrix<T> copy(int rows, int columns, MatrixFunction<T> matrixFunction) {
        Matrix<T> matrix = new Matrix<>(rows, columns, arrayCreator, constantHolder, toStringFunction);
        for (int j = 0; j < this.columns; j++) {
            for (int i = 0; i < this.rows; i++) {
                MatrixValue<T> matrixValue = new MatrixValue<>(get(i, j), i, j);
                matrixValue = matrixFunction.apply(matrixValue);
                matrix.set(matrixValue.row, matrixValue.column, matrixValue.number);
            }
        }
        return matrix;
    }

    @FunctionalInterface
    private interface MatrixFunction<T> {
        MatrixValue<T> apply(MatrixValue<T> matrixValue);
    }

    private static class MatrixValue<T> {
        private T number;
        private int row;
        private int column;

        public MatrixValue(T number, int row, int column) {
            this.number = number;
            this.row = row;
            this.column = column;
        }
    }

    @Override
    public String toString() {
        int max = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                max = Math.max(max, toStringFunction.apply(get(j, i)).length());
            }
        }

        StringBuilder st = new StringBuilder();
        for (int j = 0; j < rows; j++) {
            if (j != 0) st.append("\n");
            for (int i = 0; i < columns; i++) {
                if (i != 0) st.append("  ");
                st.append(pad(get(j, i), max));
            }
        }
        return st.toString();
    }

    private final String pad = "                                                                ";

    private String pad(T number, int length) {
        String s = toStringFunction.apply(number);
        return s + pad.substring(0, length - s.length());
    }

}
