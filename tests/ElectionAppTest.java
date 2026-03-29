import org.junit.Test;
import static org.junit.Assert.*;

public class ElectionAppTest {

    @Test
    public void testAddCandidate() {
        ElectionApp app = new ElectionApp();
        Candidate candidate = new Candidate(1, "Test Candidate");
        app.addCandidate(candidate);
        assertEquals(1, app.getCandidates().size());
        assertEquals("Test Candidate", app.getCandidates().get(0).getName());
    }

    @Test
    public void testAddVoter() {
        ElectionApp app = new ElectionApp();
        Voter voter = new Voter(1, "Test Voter");
        app.addVoter(voter);
        assertEquals(1, app.getVoters().size());
        assertEquals("Test Voter", app.getVoters().get(0).getName());
    }

    @Test
    public void testFindCandidateById() {
        ElectionApp app = new ElectionApp();
        Candidate candidate1 = new Candidate(1, "Alice");
        Candidate candidate2 = new Candidate(2, "Bob");
        app.addCandidate(candidate1);
        app.addCandidate(candidate2);
        
        assertEquals("Alice", app.findCandidateById(1).getName());
        assertEquals("Bob", app.findCandidateById(2).getName());
        assertNull(app.findCandidateById(3));
    }

    @Test
    public void testFindVoterById() {
        ElectionApp app = new ElectionApp();
        Voter voter1 = new Voter(1, "Alice");
        Voter voter2 = new Voter(2, "Bob");
        app.addVoter(voter1);
        app.addVoter(voter2);
        
        assertEquals("Alice", app.findVoterById(1).getName());
        assertEquals("Bob", app.findVoterById(2).getName());
        assertNull(app.findVoterById(3));
    }
}
