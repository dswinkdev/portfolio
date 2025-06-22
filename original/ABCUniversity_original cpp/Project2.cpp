#include <iostream>
#include <string>
#include <limits>

// Vulnerability Fix: Added proper input validation using std::getline and input checking

void DisplayWelcome() {
    std::cout << "Welcome to the Client Management Application!" << std::endl;
}

void DisplayMenu() {
    std::cout << "\nPlease select an option:" << std::endl;
    std::cout << "1. Add New Client" << std::endl;
    std::cout << "2. View All Clients" << std::endl;
    std::cout << "3. Exit Program" << std::endl;
}

void AddNewClient() {
    std::cout << "You selected to add a new client." << std::endl;
    // Future Vulnerability Note: Validate all user inputs here before storing or displaying
}

void ViewAllClients() {
    std::cout << "You selected to view all clients." << std::endl;
    // Future Vulnerability Note: Sanitize data display to avoid injection attacks
}

int CheckUserPermissionAccess() {
    std::string username;
    std::cout << "Enter your username to access the system: ";
    
    std::getline(std::cin, username); // Fix: Use getline for safer input

    // Vulnerability: Hardcoded authentication
    if (username == "admin") {
        return 1;
    } else {
        return 0;
    }

    // Fix Recommendation: Implement password-based login with hashed credentials
}

int main() {
    // Custom required output statement
    std::cout << "Created by Derricko Swink - Software Reverse Engineering Project\n" << std::endl;

    if (!CheckUserPermissionAccess()) {
        std::cout << "Access Denied. Exiting program." << std::endl;
        return 0;
    }

    DisplayWelcome();

    int choice = 0;
    std::string input;

    while (true) {
        DisplayMenu();
        std::cout << "Enter your choice: ";
        
        std::getline(std::cin, input); // Fix: Take input as a string

        try {
            choice = std::stoi(input); // Convert safely to int
        } catch (...) {
            std::cout << "Invalid input. Please enter a number." << std::endl;
            continue;
        }

        if (choice == 1) {
            AddNewClient();
        } else if (choice == 2) {
            ViewAllClients();
        } else if (choice == 3) {
            std::cout << "Thank you for using the Client Management Application. Goodbye!" << std::endl;
            break;
        } else {
            std::cout << "Invalid choice. Please try again." << std::endl;
        }
    }

    return 0;
}
