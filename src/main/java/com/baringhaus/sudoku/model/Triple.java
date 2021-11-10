package com.baringhaus.sudoku.model;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Objects;

public class Triple<A, B, C extends Comparable<C>> implements Comparable<Triple<A,B,C>>{
    public final A fst;
    public final B snd;
    public final C thd;

    public Triple(A var1, B var2, C var3) {
        this.fst = var1;
        this.snd = var2;
        this.thd = var3;
    }

    public String toString() {
        return "Triple[" + this.fst + "," + this.snd + "," + this.thd + "]";
    }

    public boolean equals(Triple<A,B,C> var1) {
        return var1 != null
                && Objects.equals(this.fst, var1.fst)
                && Objects.equals(this.snd, var1.snd)
                && Objects.equals(this.thd, var1.thd);
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(fst).
                append(snd).
                append(thd).
                toHashCode();
    }

    public static <A, B, C extends Comparable<C>> Triple<A, B, C> of(A var0, B var1, C var2) {
        return new Triple<>(var0, var1, var2);
    }

    @Override
    public int compareTo(Triple<A, B, C> o) {
        return this.thd.compareTo(o.thd);
    }
}

