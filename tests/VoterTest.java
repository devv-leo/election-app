import org.junit.Test;
import static org.junit.Assert.*;

public class VoterTest {

    @Test
    public void testVoterCreation() {
        Voter voter = new Voter(1, "John Doe");
        assertEquals(1, voter.getId());
        assertEquals("John Doe", voter.getName());
        assertFalse(voter.hasVoted());
    }

    @Test
    public void testSetVoted() {
        Voter voter = new Voter(1, "Jane Smith");
        assertFalse(voter.hasVoted());
        voter.setVoted();
        assertTrue(voter.hasVoted());
    }
}
