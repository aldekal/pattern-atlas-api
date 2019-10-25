package com.patternpedia.api.repository;

import com.patternpedia.api.entities.*;
import com.patternpedia.api.repositories.*;
import com.patternpedia.api.util.IntegrationTestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    @Autowired
    private DirectedEdgeRepository directedEdgeRepository;

    @Autowired
    private UndirectedEdgeReository undirectedEdgeReository;

    @Autowired
    private DirectedHyperedgeRepository directedHyperedgeRepository;

    @Autowired
    private UndirectedHyperedgeRepository undirectedHyperedgeRepository;

    @Autowired
    private List<CrudRepository<?, ?>> repositoryList;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;


    @Test
    public void testSavePattern() {
        Pattern p = new Pattern();
        p.setUri("http://patternpedia.org/TestPattern1");
        p.setName("TestPattern1");
        this.patternRepository.save(p);
    }

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void testFailToSaveNonUniquePattern() {
        Pattern p1 = new Pattern();
        p1.setUri("http://patternpedia.org/TestPattern2");
        p1.setName("TestPattern2");
        this.patternRepository.save(p1);

        Pattern p2 = new Pattern();
        p2.setUri("http://patternpedia.org/TestPattern2");
        p2.setName("TestPattern2");
        this.patternRepository.save(p2);
    }

    @Test
    public void testSavePatternLanguage() throws UnsupportedEncodingException {
        PatternLanguage pl = new PatternLanguage();
        pl.setUri("http://patternpedia.org/TestPatternLanguage1");
        pl.setName("TestPatternLanguage1");
        this.patternLanguageRepository.save(pl);
        Assert.assertEquals("http://patternpedia.org/TestPatternLanguage1",
                this.patternLanguageRepository.findByUri(pl.getUri()).get().getUri());
    }

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void testFailToSaveNonUniquePatternLanguage() {
        PatternLanguage pl = new PatternLanguage();
        pl.setUri("http://patternpedia.org/TestPatternLanguage2");
        pl.setName("TestPatternLanguage2");
        this.patternLanguageRepository.save(pl);

        PatternLanguage pl2 = new PatternLanguage();
        pl2.setUri("http://patternpedia.org/TestPatternLanguage2");
        pl2.setName("TestPatternLanguage2");
        this.patternLanguageRepository.save(pl2);
    }

    @Test
    public void testRelatePatternAndPatternLanguage() {
        PatternLanguage pl = new PatternLanguage();
        pl.setUri("http://patternpedia.org/TestPatternLanguage3");
        pl.setName("TestPatternLanguage3");
        this.patternLanguageRepository.save(pl);

        Pattern p = new Pattern();
        p.setUri("http://patternpedia.org/TestPattern3");
        p.setName("TestPattern3");
        this.patternRepository.save(p);
        List<Pattern> list = new ArrayList<>();
        list.add(p);
        pl.setPatterns(list);
        this.patternLanguageRepository.save(pl);
    }

    @Test
    public void testSaveDirectedEdge() {
        Pattern p1 = new Pattern();
        p1.setUri("http://patternpedia.org/TestPattern4");
        p1.setName("TestPattern4");
        this.patternRepository.save(p1);

        Pattern p2 = new Pattern();
        p2.setUri("http://patternpedia.org/TestPattern5");
        p2.setName("TestPattern5");
        this.patternRepository.save(p2);

        DirectedEdge e = new DirectedEdge();
        Map<String, String> map = new HashMap<>();
        map.put("testKey", "testValue");
        e.setDescription(map);
        e.setSource(p1);
        e.setTarget(p2);
        this.directedEdgeRepository.save(e);
    }

    @Test
    public void testSaveUndirectedEdge() {
        Pattern p1 = new Pattern();
        p1.setUri("http://patternpedia.org/TestPattern6");
        p1.setName("TestPattern6");
        this.patternRepository.save(p1);

        Pattern p2 = new Pattern();
        p2.setUri("http://patternpedia.org/TestPattern7");
        p2.setName("TestPattern7");
        this.patternRepository.save(p2);

        UndirectedEdge e = new UndirectedEdge();
        Map<String, String> map = new HashMap<>();
        map.put("testKey", "testValue");
        e.setDescription(map);
        e.setP1(p1);
        e.setP2(p2);
        this.undirectedEdgeReository.save(e);
    }

    @Test
    public void testSaveDirectedHyperEdge() {
        Pattern p1 = new Pattern();
        p1.setUri("http://patternpedia.org/TestPattern8");
        p1.setName("TestPattern8");
        this.patternRepository.save(p1);

        Pattern p2 = new Pattern();
        p2.setUri("http://patternpedia.org/TestPattern9");
        p2.setName("TestPattern9");
        this.patternRepository.save(p2);

        Pattern p3 = new Pattern();
        p3.setUri("http://patternpedia.org/TestPattern10");
        p3.setName("TestPattern10");
        this.patternRepository.save(p3);

        Pattern p4 = new Pattern();
        p4.setUri("http://patternpedia.org/TestPattern11");
        p4.setName("TestPattern11");
        this.patternRepository.save(p4);

        DirectedHyperedge e = new DirectedHyperedge();
        Map<String, String> map = new HashMap<>();
        map.put("testKey", "testValue");
        e.setDescription(map);

        Set<Pattern> sourceSet = new HashSet<>();
        sourceSet.add(p1);
        sourceSet.add(p2);
        e.setSourceSet(sourceSet);

        Set<Pattern> targetSet = new HashSet<>();
        targetSet.add(p3);
        targetSet.add(p4);
        e.setTargetSet(targetSet);

        this.directedHyperedgeRepository.save(e);
    }

    @Test
    public void testSaveUndirectedHyperEdge() {
        Pattern p1 = new Pattern();
        p1.setUri("http://patternpedia.org/TestPattern12");
        p1.setName("TestPattern12");
        this.patternRepository.save(p1);

        Pattern p2 = new Pattern();
        p2.setUri("http://patternpedia.org/TestPattern13");
        p2.setName("TestPattern13");
        this.patternRepository.save(p2);

        UndirectedHyperedge e = new UndirectedHyperedge();
        Map<String, String> map = new HashMap<>();
        map.put("testKey", "testValue");
        e.setDescription(map);

        Set<Pattern> s = new HashSet<>();
        s.add(p1);
        s.add(p2);
        e.setPatterns(s);

        this.undirectedHyperedgeRepository.save(e);

    }

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void testFailSaveDirectedEdgeOnSourceIsNull() {
        Pattern p2 = new Pattern();
        p2.setUri("http://patternpedia.org/TestPattern14");
        p2.setName("TestPattern14");
        this.patternRepository.save(p2);

        DirectedEdge e = new DirectedEdge();
        Map<String, String> map = new HashMap<>();
        map.put("testKey", "testValue");
        e.setDescription(map);
        e.setTarget(p2);
        this.directedEdgeRepository.save(e);
    }

    @Test
    public void testRenamePattern() throws Exception {
        Pattern p = new Pattern();
        p.setName("TestPattern");
        p.setUri("http://patternpedia.org/TestPattern");
        p = this.patternRepository.save(p);
        p.setUri("http://patternpedia.org/RenamedPattern");
        this.patternRepository.save(p);
        if (this.patternRepository.existsByUri("http://patternpedia.org/TestPattern")) {
            System.out.println("Old Pattern still there!");
        }
        if (this.patternRepository.existsByUri("http://patternpedia.org/RenamedPattern")) {
            System.out.println("New Pattern is there");
        }
    }

}
