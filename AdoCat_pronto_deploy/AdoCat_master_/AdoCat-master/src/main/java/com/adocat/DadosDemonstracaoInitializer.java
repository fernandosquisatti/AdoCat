package com.adocat;

import com.adocat.model.Doacao;
import com.adocat.model.Gato;
import com.adocat.model.Pagamento;
import com.adocat.model.Resgate;
import com.adocat.repository.DoacaoRepository;
import com.adocat.repository.GatoRepository;
import com.adocat.repository.PagamentoRepository;
import com.adocat.repository.ResgateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DadosDemonstracaoInitializer implements CommandLineRunner {
    private final GatoRepository gatos;
    private final DoacaoRepository doacoes;
    private final PagamentoRepository pagamentos;
    private final ResgateRepository resgates;

    public DadosDemonstracaoInitializer(GatoRepository gatos, DoacaoRepository doacoes,
                                        PagamentoRepository pagamentos, ResgateRepository resgates) {
        this.gatos = gatos;
        this.doacoes = doacoes;
        this.pagamentos = pagamentos;
        this.resgates = resgates;
    }

    @Override
    @Transactional
    public void run(String... args) {
        renomearGatosDemo();
        migrarDoacoesManuais();
        criarResgatesFicticiosSeNecessario();
    }

    private void renomearGatosDemo() {
        List<String> nomes = List.of("Nino", "Amora", "Pipoca", "Theo", "Mel", "Romeu",
                "Luna", "Chico", "Mingau", "Jujuba", "Bento", "Frida", "Simba",
                "Cacau", "Tom", "Nina", "Fred", "Mimi", "Tobias", "Aurora");
        AtomicInteger indice = new AtomicInteger();
        for (Gato gato : gatos.findAll()) {
            String nome = gato.getNome() == null ? "" : gato.getNome().trim();
            if (nome.toLowerCase(Locale.ROOT).contains("demo")) {
                String novoNome = nomes.get(indice.getAndIncrement() % nomes.size());
                gato.setNome(novoNome);
                gatos.save(gato);
            }
        }
    }

    private void migrarDoacoesManuais() {
        // Pagamentos antigos marcados como MANUAL passam a compor Pix e Cartão.
        AtomicInteger alternador = new AtomicInteger();
        for (Pagamento pagamento : pagamentos.findAll()) {
            if ("MANUAL".equalsIgnoreCase(String.valueOf(pagamento.getFormaPagamento()))) {
                pagamento.setFormaPagamento(alternador.getAndIncrement() % 2 == 0 ? "PIX" : "CARTAO");
                pagamentos.save(pagamento);
            }
        }

        // Registros da tabela legada de doações também são convertidos e preservados.
        for (Doacao doacao : doacoes.findAll()) {
            Pagamento pagamento = new Pagamento();
            pagamento.setNomePagador(doacao.getNomeDoador());
            pagamento.setValor(doacao.getValor());
            pagamento.setFormaPagamento(alternador.getAndIncrement() % 2 == 0 ? "PIX" : "CARTAO");
            pagamento.setStatus("APROVADO");
            pagamento.setDataPagamento((doacao.getDataDoacao() == null ? LocalDate.now() : doacao.getDataDoacao()).atTime(12, 0));
            pagamentos.save(pagamento);
        }
        if (doacoes.count() > 0) {
            doacoes.deleteAll();
        }
    }

    private void criarResgatesFicticiosSeNecessario() {
        List<String> nomesExistentes = resgates.findAll().stream()
                .map(Resgate::getNomeAnimal)
                .filter(n -> n != null)
                .map(n -> n.trim().toLowerCase(Locale.ROOT))
                .toList();
        List<Resgate> exemplos = List.of(
                resgate("Bidu", LocalDate.of(2026, 8, 14), "Jardim Primavera",
                        "REGISTRO FICTÍCIO — Gato encontrado próximo a uma praça, assustado e com sinais de desidratação. Foi recolhido com segurança e encaminhado para avaliação.",
                        "Desidratado, sem ferimentos aparentes", "Equipe AdoCat (fictício)", "CONCLUIDO"),
                resgate("Mimo", LocalDate.of(2026, 9, 3), "Vila Esperança",
                        "REGISTRO FICTÍCIO — Filhote localizado em uma garagem após moradores relatarem miados. Recebeu alimentação, higienização e avaliação veterinária.",
                        "Filhote, baixo peso", "Voluntária Camila (fictício)", "ENCAMINHADO"),
                resgate("Frajola", LocalDate.of(2026, 10, 21), "Parque das Flores",
                        "REGISTRO FICTÍCIO — Animal resgatado em área residencial. Está em observação, recebendo cuidados e aguardando definição de lar temporário.",
                        "Em observação, ferimento superficial", "Equipe de resgate (fictício)", "EM_ATENDIMENTO")
        );
        for (Resgate exemplo : exemplos) {
            String nome = exemplo.getNomeAnimal().toLowerCase(Locale.ROOT);
            if (!nomesExistentes.contains(nome)) resgates.save(exemplo);
        }
    }

    private Resgate resgate(String animal, LocalDate data, String local, String descricao,
                            String condicao, String responsavel, String status) {
        Resgate r = new Resgate();
        r.setNomeAnimal(animal);
        r.setDataResgate(data);
        r.setLocalizacao(local);
        r.setDescricao(descricao);
        r.setCondicaoAnimal(condicao);
        r.setResponsavel(responsavel);
        r.setStatus(status);
        return r;
    }
}
