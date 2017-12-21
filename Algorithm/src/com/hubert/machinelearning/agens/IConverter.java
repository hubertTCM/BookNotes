package com.hubert.machinelearning.agens;

import java.util.*;

public interface IConverter<T, U> {
    public Set<U> convert(T x);
}
