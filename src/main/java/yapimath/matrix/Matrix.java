package yapimath.matrix;

import yapimath.Fraction;

import java.math.MathContext;
import java.math.RoundingMode;

public class Matrix {

    public static void main(String[] args) {
        Matrix matrix1 = new Matrix(3, 3);
        matrix1.set(0, 0, Fraction.valueOf(8));
        matrix1.set(1, 0, Fraction.valueOf(6));
        matrix1.set(2, 0, Fraction.valueOf(9));
        matrix1.set(0, 1, Fraction.valueOf(5));
        matrix1.set(1, 1, Fraction.valueOf(3));
        matrix1.set(2, 1, Fraction.valueOf(4));
        matrix1.set(0, 2, Fraction.valueOf(1));
        matrix1.set(1, 2, Fraction.valueOf(7));
        matrix1.set(2, 2, Fraction.valueOf(2));

        Matrix matrix2 = new Matrix(3, 3);
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

        Matrix matrix3 = new Matrix(3, 3);
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
    }

    private Fraction[][] fractions;
    private final int rows, columns;

    private MathContext precision = new MathContext(200, RoundingMode.HALF_UP);

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        fractions = new Fraction[columns][rows];
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                fractions[j][i] = Fraction.ZERO;
                fractions[j][i].setPrecision(precision);
            }
        }
    }

    public Fraction get(int row, int column) {
        return fractions[column][row];
    }

    public void set(int row, int column, Fraction fraction) {
        if (fraction == null) return;
        fractions[column][row] = fraction;
    }

    private void checkSize(Matrix matrix) {
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

    private void checkMultiply(Matrix matrix) {
        if (columns != matrix.rows) {
            throw new IllegalArgumentException();
        }
        if (rows != matrix.columns) {
            throw new IllegalArgumentException();
        }
    }

    public Matrix add(Matrix matrix) {
        checkSize(matrix);
        return copy(matrixValue -> {
            matrixValue.fraction = matrixValue.fraction.add(matrix.get(matrixValue.row, matrixValue.column));
            return matrixValue;
        });
    }

    public Matrix sub(Matrix matrix) {
        checkSize(matrix);
        return copy(matrixValue -> {
            matrixValue.fraction = matrixValue.fraction.subtract(matrix.get(matrixValue.row, matrixValue.column));
            return matrixValue;
        });
    }

    public Matrix multiply(Fraction fraction) {
        return copy(matrixValue -> {
            matrixValue.fraction = matrixValue.fraction.multiply(fraction);
            return matrixValue;
        });
    }

    public Matrix multiply(Matrix matrix) {
        checkMultiply(matrix);
        Matrix result = new Matrix(rows, matrix.columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                result.set(i, j, skalarProduct(getRow(i), matrix.getColumn(j)));
            }
        }
        return result;
    }

    public Matrix power(int power) {
        Matrix result = this;
        for (int i = 0; i < power; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    public Matrix divide(Fraction fraction) {
        return copy(matrixValue -> {
            matrixValue.fraction = matrixValue.fraction.divide(fraction);
            return matrixValue;
        });
    }

    @SuppressWarnings({"java:S2234"})
    public Matrix transpose() {
        return copy(columns, rows, matrixValue -> {
            int temp = matrixValue.column;
            matrixValue.column = matrixValue.row;
            matrixValue.row = temp;
            return matrixValue;
        });
    }

    public Matrix adjugate() {
        checkQuadratic("No adjugate, Matrix needs to be quadratic");
        return copy(matrixValue -> {
            Fraction determinant = subMatrix(matrixValue.column, matrixValue.row).det();
            if (((matrixValue.column % 2) + (matrixValue.row % 2)) % 2 == 1) {
                determinant = determinant.multiply(Fraction.NEGATION);
            }
            matrixValue.fraction = determinant;
            return matrixValue;
        }).transpose();
    }

    public Matrix inverse() {
        checkQuadratic("No inverse, Matrix needs to be quadratic");
        return adjugate().multiply(Fraction.ONE.divide(det()));
    }

    public Fraction det() {
        checkQuadratic("No determinant, Matrix needs to be quadratic");
        if (rows == 1 || columns == 1) {
            return get(0, 0);
        }

        Fraction fraction = Fraction.ZERO;
        for (int i = 0; i < columns; i++) {
            Fraction det = get(0, i).multiply(subMatrix(i, 0).det());
            fraction = (i % 2 == 0) ? fraction.add(det) : fraction.subtract(det);
        }
        return fraction;
    }

    private Matrix subMatrix(int column, int row) {
        Matrix matrix = new Matrix(rows - 1, columns - 1);
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

    private Fraction[] getRow(int row) {
        Fraction[] fractions = new Fraction[columns];
        for (int i = 0; i < columns; i++) {
            fractions[i] = get(row, i);
        }
        return fractions;
    }

    private Fraction[] getColumn(int column) {
        Fraction[] fractions = new Fraction[rows];
        for (int i = 0; i < rows; i++) {
            fractions[i] = get(i, column);
        }
        return fractions;
    }

    private Fraction skalarProduct(Fraction[] fractions1, Fraction[] fractions2) {
        if (fractions1.length != fractions2.length) {
            throw new IllegalArgumentException();
        }
        Fraction fraction = Fraction.ZERO;
        for (int i = 0; i < fractions1.length; i++) {
            fraction = fraction.add(fractions1[i].multiply(fractions2[i]));
        }
        return fraction;
    }

    public Matrix copy() {
        return copy(matrixValue -> matrixValue);
    }

    private Matrix copy(MatrixFunction matrixFunction) {
        return copy(rows, columns, matrixFunction);
    }

    private Matrix copy(int rows, int columns, MatrixFunction matrixFunction) {
        Matrix matrix = new Matrix(rows, columns);
        for (int j = 0; j < this.columns; j++) {
            for (int i = 0; i < this.rows; i++) {
                MatrixValue matrixValue = new MatrixValue(get(i, j), i, j);
                matrixValue = matrixFunction.apply(matrixValue);
                matrix.set(matrixValue.row, matrixValue.column, matrixValue.fraction);
            }
        }
        return matrix;
    }

    @FunctionalInterface
    private interface MatrixFunction {
        MatrixValue apply(MatrixValue matrixValue);
    }

    private static class MatrixValue {
        private Fraction fraction;
        private int row;
        private int column;

        public MatrixValue(Fraction fraction, int row, int column) {
            this.fraction = fraction;
            this.row = row;
            this.column = column;
        }
    }

    @Override
    public String toString() {
        int max = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                max = Math.max(max, get(j, i).encodeFlat().length());
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

    private String pad(Fraction fraction, int length) {
        String s = fraction.encodeFlat();
        return s + pad.substring(0, length - s.length());
    }

}
