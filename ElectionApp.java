import java.util.ArrayList;
import java.util.Scanner;

public class ElectionApp {
    private ArrayList<Candidate> candidates;
    private ArrayList<Voter> voters;
    private Scanner scanner;

    public ElectionApp() {
        this.candidates = new ArrayList<>();
        this.voters = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

    public void addVoter(Voter voter) {
        voters.add(voter);
    }

    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    public ArrayList<Voter> getVoters() {
        return voters;
    }

    public static void main(String[] args) {
        ElectionApp app = new ElectionApp();
        System.out.println("Welcome to my Election App!");
        app.scanner.close();
    }
}
