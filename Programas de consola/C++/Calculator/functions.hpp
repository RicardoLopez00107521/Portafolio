#ifndef FUNCTIONS_HPP
#define FUNCTIONS_HPP

#include <limits>

using namespace std;

int sum_two_numbers(int first_number, int second_number) {
    return first_number + second_number;
}

int rest_two_numbers(int first_number, int second_number) {
    return first_number - second_number;
}

int multiply_two_numbers(int first_number, int second_number) {
    return first_number * second_number;
}

string divide_two_numbers(int first_number, int second_number) {
    if (second_number == 0) return "Undefined!";

    return to_string(first_number / second_number);
}

bool validate_entry(int& entry) {
    if (!(cin >> entry)) {
        cout << "Invalid entry, try again!.\n";
        cin.clear(); 
        cin.ignore(numeric_limits<streamsize>::max(), '\n'); 
        return false; 
    }
    return true;
}

#endif