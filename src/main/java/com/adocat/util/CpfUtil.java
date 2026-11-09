package com.adocat.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/** Validação e proteção do CPF (nunca armazenado em texto puro). */
public final class CpfUtil {

    private CpfUtil() {}

    /** Remove tudo que não é dígito. */
    public static String somenteDigitos(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    /** Valida os dois dígitos verificadores. */
    public static boolean valido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
            return false;
        }
        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
        int d1 = 11 - (soma % 11);
        if (d1 >= 10) d1 = 0;
        soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
        int d2 = 11 - (soma % 11);
        if (d2 >= 10) d2 = 0;
        return d1 == cpf.charAt(9) - '0' && d2 == cpf.charAt(10) - '0';
    }

    /**
     * Gera o identificador do CPF. Com segredo, usa HMAC-SHA256 (não dá para
     * recuperar o CPF sem a chave). Sem segredo, cai para SHA-256 simples,
     * aceitável apenas em ambiente de teste.
     */
    public static String hash(String cpf, String segredo) {
        try {
            byte[] dados = cpf.getBytes(StandardCharsets.UTF_8);
            if (segredo == null || segredo.isBlank()) {
                return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(dados));
            }
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(segredo.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(dados));
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao proteger o CPF", e);
        }
    }
}
