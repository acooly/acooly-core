package com.acooly.core.utils.id;

public interface IdentifierAnalyzer<T, E> {
  E analyze(T paramT);
}
