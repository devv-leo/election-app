# Election App

A terminal-based Java application for managing general elections with in-memory storage.

## Features

- Candidate registration and management
- Voter registration with voting status tracking
- Secure voting system with duplicate prevention
- Real-time election results and winner calculation
- Voter turnout statistics

## Usage

1. Run the application:
   ```bash
   java ElectionApp.java
   ```

3. Follow the menu prompts to:
   - Register candidates (up to 15)
   - Register voters (up to 100)
   - Cast votes
   - View election results

## Technical Details

- **Language**: Java
- **Storage**: In-memory using ArrayLists
- **Interface**: Terminal-based
- **Dependencies**: None (standard Java library only)

## Classes

- `ElectionApp`: Main application class with all functionality
- `Candidate`: Represents election candidates with vote tracking
- `Voter`: Represents voters with voting status

## Limitations

- Data is stored in memory only (lost when application closes)
- No persistent storage
- Basic input validation
- Maximum 15 candidates and 100 voters
