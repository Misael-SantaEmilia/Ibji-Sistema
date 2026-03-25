package com.ibji.app.controller;

import com.ibji.app.dto.response.AniversarianteResponseDTO;
import com.ibji.app.service.MembroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aniversariantes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AniversarianteController {

    private final MembroService membroService;

    @GetMapping
    public ResponseEntity<List<AniversarianteResponseDTO>> findAniversariantes(
            @RequestParam(defaultValue = "M") String tipo) {

        List<AniversarianteResponseDTO> aniversariantes = membroService.findAniversariantes(tipo);
        return ResponseEntity.ok(aniversariantes);
    }
}
