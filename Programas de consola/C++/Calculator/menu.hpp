#ifndef MENU_HPP
#define MENU_HPP

#include "functions.hpp"

using namespace std; 

void menu() {

    int option, first_number = 0, second_number = 0; 

    do {
        cout<<"\t\n ****** Menu ******\n";
        cout<<"1. Sum two numbers\n";
        cout<<"2. Rest two numbers\n";
        cout<<"3. Multiply two numbers\n";
        cout<<"4. Divide two numbers\n";
        cout<<"5. Exit\n";
        cout << "Choose operation: ";

        if (!validate_entry(option)) continue;

        switch (option) {
        case 1:

            cout << "\nEscribe el primer numero entero: ";
            if (!validate_entry(first_number)) break;
            cout << "Escribe el se numero entero: ";
            if (!validate_entry(second_number)) break;

            cout << "La suma de " << first_number << " y " << second_number << " es: " << sum_two_numbers(first_number, second_number) << "\n" ;
            break;
        
        case 2:
            cout<<"Implment rest fuction\n";
            break;

        case 3:
            cout<<"Implment multiply fuction\n";
            break;

        case 4:
            cout<<"Implment division fuction\n";
            break;

        default:
            cout<<"Invalid option, try again!.\n";
            break;
        }
    } while (option !=5);
    cout << "\nGood bye!";
}

#endif