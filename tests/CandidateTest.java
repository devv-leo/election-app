import org.junit.Test;

import static org.junit.Assert.*;

public class CandidateTest {

    @Test
    @org.testng.annotations.Test
    public void testCandidateCreation() {
        Candidate candidate = new Candidate(1, "John Doe");
        assertEquals(1, candidate.getId());
        assertEquals("John Doe", candidate.getName());
        assertEquals(0, candidate.getVoteCount());
    }

    @Test
    public void testIncrementVoteCount() {
        Candidate candidate = new Candidate(1, "Jane Smith");
        candidate.incrementVoteCount();
        assertEquals(1, candidate.getVoteCount());
        candidate.incrementVoteCount();
        assertEquals(2, candidate.getVoteCount());
    }
}
