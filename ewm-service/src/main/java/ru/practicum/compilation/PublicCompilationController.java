package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilationList(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        log.info("В метод getCompilationList переданы данные: pinned = {}, from = {}, size = {}", pinned, from, size);

        return compilationService.getPublicCompilationList(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getPublicCompById(@PathVariable long compId) {
        log.info("В метод getPublicCompById переданы данные: compId = {}", compId);
        return compilationService.getPublicCompilationById(compId);
    }
}