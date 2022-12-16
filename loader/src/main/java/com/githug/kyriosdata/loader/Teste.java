package com.githug.kyriosdata.loader;

import java.util.function.Function;

public class Teste implements Function<String, String> {

    public Teste() {
        System.out.println("Teste");
    }

    public static void main(String[] args) {
        Function<String,String> maiuscula = new Teste();
        System.out.println(maiuscula.apply("abc"));
    }

    @Override
    public String apply(String s) {
        return s.toUpperCase();
    }
}
