package dev.midplane.fuzzysearch.performance;

import dev.midplane.fuzzysearch.Config;
import dev.midplane.fuzzysearch.entitySearcher.EntitySearcherFactory;
import dev.midplane.fuzzysearch.interfaces.EntityResult;
import dev.midplane.fuzzysearch.interfaces.EntitySearcher;
import dev.midplane.fuzzysearch.interfaces.Query;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class FuzzySearchBenchmark {

    private static EntitySearcher<String, Integer> searcher1k;
    private static EntitySearcher<String, Integer> searcher10k;
    private static EntitySearcher<String, Integer> searcher100k;
    private static EntitySearcher<String, Integer> searcher1m;

    private static List<String> queries;
    private static final Random RANDOM = new Random(System.nanoTime());

    @Setup
    public void setup() throws IOException {
        queries = Files.readAllLines(Path.of("src/test/resources/world-ctvs.txt"));

        searcher1k = EntitySearcherFactory.createSearcher(Config.createDefault());
        searcher1k.indexEntities(queries.subList(0, 1000), String::hashCode, List::of);

        searcher10k = EntitySearcherFactory.createSearcher(Config.createDefault());
        searcher10k.indexEntities(queries.subList(0, 10000), String::hashCode, List::of);

        searcher100k = EntitySearcherFactory.createSearcher(Config.createDefault());
        searcher100k.indexEntities(queries.subList(0, 100000), String::hashCode, List::of);

        searcher1m = EntitySearcherFactory.createSearcher(Config.createDefault());
        searcher1m.indexEntities(queries.subList(0, 1000000), String::hashCode, List::of);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(FuzzySearchBenchmark.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3)
    @Measurement(iterations = 1)
    public EntityResult<String> searcher1k() {
        int queryIndex = RANDOM.nextInt(1000);
        return searcher1k.getMatches(new Query(queries.get(queryIndex)));
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3)
    @Measurement(iterations = 1)
    public EntityResult<String> searcher10k() {
        int queryIndex = RANDOM.nextInt(10000);
        return searcher10k.getMatches(new Query(queries.get(queryIndex)));
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3)
    @Measurement(iterations = 1)
    public EntityResult<String> searcher100k() {
        int queryIndex = RANDOM.nextInt(100000);
        return searcher100k.getMatches(new Query(queries.get(queryIndex)));
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3)
    @Measurement(iterations = 1)
    public EntityResult<String> searcher1m() {
        int queryIndex = RANDOM.nextInt(1000000);
        return searcher1m.getMatches(new Query(queries.get(queryIndex)));
    }
}
