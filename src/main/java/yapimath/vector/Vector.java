package yapimath.vector;

import java.util.Arrays;

public class Vector {

    private double[] vec;

    public Vector(int dimensions) {
        vec = new double[dimensions];
        Arrays.fill(vec, 0);
    }

    public Vector(double... values) {
        vec = values;
    }


    private double[] getVec() {
        return vec;
    }

    private double[] invertVector() {
        double[] vector = this.vec;
        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i] * -1;
        }
        return vector;
    }


    /**
     *
     * @since Version 1
     *
     * @param dimension
     * @param value
     */
    public void setVector(int dimension, double value) {
        if (!(dimension < 0 || dimension > vec.length)) {
            vec[dimension] = value;
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param vec
     */
    public void setVec(double[] vec) {
        if (this.vec.length == vec.length) {
            this.vec = vec;
        }
    }


    /**
     *
     * @since Version 1
     *
     * @param vector
     */
    public void addVector(Vector vector) {
        if (this.vec.length == vector.vec.length) {
            int i = 0;
            for (double j : vector.getVec()) {
                this.vec[i] += j;
                i++;
            }
        } else if (this.vec.length > vector.vec.length) {
            try {
                int i = 0;
                for (double j : vector.getVec()) {
                    this.vec[i] += j;
                    i++;
                }
            } catch (IndexOutOfBoundsException e) {
                // Ignore the IndexOutOfBoundsException
            }
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param vector
     */
    public void subtractVector(Vector vector) {
        if (this.vec.length == vector.vec.length) {
            int i = 0;
            for (double j : vector.invertVector()) {
                this.vec[i] += j;
                i++;
            }
        } else if (this.vec.length > vector.vec.length) {
            try {
                int i = 0;
                for (double j : vector.invertVector()) {
                    this.vec[i] += j;
                    i++;
                }
            } catch (IndexOutOfBoundsException e) {
                // Ignore the IndexOutOfBoundsException
            }
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param r
     */
    public void multiplyVector(double r) {
        for (int i = 0; i < vec.length; i++) {
            vec[i] *= r;
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param r
     */
    public void divideVector(double r) {
        for (int i = 0; i < vec.length; i++) {
            vec[i] /= r;
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param vector
     * @return
     */
    public double multiplyVectorScalar(Vector vector) {
        double value = 0;
        try {
            for (int i = 0; i < this.vec.length; i++) {
                value += this.vec[i] * vector.getVec()[i];
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore the IndexOutOfBoundsException
        }
        return Math.acos(value / (length() * vector.length()));
    }

    /**
     *
     * @since Version 1
     *
     * @param vector
     */
    public void crossProduct(Vector vector) {
        if (this.vec.length != 3 || vector.vec.length != 3) {
            return;
        }
        double[] doubles = new double[3];
        doubles[0] = this.vec[1] * vector.vec[2] - this.vec[2] * vector.vec[1];
        doubles[1] = this.vec[2] * vector.vec[0] - this.vec[0] * vector.vec[2];
        doubles[2] = this.vec[0] * vector.vec[1] - this.vec[1] * vector.vec[0];
        vec = doubles;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public double length() {
        double value = 0;
        for (int i = 0; i < vec.length; i++) {
            value += vec[i] * vec[i];
        }
        return Math.sqrt(value);
    }

    public int size() {
        return vec.length;
    }

    /**
     *
     * @since Version 1
     *
     * @param i
     * @return
     */
    public double get(int i) {
        return vec[i];
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public Vector copy() {
        return new Vector(Arrays.copyOf(vec, vec.length));
    }


    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("Vector[ ");
        for (int i = 0; i < vec.length; i++) {
            if (i == 0) {
                st.append("x=").append(vec[i]);
            } else if (i == 1) {
                st.append("y=").append(vec[i]);
            } else if (i == 2) {
                st.append("z=").append(vec[i]);
            } else if (i == 3) {
                st.append("w=").append(vec[i]);
            } else {
                st.append((i + 1)).append("=").append(vec[i]);
            }
            if (i < vec.length) {
                st.append(" ");
            }
        }
        st.append("]");
        return st.toString();
    }

    String toMatrixString() {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < vec.length; i++) {
            if (i != 0) {
                st.append(", ");
            }
            st.append(vec[i]);
        }
        return st.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        Vector vector = (Vector) o;
        return Arrays.equals(vec, vector.vec);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vec);
    }

}