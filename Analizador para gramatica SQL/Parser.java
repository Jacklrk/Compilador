/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.analizadorsintacticosql;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int index;
    private Token tokenActual;

    // Tokens predefinidos con sus respectivos tipos y lexemas
    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, "");
    private final Token select = new Token(TipoToken.SELECT, "select");
    private final Token from = new Token(TipoToken.FROM, "from");
    private final Token distinct = new Token(TipoToken.DISTINCT, "distinct");
    private final Token coma = new Token(TipoToken.COMA, ",");
    private final Token punto = new Token(TipoToken.PUNTO, ".");
    private final Token asterisco = new Token(TipoToken.ASTERISCO, "*");
    private final Token finCadena = new Token(TipoToken.EOF, "");

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.index = 0;
        this.tokenActual = tokens.get(0);
    }

    public void parse() {
        // Llamada al símbolo inicial de la gramática
        Q();

        // Verificación de errores de sintaxis
        if (tokenActual.getTipo() != TipoToken.EOF) {
            System.out.println("Error de sintaxis. Se esperaba el final de la cadena.");
        } else {
            System.out.println("Consulta válida.");
        }
    }

    private void avanzar() {
        // Avanza al siguiente token
        index++;
        if (index < tokens.size()) {
            tokenActual = tokens.get(index);
        } else {
            tokenActual = new Token(TipoToken.EOF, "");
        }
    }

    private void coincidir(TipoToken tipo) {
        // Verifica si el tipo del token actual coincide con el tipo esperado
        // Si coincide, avanza al siguiente token. Si no, lanza una excepción.
        if (tokenActual.getTipo() == tipo) {
            avanzar();
        } else {
            throw new RuntimeException("Se esperaba '" + tipo.toString() + "'");
        }
    }

    // Regla Q: SELECT D FROM T
    private void Q() {
        coincidir(TipoToken.SELECT);
        D();
        coincidir(TipoToken.FROM);
        T();
    }

    // Regla D: DISTINCT P | P
    private void D() {
        if (tokenActual.getTipo() == TipoToken.DISTINCT) {
            coincidir(TipoToken.DISTINCT);
            P();
        } else if (tokenActual.getTipo() == TipoToken.ASTERISCO || tokenActual.getTipo() == TipoToken.IDENTIFICADOR) {
            P();
        } else {
            throw new RuntimeException("Se esperaba DISTINCT, * o un identificador");
        }
    }

    // Regla P: * | A
    private void P() {
        if (tokenActual.getTipo() == TipoToken.ASTERISCO) {
            coincidir(TipoToken.ASTERISCO);
        } else if (tokenActual.getTipo() == TipoToken.IDENTIFICADOR) {
            A();
        } else {
            throw new RuntimeException("Se esperaba * o un identificador");
        }
    }

    // Regla A: identificador A2 A1
    private void A() {
        coincidir(TipoToken.IDENTIFICADOR);
        A2();
        A1();
    }

    // Regla A1: , A
    private void A1() {
        if (tokenActual.getTipo() == TipoToken.COMA) {
            coincidir(TipoToken.COMA);
            A();
        }
    }

    // Regla A2: identificador A3
    private void A2() {
        if (tokenActual.getTipo() == TipoToken.IDENTIFICADOR) {
            coincidir(TipoToken.IDENTIFICADOR);
            A3();
        }
    }

    // Regla A3: . identificador
    private void A3() {
        if (tokenActual.getTipo() == TipoToken.PUNTO) {
            coincidir(TipoToken.PUNTO);
            coincidir(TipoToken.IDENTIFICADOR);
        }
    }

    // Regla T: identificador T2 T1
    private void T() {
        coincidir(TipoToken.IDENTIFICADOR);
        T2();
        T1();
    }

    // Regla T1: , T
    private void T1() {
        if (tokenActual.getTipo() == TipoToken.COMA) {
            coincidir(TipoToken.COMA);
            T();
        }
    }

    // Regla T2: identificador T3
    private void T2() {
        if (tokenActual.getTipo() == TipoToken.IDENTIFICADOR) {
            coincidir(TipoToken.IDENTIFICADOR);
            T3();
        }
    }

    // Regla T3: identificador
    private void T3() {
        if (tokenActual.getTipo() == TipoToken.IDENTIFICADOR) {
            coincidir(TipoToken.IDENTIFICADOR);
        }
    }
}