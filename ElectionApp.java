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

    public void castVote() {
        if (candidates.isEmpty()) {
            System.out.println("No candidates available for voting!");
            return;
        }
        
        if (voters.isEmpty()) {
            System.out.println("No voters registered yet!");
            return;
        }
        
        displayCandidates();
        System.out.print("Enter your voter ID: ");
        int voterId = Integer.parseInt(scanner.nextLine().trim());
        
        Voter voter = findVoterById(voterId);
        if (voter == null) {
            System.out.println("Voter not found!");
            return;
        }
        
        if (voter.hasVoted()) {
            System.out.println("You have already voted!");
            return;
        }
        
        System.out.print("Enter candidate ID to vote for: ");
        int candidateId = Integer.parseInt(scanner.nextLine().trim());
        
        Candidate candidate = findCandidateById(candidateId);
        if (candidate == null) {
            System.out.println("Candidate not found!");
            return;
        }
        
        candidate.incrementVoteCount();
        voter.setVoted();
        System.out.println("Vote cast successfully!");
    }

    private Candidate findCandidateById(int id) {
        for (Candidate candidate : candidates) {
            if (candidate.getId() == id) {
                return candidate;
            }
        }
        return null;
    }

    private Voter findVoterById(int id) {
        for (Voter voter : voters) {
            if (voter.getId() == id) {
                return voter;
            }
        }
        return null;
    }

    public void displayResults() {
        if (candidates.isEmpty()) {
            System.out.println("No candidates registered yet!");
            return;
        }
        
        System.out.println("\n--- ELECTION RESULTS ---");
        
        Candidate winner = candidates.get(0);
        int totalVotes = 0;
        
        for (Candidate candidate : candidates) {
            int votes = candidate.getVoteCount();
            totalVotes += votes;
            
            if (votes > winner.getVoteCount()) {
                winner = candidate;
            }
            
            System.out.println(candidate.getName() + ": " + votes + " votes");
        }
        
        System.out.println("\nTotal votes cast: " + totalVotes);
        
        int votedCount = 0;
        for (Voter voter : voters) {
            if (voter.hasVoted()) {
                votedCount++;
            }
        }
        
        System.out.println("Voter turnout: " + votedCount + "/" + voters.size() + 
                           " (" + (voters.isEmpty() ? 0 : (votedCount * 100 / voters.size())) + "%)");
        
        if (totalVotes > 0) {
            System.out.println("\nWINNER: " + winner.getName() + " with " + winner.getVoteCount() + " votes!");
        } else {
            System.out.println("\nNo votes have been cast yet.");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ElectionApp app = new ElectionApp();
        System.out.println("Welcome to my Election App!");
        app.scanner.close();
    }
}
