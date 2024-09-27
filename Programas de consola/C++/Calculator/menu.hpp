#ifndef MENU_HPP
#define MENU_HPP

using namespace std; 

void menu() {
    int option;
    do {
        cout<<"\t\n ****** Menu ******\n";
        cout<<"1. Sum two numbers\n";
        cout<<"2. Rest two numbers\n";
        cout<<"3. Multiply two numbers\n";
        cout<<"4. Divide two numbers\n";
        cout<<"5. Exit\n";
        cout << "Choose operation: ";

        if (!(cin >> option)) {
            cout << "Invalid option, try again!.\n";
            cin.clear(); 
            cin.ignore(numeric_limits<streamsize>::max(), '\n'); 
            continue; 
        }

        switch (option) {
        case 1:
            cout<<"Implment sum fuction\n";
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