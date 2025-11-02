package graph.scc;

import common.Metrics;
import org.testng.annotations.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {

    @Test
    public void testSimpleSCC() {
        // graph: 0->1->2->0 and 3->4
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < 5; i++) g.add(new ArrayList<>());
        g.get(0).add(1);
        g.get(1).add(2);
        g.get(2).add(0);
        g.get(3).add(4);

        Metrics m = new Metrics();
        TarjanSCC t = new TarjanSCC(g, m);
        SCCResult res = t.computeSCC();
        List<List<Integer>> comps = res.getComponents();

        // Expect 2 components (one size 3, one size 2)
        assertEquals(2, comps.size());
        List<Integer> sizes = res.sizes();
        Collections.sort(sizes);
        assertEquals(Arrays.asList(2,3), sizes);
    }

    @Test
    public void testTrivial() {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < 3; i++) g.add(new ArrayList<>());
        TarjanSCC t = new TarjanSCC(g, new Metrics());
        SCCResult res = t.computeSCC();
        assertEquals(3, res.getComponents().size());
    }
}
