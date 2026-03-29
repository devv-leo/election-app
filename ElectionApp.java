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

    public void registerCandidate() {
        System.out.print("Enter candidate name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        
        for (Candidate candidate : candidates) {
            if (candidate.getName().equalsIgnoreCase(name)) {
                System.out.println("Candidate already exists!");
                return;
            }
        }
        
        int newId = candidates.size() + 1;
        Candidate candidate = new Candidate(newId, name);
        addCandidate(candidate);
        System.out.println("Candidate registered successfully!");
    }

    public void displayCandidates() {
        if (candidates.isEmpty()) {
            System.out.println("No candidates registered yet.");
            return;
        }
        
        System.out.println("\n--- Registered Candidates ---");
        for (Candidate candidate : candidates) {
            System.out.println(candidate.getId() + ". " + candidate.getName());
        }
        System.out.println();
    }

    public void registerVoter() {
        System.out.print("Enter voter name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        
        for (Voter voter : voters) {
            if (voter.getName().equalsIgnoreCase(name)) {
                System.out.println("Voter already exists!");
                return;
            }
        }
        
        int newId = voters.size() + 1;
        Voter voter = new Voter(newId, name);
        addVoter(voter);
        System.out.println("Voter registered successfully!");
    }

    public void displayVoters() {
        if (voters.isEmpty()) {
            System.out.println("No voters registered yet.");
            return;
        }
        
        System.out.println("\n--- Registered Voters ---");
        for (Voter voter : voters) {
            String status = voter.hasVoted() ? "Voted" : "Not Voted";
            System.out.println(voter.getId() + ". " + voter.getName() + " (" + status + ")");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ElectionApp app = new ElectionApp();
        System.out.println("Welcome to my Election App!");
        app.scanner.close();
    }
}
