package com.devdojo.springwebfluxessentials.controller;

import com.devdojo.springwebfluxessentials.domain.Anime;
import com.devdojo.springwebfluxessentials.service.AnimeService;
import com.devdojo.springwebfluxessentials.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0); //NOSONAR
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(animeService.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeService.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeService.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeService.update(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeService.delete(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findAll returns a flux of anime")
    public void findAll_ReturnsFluxOfAnime_WhenSuccessful() {
        StepVerifier.create(animeController.getAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns Mono with anime when it exists")
    public void findById_ReturnsMonoAnime_WhenSuccessful() {
        StepVerifier.create(animeController.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("save creates anime when successful")
    public void save_CreatesAnime_WhenSuccessful() {
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();

        StepVerifier.create(animeController.save(animeToSave))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemovesAnime_WhenSuccessful() {
        StepVerifier.create(animeController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update saves updated anime when successful")
    public void update_SavesUpdatedAnime_WhenSuccessful() {
        Anime updatedAnime = AnimeCreator.createValidAnime();

        StepVerifier.create(animeController.update(1, updatedAnime))
                .expectSubscription()
                .verifyComplete();
    }
}