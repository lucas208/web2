package com.projeto.web2.config;

import com.projeto.web2.model.*;
import com.projeto.web2.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer {

    private final FornecedorRepository fornecedorRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ProjetoRepository projetoRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(FornecedorRepository fornecedorRepository,
                           EquipamentoRepository equipamentoRepository,
                           ProjetoRepository projetoRepository,
                           ManutencaoRepository manutencaoRepository, UsuarioRepository usuarioRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.fornecedorRepository = fornecedorRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.projetoRepository = projetoRepository;
        this.manutencaoRepository = manutencaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {

        if (usuarioRepository.count() > 0) {
            return;
        }

        Fornecedor f1 = fornecedorRepository.save(new Fornecedor(null, "Fornecedor A", "12345678000100", null));

        Fornecedor f2 = fornecedorRepository.save(new Fornecedor(null, "Fornecedor B", "98765432000100", null));

        Projeto p1 = projetoRepository.save(new Projeto(null, "Projeto Alpha", "Sistema de automação industrial", new HashSet<>()));

        Projeto p2 = projetoRepository.save(new Projeto(null, "Projeto Beta", "Monitoramento de sensores", new HashSet<>()));

        Equipamento e1 = equipamentoRepository.save(Equipamento.builder()
            .nome("Sensor IoT")
            .valor(1200.0)
            .categoria(Categoria.ELETRONICO)
            .quantidade(10)
            .fornecedor(f1)
            .build()
        );

        Equipamento e2 = equipamentoRepository.save(Equipamento.builder()
            .nome("Motor Industrial")
            .valor(8000.0)
            .categoria(Categoria.INDUSTRIAL)
            .quantidade(5)
            .fornecedor(f2)
            .build()
        );

        e1.setProjetos(Set.of(p1, p2));
        e2.setProjetos(Set.of(p1));

        p1.getEquipamentos().add(e1);
        p1.getEquipamentos().add(e2);

        p2.getEquipamentos().add(e1);

        projetoRepository.saveAll(List.of(p1, p2));
        equipamentoRepository.saveAll(List.of(e1, e2));

        manutencaoRepository.save(new Manutencao(null, "Troca de sensor", LocalDate.now(), 300.0, e1));

        manutencaoRepository.save(new Manutencao(null, "Calibração do sensor", LocalDate.now().minusDays(1), 150.0, e1));

        manutencaoRepository.save(new Manutencao(null, "Lubrificação motor", LocalDate.now().minusDays(2), 500.0, e2));

        UserRole roleMaster = userRoleRepository.save(new UserRole("ROLE_MASTER"));
        UserRole roleContributor = userRoleRepository.save(new UserRole("ROLE_CONTRIBUTOR"));
        UserRole roleAuditor = userRoleRepository.save(new UserRole("ROLE_AUDITOR"));

        User master = new User();
        master.setUsername("master");
        master.setPassword(passwordEncoder.encode("123"));
        master.setRoles(new HashSet<>(Set.of(roleMaster)));

        User contributor = new User();
        contributor.setUsername("contrib");
        contributor.setPassword(passwordEncoder.encode("123"));
        contributor.setRoles(new HashSet<>(Set.of(roleContributor)));

        User auditor = new User();
        auditor.setUsername("auditor");
        auditor.setPassword(passwordEncoder.encode("123"));
        auditor.setRoles(new HashSet<>(Set.of(roleAuditor)));

        usuarioRepository.saveAll(List.of(master, contributor, auditor));

        System.out.println(">>> Seed inicial COMPLETO carregado com sucesso!");
    }
}
