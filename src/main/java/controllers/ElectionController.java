package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import models.Candidate;
import models.Voter;

public class ElectionController {
    private static List<Candidate> candidates = new ArrayList<>();
    private static List<Voter> voters = new ArrayList<>();
    private static int candidateIdCounter = 1;
    private static int voterIdCounter = 1;

    public static Map<String, Object> registerCandidate(String name) {
        if (name == null || name.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Candidate name cannot be empty");
            return error;
        }

        for (Candidate candidate : candidates) {
            if (candidate.getName().equalsIgnoreCase(name.trim())) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Candidate already exists");
                return error;
            }
        }

        Candidate candidate = new Candidate(candidateIdCounter++, name.trim());
        candidates.add(candidate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", candidate.getId());
        response.put("name", candidate.getName());
        response.put("voteCount", candidate.getVoteCount());
        return response;
    }

    public static Map<String, Object> registerVoter(String name) {
        if (name == null || name.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Voter name cannot be empty");
            return error;
        }

        for (Voter voter : voters) {
            if (voter.getName().equalsIgnoreCase(name.trim())) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Voter already exists");
                return error;
            }
        }

        Voter voter = new Voter(voterIdCounter++, name.trim());
        voters.add(voter);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", voter.getId());
        response.put("name", voter.getName());
        response.put("hasVoted", voter.hasVoted());
        return response;
    }

    public static Map<String, Object> castVote(int voterId, int candidateId) {
        Voter voter = findVoterById(voterId);
        Candidate candidate = findCandidateById(candidateId);

        if (voter == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Voter not found");
            return error;
        }

        if (candidate == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Candidate not found");
            return error;
        }

        if (voter.hasVoted()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Voter has already voted");
            return error;
        }

        candidate.incrementVoteCount();
        voter.setVoted();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Vote cast successfully");
        response.put("candidateName", candidate.getName());
        response.put("voterName", voter.getName());
        return response;
    }

    public static Map<String, Object> getResults() {
        Map<String, Object> response = new HashMap<>();
        response.put("candidates", candidates);
        response.put("totalCandidates", candidates.size());
        response.put("totalVoters", voters.size());
        response.put("totalVotes", candidates.stream().mapToInt(Candidate::getVoteCount).sum());
        return response;
    }

    public static Map<String, Object> getCandidates() {
        Map<String, Object> response = new HashMap<>();
        response.put("candidates", candidates);
        return response;
    }

    public static Map<String, Object> getVoters() {
        Map<String, Object> response = new HashMap<>();
        response.put("voters", voters);
        return response;
    }

    private static Candidate findCandidateById(int id) {
        for (Candidate candidate : candidates) {
            if (candidate.getId() == id) {
                return candidate;
            }
        }
        return null;
    }

    private static Voter findVoterById(int id) {
        for (Voter voter : voters) {
            if (voter.getId() == id) {
                return voter;
            }
        }
        return null;
    }
}
