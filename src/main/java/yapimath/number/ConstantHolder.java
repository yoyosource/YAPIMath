package yapimath.number;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConstantHolder<T extends MatrixNumber<T>> {
    private T zero;
    private T one;
    private T negation;
}
