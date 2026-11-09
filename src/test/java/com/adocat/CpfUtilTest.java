package com.adocat;

import com.adocat.util.CpfUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Teste unitário simples: não precisa de banco de dados para rodar. */
class CpfUtilTest {

    @Test
    void aceitaCpfValido() {
        assertTrue(CpfUtil.valido(CpfUtil.somenteDigitos("529.982.247-25")));
    }

    @Test
    void recusaCpfInvalido() {
        assertFalse(CpfUtil.valido("52998224726"));
        assertFalse(CpfUtil.valido("11111111111"));
        assertFalse(CpfUtil.valido("123"));
        assertFalse(CpfUtil.valido(null));
    }

    @Test
    void hashComSegredoDifereDoHashSemSegredo() {
        String cpf = "52998224725";
        String semSegredo = CpfUtil.hash(cpf, "");
        String comSegredo = CpfUtil.hash(cpf, "segredo-de-teste");
        assertEquals(64, semSegredo.length());
        assertEquals(64, comSegredo.length());
        assertNotEquals(semSegredo, comSegredo);
        assertEquals(comSegredo, CpfUtil.hash(cpf, "segredo-de-teste"));
    }
}
