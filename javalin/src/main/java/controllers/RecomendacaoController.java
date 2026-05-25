package com.recomendador.controllers;

import com.recomendador.dtos.request.GerarEquipesRequestDTO;
import com.recomendador.dtos.response.EquipeResponseDTO;
import com.recomendador.models.Equipe;
import com.recomendador.services.RecomendacaoService;
import com.recomendador.strategies.AleatorioStrategy;
import com.recomendador.strategies.RecomendacaoStrategy;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class RecomendacaoController {
    private final RecomendacaoService service;

    public RecomendacaoController(RecomendacaoService service) {
        this.service = service;
    }

    public void gerarEquipes(Context ctx) {
        try {
            String turmaId = ctx.pathParam("id");
            GerarEquipesRequestDTO requestDTO = ctx.bodyAsClass(GerarEquipesRequestDTO.class);

            RecomendacaoStrategy strategy = switch (requestDTO.estrategia() != null ? requestDTO.estrategia() : "") {
                // Aqui você pode adicionar os novos cases para "balanceado", "mesmo_atributo", etc.
                default -> new AleatorioStrategy();
            };

            List<Equipe> equipesModel = service.gerarEValidarEquipes(
                    turmaId,
                    requestDTO.quantidade(),
                    requestDTO.porTamanho(),
                    strategy
            );

            List<EquipeResponseDTO> responseDTOs = equipesModel.stream()
                    .map(EquipeResponseDTO::fromModel)
                    .collect(Collectors.toList());

            ctx.status(200).json(responseDTOs);

        } catch (IllegalArgumentException | IllegalStateException e) {
            ctx.status(400).result(e.getMessage());
        }
    }
}