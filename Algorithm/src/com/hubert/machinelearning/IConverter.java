package com.hubert.machinelearning;

import java.util.*;

public interface IConverter<T, U> {
    public Set<U> convert(T x);
}
